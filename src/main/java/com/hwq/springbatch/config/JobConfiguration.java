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
@Configuration //相当于配置，可以创建对象
@EnableBatchProcessing //表示要执行批处理
public class JobConfiguration {
    //注入创建任务对象的对象
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    //任务的执行由step决定，一个任务可以包含若干个step
    //注入创建step对象的对象
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    //创建任务对象
    @Bean
    public Job helloWorldJob(){
        return jobBuilderFactory.get("helloWorldJob")//指定任务的名称
                                    .start(step())
                                    .build();
    }

    @Bean
    public Step step(){
        return stepBuilderFactory.get("step")//指定step的名称
                                    .tasklet(new Tasklet() {
                                        @Override
                                        public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                                            System.out.println("Hello World!");
                                            return RepeatStatus.FINISHED; //只有这个任务正常完成了才能执行后面的task
                                        }
                                    }) //具体执行的功能
                                    .build();

    }
}


