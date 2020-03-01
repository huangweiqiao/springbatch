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

/**
 * Created by hwq on 2020/2/29.
 */
@Configuration
@EnableBatchProcessing
public class FlowDemo {
    //Flow 是多个 step的集合
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step flowDemoStep1(){
        return stepBuilderFactory.get("flowDemoStep1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("flowDemoStep1");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Step flowDemoStep2(){
        return stepBuilderFactory.get("flowDemoStep2")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("flowDemoStep2");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Step flowDemoStep3(){
        return stepBuilderFactory.get("flowDemoStep3")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("flowDemoStep3");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    //创建flow对象，指明flow对象包含哪些step
    @Bean
    public Flow flowDemoFlow(){
        return new FlowBuilder<Flow>("flowDemoFlow") //指定flow的名称
                .start(flowDemoStep1())
                .next(flowDemoStep2())
                .build();
    }

    //创建job对象
    @Bean
    public Job flowDemoJob(){
        return jobBuilderFactory.get("flowDemoJob")
                .start(flowDemoFlow()) //指定flow
                .next(flowDemoStep3()) //flowDemoStep3不包含在flow里所有还可以指定step3
                .end()
                .build();
    }

}
