package pl.edu.icm.coansys.commons.spring;

import java.io.IOException;

import org.apache.hadoop.mapreduce.Reducer;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class DiReducer<KEYIN, VALUEIN, KEYOUT, VALUEOUT> extends Reducer<KEYIN, VALUEIN, KEYOUT, VALUEOUT> {

    
    public static String DI_REDUCE_SERVICE_BEAN_NAME = "diReduceServiceBeanName";
    public static String DI_REDUCE_APPLICATION_CONTEXT_PATH = "diReduceApplicationContextPath";
    
    
    private DiReduceService<KEYIN, VALUEIN, KEYOUT, VALUEOUT> diReduceService;
    
    
    private ClassPathXmlApplicationContext appCtx;
    
    
    
    
    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
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
