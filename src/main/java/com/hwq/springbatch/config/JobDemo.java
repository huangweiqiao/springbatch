package com.hwq.springbatch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
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
public class JobDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job jobDemoJob(){
        return jobBuilderFactory.get("jobDemoJob")
//                .start(step1())
//                .next(step2())
//                .next(step3())
                //上面是按写死的步骤，下面这种写法是有条件的执行
                .start(step1()) //从step1开始执行
                .on("COMPLETED").to(step2()) //当step1成功的结束了再执行step2
                .from(step2()).on("COMPLETED").to(step3())//当step2成功结束再执行step3
//                .from(step2()).on("COMPLETED").fail() //这是要在step2执行完之后当失败处理，不要继续执行了
//                .from(step2()).on("COMPLETED").stopAndRestart(flow) // 这是要在step2执行完之后停止且重新开始
                .from(step3()).end() //step3执行完就结束
                .build();
    }

    @Bean
    public Step step1(){
        return stepBuilderFactory.get("step1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("step1");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Step step2(){
        return stepBuilderFactory.get("step2").tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                System.out.println("step2");
                return RepeatStatus.FINISHED;
            }
        }).build();
    }

    @Bean
    public Step step3(){
        return stepBuilderFactory.get("step3").tasklet(new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                System.out.println("step3");
                return RepeatStatus.FINISHED;
            }
        }).build();
    }
}
