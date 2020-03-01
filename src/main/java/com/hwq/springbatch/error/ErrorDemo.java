package com.hwq.springbatch.error;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * Created by hwq on 2020/3/1.
 * 默认请情况下，任务出现异常时 springBatch会将任务停止
 */
@Configuration
@EnableBatchProcessing
public class ErrorDemo {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job errorDemoJob(){
        return jobBuilderFactory.get("errorDemoJob")
                .start(errorDemoStep1())
                .next(errorDemoStep2())
                .build();
    }

    @Bean
    public Step errorDemoStep1() {
        return stepBuilderFactory.get("errorDemoStep1")
                .tasklet(errorHandling())
                .build();
    }

    @Bean
    public Step errorDemoStep2() {
        return stepBuilderFactory.get("errorDemoStep2")
                .tasklet(errorHandling())
                .build();
}
    @Bean
    @StepScope
    public Tasklet errorHandling() {
        return new Tasklet(){
            @Override
            public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                Map<String,Object> stepExecutionContext = chunkContext.getStepContext().getStepExecutionContext();
                if (stepExecutionContext.containsKey("hwq")){
                    System.out.println("The second run will success");
                    return RepeatStatus.FINISHED;
                }else{
                    System.out.println("The first run will fail");
                    chunkContext.getStepContext().getStepExecution().getExecutionContext().put("hwq","first");
                    throw new RuntimeException(chunkContext.getStepContext().getStepName()+"出错了，请再次执行!");
                }
            }
        };
    }
}
