package com.hwq.springbatch.itemreaderfile;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by hwq on 2020/3/1.
 */
@Component("flatFileWriter")
public class FlatFileWriter implements ItemWriter<Customer> {
    @Override
    public void write(List<? extends Customer> list) throws Exception {
        System.out.println("开始输出...");
        for (Customer customer:list){
            System.out.println(customer);
        }
        System.out.println("结束输出...");
    }
}
