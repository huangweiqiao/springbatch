package com.hwq.springbatch.restart;

import com.hwq.springbatch.itemreaderfile.Customer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by hwq on 2020/3/1.
 */
@Component("restartWriter")
public class RestartWriter implements ItemWriter<Customer> {
    @Override
    public void write(List<? extends Customer> list) throws Exception {
        System.out.println("输出数据开始。。。");
        for (Customer customer:list){
            System.out.println(customer);
        }
        System.out.println("输出数据结束。。。");
    }
}
