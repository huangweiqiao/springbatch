package com.hwq.springbatch.retry;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
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
 * 发生异常时如果不想让springbatch马上停止任务，可以这样处理
 */
@Configuration
@EnableBatchProcessing
public class RetryDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    @Qualifier("retryItemProcessor")
    private ItemProcessor<String,String> retryItemProcessor;

    @Bean
    public Job retryDemoJob(){
        return jobBuilderFactory.get("retryDemoJob")
                .start(retryDemoStep())
                .build();
    }

    @Bean
    public Step retryDemoStep() {
        return stepBuilderFactory.get("retryDemoStep")
                .<String,String>chunk(10)
                .reader(reader())
                .writer(writer())
                .processor(retryItemProcessor)
                .faultTolerant() //容错，发生异常时不要马上停止任务
                .retry(CustomRetryException.class) //当发现CustomRetryException异常时进行重试
                .retryLimit(5) //最多重试5次 ，不管时在read 还时 processor 还时writer 里重试，总共加起来不超过5次
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
