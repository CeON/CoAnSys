/*
 * This file is part of CoAnSys project.
 * Copyright (c) 20012-2013 ICM-UW
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

import org.apache.hadoop.mapreduce.Reducer;
import org.springframework.beans.BeansException;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/** 
 * An implementation of the {@link Reducer} class that facilitates the use of the spring framework in the reduce phase.
 * 
 * The class starts the spring application context in the {@link #setup(org.apache.hadoop.mapreduce.Reducer.Context)} method and uses
 * {@link DiReducerService} spring bean to perform the business logic in the {@link #reduce(Object, Iterable, org.apache.hadoop.mapreduce.Reducer.Context)}
 * method.
 * 
 *  
 * @author lukdumi
 *
 */
public final class DiReducer<KEYIN, VALUEIN, KEYOUT, VALUEOUT> extends Reducer<KEYIN, VALUEIN, KEYOUT, VALUEOUT> {

    
    public static final String DI_REDUCE_SERVICE_BEAN_NAME = "diReduceServiceBeanName";
    public static final String DI_REDUCE_APPLICATION_CONTEXT_PATH = "diReduceApplicationContextPath";
    
    
    private DiReduceService<KEYIN, VALUEIN, KEYOUT, VALUEOUT> diReduceService;
    
    
    private ClassPathXmlApplicationContext appCtx;
    
    
    
    /**
     * Starts the spring application context from the path pointed by {@link #DI_REDUCE_APPLICATION_CONTEXT_PATH} attribute
     * of the {@link Context#getConfiguration()} and then looks for the spring bean under the name given in {@link #DI_REDUCE_SERVICE_BEAN_NAME} attribute. 
     * The bean is then used to perform the business logic in the {@link #reduce(Object, Iterable, org.apache.hadoop.mapreduce.Reducer.Context)} method.
     */
    @Override
    protected void setup(Context context) throws IOException, InterruptedException, BeansException {
        String diReduceApplicationContextPath = context.getConfiguration().get(DI_REDUCE_APPLICATION_CONTEXT_PATH);
        String diReduceServiceBeanName = context.getConfiguration().get(DI_REDUCE_SERVICE_BEAN_NAME);
        
        appCtx = new ClassPathXmlApplicationContext(diReduceApplicationContextPath);
        @SuppressWarnings("unchecked")
        DiReduceService<KEYIN, VALUEIN, KEYOUT, VALUEOUT> bean = (DiReduceService<KEYIN, VALUEIN, KEYOUT, VALUEOUT>)appCtx.getBean(diReduceServiceBeanName);
        diReduceService = bean;
    }

    
    @Override
    protected void reduce(KEYIN key, Iterable<VALUEIN> values, Context context) throws IOException, InterruptedException {
        diReduceService.reduce(key, values, context);
    }

    
    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
       appCtx.close();
    }
    
    
    
    
}
