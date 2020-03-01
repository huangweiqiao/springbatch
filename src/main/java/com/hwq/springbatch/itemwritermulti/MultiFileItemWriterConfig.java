package com.hwq.springbatch.itemwritermulti;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hwq.springbatch.itemreaderfile.Customer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.classify.Classifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hwq on 2020/3/1.
 */
@Configuration
public class MultiFileItemWriterConfig {
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

    //不需要按照数据分类
    /*@Bean
    public ItemWriter <Customer> multiFileItemWriter() throws Exception {
        //不按照数据分类 输出到多个文件

        CompositeItemWriter writer = new CompositeItemWriter();
        //实际上是委派 其他writer去写
        writer.setDelegates(Arrays.asList(fileItemWriter(),xmlItemWriter()));
        writer.afterPropertiesSet();
        return writer;
    }*/

    //按照数据分类写入到不同的文件
    @Bean
    public ClassifierCompositeItemWriter  multiFileItemWriter() throws Exception{
        //按照数据分类写入到不同的文件
        ClassifierCompositeItemWriter<Customer> writer = new ClassifierCompositeItemWriter<Customer>();
        writer.setClassifier(new Classifier<Customer, ItemWriter<? super Customer>>() {
            @Override
            public ItemWriter<? super Customer> classify(Customer customer) {
                //按照id进行分类
                try {
                    return customer.getId()%2==0?fileItemWriter():xmlItemWriter();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
        return writer;
    }

}
