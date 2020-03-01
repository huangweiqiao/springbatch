package com.hwq.springbatch.itemreaderxml;

import com.hwq.springbatch.itemreaderfile.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hwq on 2020/3/1.
 * 从xml文件中读取数据
 */
@Configuration
@EnableBatchProcessing
public class XmlItemReaderDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    @Qualifier("xmlFileWriter")
    private ItemWriter<Customer> xmlFileWriter;

    @Bean
    public Job xmlItemReaderDemoJob(){
        return jobBuilderFactory.get("xmlItemReaderDemoJob")
                .start(xmlItemReaderDemoJobStep())
                .build();
    }

    @Bean
    public Step xmlItemReaderDemoJobStep() {
        return stepBuilderFactory.get("xmlItemReaderDemoJobStep")
                .<Customer,Customer>chunk(2)
                .reader(xmlFileReader())
                .writer(xmlFileWriter)
                .build();
    }

    @Bean
    public StaxEventItemReader<Customer> xmlFileReader() {
        //从xml文件里都
        StaxEventItemReader<Customer> reader = new StaxEventItemReader<Customer>();
        reader.setResource(new ClassPathResource("customer.xml"));
        //指定根标签，就是表示每一实体对象的
        reader.setFragmentRootElementName("customer");
        //把xml数据转换成对象
        XStreamMarshaller unmarshaller = new XStreamMarshaller();
        Map<String,Class> map = new HashMap<>();
        map.put("customer",Customer.class);
        unmarshaller.setAliases(map);
        reader.setUnmarshaller(unmarshaller);
        return reader;
    }
}
