package com.hwq.springbatch.itemwriterfile;

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
public class FileItemWriteDemo {
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
                .reader(dbJdbcReader)
                .writer(fileItemWriter)
                .build();
    }
}
