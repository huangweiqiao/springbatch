package com.hwq.springbatch.listener;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * Created by hwq on 2020/2/29.
 * 监听器demo
 */
@Configuration
@EnableBatchProcessing
public class ListenerDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job listenerJob(){
        return jobBuilderFactory.get("listenerJob")
                .start(listenerJobstep1())
                .listener(new MyJobListener()) //注册监听器
                .build();

    }

    @Bean
    public Step listenerJobstep1() {
        return stepBuilderFactory.get("listenerJobstep1")
                .<String,String>chunk(2) //chunk实现数据的读取，2表示每读取2个数据做相应的处理, 因为chunk有 read读数据，process处理数据，write写数据
        .faultTolerant() //容错的
        .listener(new MyChunkListener())
          .reader(read()) //读数据
                .writer(writer()) //写数据
        .build();


    }

    private ItemWriter<String> writer() {
        return new ItemWriter<String>() {
            @Override
            public void write(List<? extends String> list) throws Exception {
                System.out.println("开始写...");
                for (String str:list){
                    System.out.println(str);
                }
            }
        };
    }

    private ItemReader<String> read() {
        return new ListItemReader<>(Arrays.asList("java","spring","mybatis"));
    }
}
