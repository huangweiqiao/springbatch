package com.hwq.springbatch.itemreader;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.Iterator;
import java.util.List;

/**
 * Created by hwq on 2020/2/29.
 */
public class MyReader implements ItemReader<String> {

    private Iterator<String> iterator;

    public MyReader(List<String> data) {
       this.iterator =  data.iterator();
    }

    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        //一个数据一个数据的读
        if (iterator.hasNext())
            return iterator.next();
        else
            return null;
    }
}
