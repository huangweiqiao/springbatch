package com.hwq.springbatch.itemreaderdb;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by hwq on 2020/2/29.
 */
@Component("dbJdbcWriter")
public class DbJdbcWriter implements ItemWriter<User> {
    @Override
    public void write(List<? extends User> list) throws Exception {
        System.out.println("输出开始。。。");
        for (User user:list){
            System.out.println(user.toString());
        }
        System.out.println("输出结束。。。");
    }
}
