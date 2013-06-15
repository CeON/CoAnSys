package pl.edu.icm.coansys.commons.spring;

import java.io.IOException;

import org.apache.hadoop.mapreduce.Mapper;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class DiMapper<KEYIN, VALUEIN, KEYOUT, VALUEOUT> extends Mapper<KEYIN, VALUEIN, KEYOUT, VALUEOUT>  {
    
    //private static Logger log = LoggerFactory.getLogger(DiMapper.class);
   
    public static String DI_MAP_SERVICE_BEAN_NAME = "diMapServiceBeanName";
    public static String DI_MAP_APPLICATION_CONTEXT_PATH = "diMapApplicationContextPath";
    
    
    private DiMapService<KEYIN, VALUEIN, KEYOUT, VALUEOUT> diMapService;
    private ClassPathXmlApplicationContext appCtx;
    
    
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