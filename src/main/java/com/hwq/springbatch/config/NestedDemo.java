package com.hwq.springbatch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.JobStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Created by hwq on 2020/2/29.
 * 嵌套job 一个父job可以嵌套多个子job,子job不能单独启动，只能由父job启动，
 * 需要在 application.properties文件中加上配置指定启动那个父job spring.batch.job.name=parentJob
 */
@Configuration
@EnableBatchProcessing //如果每个地方都要写这个注解，觉得麻烦的话可以在启动类SpringbatchAppliction上加，这样就不需要处处写了
public class NestedDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private Job childJobOne;  //这个是 ChildJob1中定义的
    @Autowired
    private Job childJobTwo; //这个是 ChildJob2中定义的
    @Autowired
    private JobLauncher jobLauncher;

    @Bean
    public Job parentJobs(JobRepository repository, PlatformTransactionManager transactionManager){
        return jobBuilderFactory.get("parentJobs")
                .start(childJob1(repository,transactionManager))
                .next(childJob2(repository,transactionManager))
                .build();
    }

    //这里返回job类型的step,特殊的step
    private Step childJob2(JobRepository repository, PlatformTransactionManager transactionManager) {
        //这里演示了如何将job类型转换成step类型
        return new JobStepBuilder(new StepBuilder("childJob2"))
                .job(childJobTwo)
                .launcher(jobLauncher) //使用父job的启动对象
                .repository(repository) //指定父类的持久化对象
                .transactionManager(transactionManager) //指定事务管理器
                .build();
    }

    //这里返回job类型的step,特殊的step
    private Step childJob1(JobRepository repository, PlatformTransactionManager transactionManager) {
        //这里演示了如何将job类型转换成step类型
        return new JobStepBuilder(new StepBuilder("childJob1"))
                .job(childJobOne)
                .launcher(jobLauncher) //使用父job的启动对象
                .repository(repository) //指定父类的持久化对象
                .transactionManager(transactionManager) //指定事务管理器
                .build();
    }




}
