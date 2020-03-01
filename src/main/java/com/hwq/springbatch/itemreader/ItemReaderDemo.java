package com.hwq.springbatch.itemreader;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * Created by hwq on 2020/2/29.
 */
@Configuration
@EnableBatchProcessing
public class ItemReaderDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job itemReaderDemoJob(){
        return jobBuilderFactory.get("itemReaderDemoJob")
                .start(itemReaderDemoStep())
                .build();
    }

    @Bean
    public Step itemReaderDemoStep() {
        return stepBuilderFactory.get("itemReaderDemoStep")
                .<String,String>chunk(2)
                .reader(itemReaderDemoRead())
                .writer(list->{
                    System.out.println("输出开始...");
                    for (String str:list){
                        System.out.println(str+"...");
                    }
                    System.out.println("输出结束...");
                }).build();

    }

    @Bean
    public MyReader itemReaderDemoRead() {
        List <String> data = Arrays.asList("cat","dog","pig","duck");
        return new MyReader(data);
    }

}
