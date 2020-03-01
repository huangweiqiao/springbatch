package com.hwq.springbatch.itemwriterdb;

import com.hwq.springbatch.itemreaderfile.Customer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.validation.BindException;

/**
 * Created by hwq on 2020/3/1.
 */
@Configuration
public class FlatFileReaderConfig {
    @Bean
    public FlatFileItemReader< Customer> flatFileReader() {
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
