package com.hwq.springbatch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
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
//由决策器选择step
public class DeciderDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    //创建step
    @Bean
    public Step deciderDemoStep1(){
        return stepBuilderFactory.get("deciderDemoStep1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("deciderDemoStep1");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Step deciderDemoStep2(){
        return stepBuilderFactory.get("deciderDemoStep2")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("even");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Step deciderDemoStep3(){
        return stepBuilderFactory.get("deciderDemoStep3")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("odd");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    //创建决策器
    @Bean
    public JobExecutionDecider myDecider(){
        return new MyDecider();
    }

    //创建任务
    @Bean
    public Job deciderDemoJob(){
        return jobBuilderFactory.get("deciderDemoJob")
                .start(deciderDemoStep1()) //首先执行step1
                .next(myDecider()) // 然后交由决策器处理
                .from(myDecider()).on("even").to(deciderDemoStep2()) //如果决策器返回even,则执行step2
                .from(myDecider()).on("odd").to(deciderDemoStep3()) //如果决策器返回odd,则执行step3
                .from(deciderDemoStep3()).on("*").to(myDecider()) //不管stpe3返回任何值，都交由决策器，有决策器决定执行那个step
                .end()
                .build();
    }

}
