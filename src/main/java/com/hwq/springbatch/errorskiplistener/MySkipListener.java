package com.hwq.springbatch.errorskiplistener;

import org.springframework.batch.core.SkipListener;
import org.springframework.stereotype.Component;

/**
 * Created by hwq on 2020/3/1.
 */
@Component("mySkipListener")
public class MySkipListener implements SkipListener<String,String>{ //这个两个泛型是 读取的类型和输出的类型
    /**
     * 在read里发生了异常可以在这里记录
     * @param throwable
     */
    @Override
    public void onSkipInRead(Throwable throwable) {

    }

    /**
     * 在writer中发生了异常可以在这里记录
     * @param s
     * @param throwable
     */
    @Override
    public void onSkipInWrite(String s, Throwable throwable) {

    }

    /**
     * 在processor中发生了异常可以在这里记录
     * @param s
     * @param throwable
     */
    @Override
    public void onSkipInProcess(String s, Throwable throwable) {
        System.out.println(s+ " occur exception "+throwable);
    }
}
