package com.hwq.springbatch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

/**
 * Created by hwq on 2020/2/29.
 */
@Configuration
@EnableBatchProcessing
public class SplitDemo { //让job中的flow并行执行
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step splitDemoStep1(){
        return stepBuilderFactory.get("splitDemoStep1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("splitDemoStep1");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Step splitDemoStep2(){
        return stepBuilderFactory.get("splitDemoStep2")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("splitDemoStep2");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Step splitDemoStep3(){
        return stepBuilderFactory.get("splitDemoStep3")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("splitDemoStep3");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Flow splitDemoFlow1(){
        return new FlowBuilder<Flow>("splitDemoFlow1")
                .start(splitDemoStep1())
                .build();
    }

    @Bean
    public Flow splitDemoFlow2(){
        return new FlowBuilder<Flow>("splitDemoFlow2")
                .start(splitDemoStep2())
                .next(splitDemoStep3())
                .build();
    }

    @Bean
    public Job splitDemoJob(){
        return jobBuilderFactory.get("splitDemoJob")
                .start(splitDemoFlow1())
                .split(new SimpleAsyncTaskExecutor()) //表示异步执行,让flow1和flow2在各自的线程里执行
                .add(splitDemoFlow2())
                .end()
                .build();
    }
}
