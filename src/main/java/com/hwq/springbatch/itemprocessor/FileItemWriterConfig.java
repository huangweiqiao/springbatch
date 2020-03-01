package com.hwq.springbatch.itemprocessor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hwq.springbatch.itemreaderfile.Customer;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

/**
 * Created by hwq on 2020/3/1.
 */
@Configuration
public class FileItemWriterConfig {
    @Bean
    public FlatFileItemWriter<Customer> fileItemWriter() throws Exception {
        //把Customer对象转换成字符串输出到文件
        FlatFileItemWriter<Customer> writer = new FlatFileItemWriter<Customer>();
        String path="g:\\customer.txt";
        writer.setResource(new FileSystemResource(path));
        //把Customer对象转换成字符串
        writer.setLineAggregator(new LineAggregator<Customer>() {
            ObjectMapper mapper = new ObjectMapper();
            @Override
            public String aggregate(Customer customer) {
                String str= null;
                try {
                    //这里是JSON字符串
                    str = mapper.writeValueAsString(customer);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                return  str;
            }
        });
        //做一下检查
        writer.afterPropertiesSet();
        return writer;
    }
}
