package com.hwq.springbatch.listener;

import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.scope.context.ChunkContext;

/**
 * Created by hwq on 2020/2/29.
 * chunk方式执行时使用的监控器，实现监听器起始可以使用实现接口的方式例如 implements ChunkListener,
 * 也可以使用注解的方式，这里我们使用注解试试
 */
public class MyChunkListener {
    @BeforeChunk
    public void beforeChunk(ChunkContext context){
        System.out.println(context.getStepContext().getStepName()+"before...");
    }

    @AfterChunk
    public void afterChunk(ChunkContext context){
        System.out.println(context.getStepContext().getStepName()+"after...");
    }
}
