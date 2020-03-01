package com.hwq.springbatch.itemprocessor;

import com.hwq.springbatch.itemreaderfile.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * Created by hwq on 2020/3/1.
 */
@Configuration
@EnableBatchProcessing
public class ItemProcessDemo {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    @Qualifier("dbJdbcReader")
    private ItemReader<Customer> dbJdbcReader;

    @Autowired
    @Qualifier("fileItemWriter")
    private ItemWriter<Customer> fileItemWriter;

    @Autowired
    @Qualifier("firstNameUpperProcessor")
    private ItemProcessor<Customer, Customer> firstNameUpperProcessor;

    @Autowired
    @Qualifier("idFirsterProcessor")
    private ItemProcessor<Customer, Customer> idFirsterProcessor;


    @Bean
    public Job fileItemWriterDemoJob(){
        return jobBuilderFactory.get("fileItemWriterDemoJob")
                .start(fileItemWriterDemoStep())
                .build();
    }

    @Bean
    public Step fileItemWriterDemoStep() {
        return stepBuilderFactory.get("fileItemWriterDemoStep")
                .<Customer,Customer>chunk(2)
                .reader(dbJdbcReader)//读
                .processor(process()) //处理
                .writer(fileItemWriter) //写
                .build();
    }

    //有多种数据处理方式
    @Bean
    public CompositeItemProcessor<Customer,Customer> process(){
        CompositeItemProcessor <Customer,Customer>processor = new CompositeItemProcessor<Customer,Customer>();
        processor.setDelegates(Arrays.asList(firstNameUpperProcessor,idFirsterProcessor));
        return processor;
    }
}
