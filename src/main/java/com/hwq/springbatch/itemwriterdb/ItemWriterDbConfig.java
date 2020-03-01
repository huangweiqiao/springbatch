package com.hwq.springbatch.itemwriterdb;

import com.hwq.springbatch.itemreaderfile.Customer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Created by hwq on 2020/3/1.
 * 向数据库中写入数据，读数据是一条条的读，但是写数据是一批一批的写，具体多少条一批看 step的chunk设置值
 */
@Configuration
public class ItemWriterDbConfig {
    @Autowired
    private DataSource dataSource;

    @Bean
    public JdbcBatchItemWriter<Customer> itemWriterDb(){
        JdbcBatchItemWriter writer = new JdbcBatchItemWriter();
        writer.setDataSource(dataSource);
        writer.setSql("insert into customer (id,firstName,lastName,birthday) values"+
        "(:id,:firstName,:lastName,:birthday)");
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Customer>());
        return writer;
    }
}
