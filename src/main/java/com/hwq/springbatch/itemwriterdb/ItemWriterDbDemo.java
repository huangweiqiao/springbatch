package com.hwq.springbatch.itemwriterdb;

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
public class ItemWriterDbDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    @Qualifier("flatFileReader")
    private ItemReader<Customer> flatFileReader;

    @Autowired
    @Qualifier("itemWriterDb")
    private ItemWriter<Customer> itemWriterDb;

    @Bean
    public Job itemWriterDbDemoJob(){
        return jobBuilderFactory.get("itemWriterDbDemoJob")
                .start(itemWriterDbDemoStep())
                .build();
    }

    @Bean
    public Step itemWriterDbDemoStep() {
        return stepBuilderFactory.get("itemWriterDbDemoStep")
                .<Customer,Customer>chunk(2)
                .reader(flatFileReader)
                .writer(itemWriterDb)
                .build();
    }
}
