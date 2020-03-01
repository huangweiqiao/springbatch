package com.hwq.springbatch.errorskiplistener;

import com.hwq.springbatch.retry.CustomRetryException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hwq on 2020/3/1.
 * 任务在执行时出现异常，这时如果跳过但有想记录是哪些数据跳过了，
 * 这里可以加监听器 SkipListener 来实现
 */
@Configuration
@EnableBatchProcessing
public class SkipListenerDemo {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    @Qualifier("errorSkipProcessor")
    private ItemProcessor<String, String> errorSkipProcessor;

    @Autowired
    @Qualifier("mySkipListener")
    private SkipListener<String, String> mySkipListener;

    @Bean
    public Job skipListenerDemoJob(){
        return jobBuilderFactory.get("skipListenerDemoJob")
                .start(skipListenerDemoStep())
                .build();
    }

    @Bean
    public Step skipListenerDemoStep() {
        return stepBuilderFactory.get("skipListenerDemoStep")
                .<String,String>chunk(10)
                .reader(reader())
                .processor(errorSkipProcessor)
                .writer(writer())
                .faultTolerant() //容错
                .skip(CustomRetryException.class) //发生异常跳过
                .skipLimit(10)
                .listener(mySkipListener)  //如果想记录下个哪些数据被跳过了，可以在这里加监听
                .build();
    }

    @Bean
    public ItemWriter<String> writer() {
        return new ItemWriter<String>() {
            @Override
            public void write(List<? extends String> list) throws Exception {
                for (String str:list){
                    System.out.println(str);
                }
            }
        };
    }

    @Bean
    public ListItemReader<String> reader() {
        List<String> list = new ArrayList<>();
        for (int i=0;i<60;i++){
            list.add(i+"");
        }
        ListItemReader<String> reader = new ListItemReader<>(list);
        return reader;
    }


}
