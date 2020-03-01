package com.hwq.springbatch.retry;

/**
 * Created by hwq on 2020/3/1.
 */
public class CustomRetryException extends Exception {
    public CustomRetryException(){
        super();
    }
    public CustomRetryException(String msg){
        super(msg);
    }
}
