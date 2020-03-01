package com.hwq.springbatch.restart;

import com.hwq.springbatch.itemreaderfile.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by hwq on 2020/3/1.
 */
@Configuration
@EnableBatchProcessing
public class RestartReaderDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    @Qualifier("restartReader")
    private ItemReader<Customer> restartReader;

    @Autowired
    @Qualifier("restartWriter")
    private ItemWriter<Customer> restartWriter;

    @Bean
    public Job restartReaderDemoJob(){
        return jobBuilderFactory.get("restartReaderDemoJob")
                .start(restartReaderDemoStep())
                .build();
    }

    @Bean
    public Step restartReaderDemoStep() {
        return stepBuilderFactory.get("restartReaderDemoStep")
                .<Customer,Customer>chunk(2)
                .reader(restartReader)
                .writer(restartWriter)
                .build();
    }



}
