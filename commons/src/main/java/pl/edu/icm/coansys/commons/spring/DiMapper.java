/*
 * This file is part of CoAnSys project.
 * Copyright (c) 2012-2015 ICM-UW
 * 
 * CoAnSys is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * CoAnSys is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with CoAnSys. If not, see <http://www.gnu.org/licenses/>.
 */

package pl.edu.icm.coansys.commons.spring;

import java.io.IOException;

import org.apache.hadoop.mapreduce.Mapper;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/** 
 * An implementation of the {@link Mapper} class that facilitates the use of the spring framework in the map phase.
 * 
 * The class starts the spring application context in the {@link #setup(org.apache.hadoop.mapreduce.Mapper.Context)} method and uses
 * {@link DiMapperService} spring bean to perform the business logic in the {@link #map(Object, Object, org.apache.hadoop.mapreduce.Mapper.Context)}
 * method.
 * 
 *  
 * @author lukdumi
 *
 */
public final class DiMapper<KEYIN, VALUEIN, KEYOUT, VALUEOUT> extends Mapper<KEYIN, VALUEIN, KEYOUT, VALUEOUT>  {
    
    //private static Logger log = LoggerFactory.getLogger(DiMapper.class);
   
    public static final String DI_MAP_SERVICE_BEAN_NAME = "diMapServiceBeanName";
    public static final String DI_MAP_APPLICATION_CONTEXT_PATH = "diMapApplicationContextPath";
    
    
    private DiMapService<KEYIN, VALUEIN, KEYOUT, VALUEOUT> diMapService;
    private ClassPathXmlApplicationContext appCtx;
    
    
    /**
     * Starts the spring application context from the path pointed by {@link #DI_MAP_APPLICATION_CONTEXT_PATH} attribute
     * of the {@link Context#getConfiguration()} and then looks for the spring bean under the name given in {@link #DI_MAP_SERVICE_BEAN_NAME} attribute. 
     * The bean is then used to perform the business logic in the {@link #map(Object, Object, org.apache.hadoop.mapreduce.Mapper.Context)} method.
     */
    @Override
    protected void setup(Context context) throws IOException ,InterruptedException {
        String diMapApplicationContextPath = context.getConfiguration().get(DI_MAP_APPLICATION_CONTEXT_PATH);
        String diMapServiceBeanName = context.getConfiguration().get(DI_MAP_SERVICE_BEAN_NAME);
        
        appCtx = new ClassPathXmlApplicationContext(diMapApplicationContextPath);
        @SuppressWarnings("unchecked")
        DiMapService<KEYIN, VALUEIN, KEYOUT, VALUEOUT> bean = (DiMapService<KEYIN, VALUEIN, KEYOUT, VALUEOUT>)appCtx.getBean(diMapServiceBeanName);
        diMapService = bean;
    };
    
    
    @Override
    protected void map(KEYIN key, VALUEIN value, Context context) throws IOException, InterruptedException {
        diMapService.map(key, value, context);
    }
    
    
    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        appCtx.close();
    }
    
}