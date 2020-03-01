package com.hwq.springbatch.itemwriterxml;

import com.hwq.springbatch.itemreaderfile.Customer;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hwq on 2020/3/1.
 */
@Configuration
public class XmlItemWriterConfig {
    @Bean
    public StaxEventItemWriter<Customer> xmlItemWriter() throws Exception {
        //定义writer对象
        StaxEventItemWriter writer = new StaxEventItemWriter();
        //将查询出来的Customer对象与xml映射
        XStreamMarshaller marshaller = new XStreamMarshaller();
        Map<String,Class> aliases = new HashMap<String,Class>();
        aliases.put("customer",Customer.class);
        marshaller.setAliases(aliases);
        writer.setRootTagName("customers");
        writer.setMarshaller(marshaller);
        //指定输出到哪个文件里
        String path = "g:\\customer.xml";
        writer.setResource(new FileSystemResource(path));
        //检查下
        writer.afterPropertiesSet();
        return writer;
    }
}
