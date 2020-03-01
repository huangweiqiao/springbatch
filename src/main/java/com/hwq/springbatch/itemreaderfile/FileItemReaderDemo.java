package com.hwq.springbatch.itemreaderfile;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.validation.BindException;

/**
 * Created by hwq on 2020/3/1.
 * 从普通文件中读取
 */
@Configuration
@EnableBatchProcessing
public class FileItemReaderDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    @Qualifier("flatFileWriter")
    private ItemWriter<Customer> flatFileWriter;
    @Bean
    public Job FileItemReaderDeamJob(){
        return jobBuilderFactory.get("FileItemReaderDeamJob")
                .start(fileItemReaderDemoStep())
                .build();
    }

    @Bean
    public Step fileItemReaderDemoStep() {
        return stepBuilderFactory.get("fileItemReaderDemoStep")
                .<Customer,Customer>chunk(2)
                .reader(FlatFileReader())
                .writer(flatFileWriter)
                .build();
    }

    @Bean
    public FlatFileItemReader< Customer> FlatFileReader() {
        //从普通文件中读取的api
        FlatFileItemReader<Customer> reader = new FlatFileItemReader<Customer>();
        //指定从哪个文件中读取
        reader.setResource(new ClassPathResource("customer.txt"));
        //跳过第几行
        reader.setLinesToSkip(1);
        //解析数据
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        //指定表头
        tokenizer.setNames(new String[]{"id","firstName","lastName","birthday"});
        //把解析出来的一行数据映射从Customer对象
        DefaultLineMapper<Customer> mapper = new DefaultLineMapper<>();
        mapper.setLineTokenizer(tokenizer);
        mapper.setFieldSetMapper(new FieldSetMapper<Customer>() {
            @Override
            public Customer mapFieldSet(FieldSet fieldSet) throws BindException {
                Customer customer = new Customer();
                customer.setId(fieldSet.readLong("id"));
                customer.setFirstName(fieldSet.readString("firstName"));
                customer.setLastName(fieldSet.readString("lastName"));
                customer.setBirthday(fieldSet.readString("birthday"));
                return customer;
            }
        });
        //做一下检查
        mapper.afterPropertiesSet();
        reader.setLineMapper(mapper);
        return reader;
    }
}
