package com.hwq.springbatch.retry;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * Created by hwq on 2020/3/1.
 */
@Component("retryItemProcessor")
public class RetryItemProcessor implements ItemProcessor<String,String>{
    private int attemptCount = 0;
    @Override
    public String process(String s) throws Exception {
        System.out.println("processing item "+s);
        if (s.equals(26+"")){
            attemptCount++;
            if (attemptCount>=3){
                System.out.println("Retryed "+attemptCount +"times success.");
                return String.valueOf(Integer.valueOf(s)*-1);
            }else{
                System.out.println("Processed the " +attemptCount +" times fail.");
                throw new CustomRetryException("Process failed . Attempt:"+attemptCount);
            }
        }else {
            return String.valueOf(Integer.valueOf(s)*-1);
        }
    }
}
