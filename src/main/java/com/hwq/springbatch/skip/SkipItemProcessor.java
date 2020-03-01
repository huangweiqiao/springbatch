package com.hwq.springbatch.skip;

import com.hwq.springbatch.retry.CustomRetryException;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * Created by hwq on 2020/3/1.
 */
@Component("skipItemProcessor")
public class SkipItemProcessor implements ItemProcessor<String,String> {
    @Override
    public String process(String s) throws Exception {
        System.out.println("processing item "+s);
        if (s.equals(26+"")){
            System.out.println("Processed the " +s +" is fail.");
            throw new CustomRetryException("Process failed . item:"+s);
        }else {
            return String.valueOf(Integer.valueOf(s)*-1);
        }
    }
}
