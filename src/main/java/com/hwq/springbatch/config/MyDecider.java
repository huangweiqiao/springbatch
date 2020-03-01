package com.hwq.springbatch.config;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.lang.Nullable;

/**
 * Created by hwq on 2020/2/29.
 */
//决策器
public class MyDecider implements JobExecutionDecider {

    private int count;

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, @Nullable StepExecution stepExecution) {
        count++;
        if (count%2==0)
            return new FlowExecutionStatus("even");
        else
            return new FlowExecutionStatus("odd");
    }
}
