package com.hwq.springbatch.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

/**
 * Created by hwq on 2020/2/29.
 * job的监听器,用实现接口的方式
 */
public class MyJobListener implements JobExecutionListener{
    /**
     * job执行前执行的方法
     * @param jobExecution
     */
    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println(jobExecution.getJobInstance().getJobName()+"before...");
    }

    /**
     * job执行后执行的方法
     * @param jobExecution
     */
    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println(jobExecution.getJobInstance().getJobName()+"after...");
    }

}
