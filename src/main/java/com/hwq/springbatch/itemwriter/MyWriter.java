package com.hwq.springbatch.itemwriter;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by hwq on 2020/3/1.
 */
@Component("myWriter")
public class MyWriter implements ItemWriter<String> {
    @Override
    public void write(List<? extends String> list) throws Exception {
        for (String str:list){
            System.out.print(str+",");
        }
        System.out.println("已经输出"+list.size());

    }
}
