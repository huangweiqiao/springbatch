package com.hwq.springbatch.itemwriterxml;

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
public class XmlItemWriterDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    @Qualifier("dbJdbcReader")
    private ItemReader<Customer> dbJdbcReader;

    @Autowired
    @Qualifier("xmlItemWriter")
    private ItemWriter<Customer> xmlItemWriter;

    @Bean
    public Job xmlItemWriterDemoJob(){
        return jobBuilderFactory.get("xmlItemWriterDemoJob")
                .start(xmlItemWriterDemoStep())
                .build();
    }

    @Bean
    public Step xmlItemWriterDemoStep() {
        return stepBuilderFactory.get("xmlItemWriterDemoStep")
                .<Customer,Customer>chunk(2)
                .reader(dbJdbcReader)
                .writer(xmlItemWriter)
                .build();
    }
}
