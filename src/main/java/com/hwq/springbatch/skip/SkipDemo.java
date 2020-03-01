package com.hwq.springbatch.skip;

import com.hwq.springbatch.retry.CustomRetryException;
import org.springframework.batch.core.Job;
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
 * 当发生异常时直接跳过
 */
@Configuration
@EnableBatchProcessing
public class SkipDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    @Qualifier("skipItemProcessor")
    private ItemProcessor<String,String> skipItemProcessor;

    @Bean
    public Job skipDemoJob(){
        return jobBuilderFactory.get("skipDemoJob")
                .start(skipDemoStep())
                .build();
    }

    @Bean
    public Step skipDemoStep() {
        return stepBuilderFactory.get("skipDemoStep")
                .<String,String>chunk(10)
                .reader(reader())
                .processor(skipItemProcessor)
                .writer(writer())
                .faultTolerant()
                .skip(CustomRetryException.class)
                .skipLimit(5)
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
