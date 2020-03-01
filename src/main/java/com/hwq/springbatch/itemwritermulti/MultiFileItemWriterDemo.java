package com.hwq.springbatch.itemwritermulti;

import com.hwq.springbatch.itemreaderfile.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by hwq on 2020/3/1.
 * 写入到多个文件
 */
@Configuration
@EnableBatchProcessing
public class MultiFileItemWriterDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    @Qualifier("dbJdbcReader")
    private ItemReader<Customer> dbJdbcReader;

    @Autowired
    @Qualifier("multiFileItemWriter")
    private ItemWriter<Customer> multiFileItemWriter;

    @Autowired
    @Qualifier("fileItemWriter")
    private ItemStreamWriter<Customer> fileItemWriter; //按数据分类写入文件时需要配置这两个

    @Autowired
    @Qualifier("xmlItemWriter")
    private ItemStreamWriter <Customer> xmlItemWriter; //按数据分类写入文件时需要配置这两个


    @Bean
    public Job multiFileItemWriterDemoJob(){
        return jobBuilderFactory.get("multiFileItemWriterDemoJob")
                .start(multiFileItemWriterDemoStep())
                .build();
    }

    @Bean
    public Step multiFileItemWriterDemoStep() {
        return stepBuilderFactory.get("multiFileItemWriterDemoStep")
                .<Customer,Customer>chunk(2)
                .reader(dbJdbcReader)
                .writer(multiFileItemWriter)
                .stream(fileItemWriter) //按数据分类写入文件时需要配置这两个
                .stream(xmlItemWriter) //按数据分类写入文件时需要配置这两个
                .build();
    }
}
