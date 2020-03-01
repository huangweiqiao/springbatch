package com.hwq.springbatch.config;

import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * Created by hwq on 2020/2/29.
 * 利用监听器传递参数,这个例子感觉没什么意义
 */
@Configuration
@EnableBatchProcessing
public class ParametersDemo implements StepExecutionListener{
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    private Map<String,JobParameter> parameters;

    @Bean
    public Job parameterJob(){
        return jobBuilderFactory.get("parameterJob")
                .start(parameterStep())
                .build();
    }

    /*
    实际上job执行的是step，job使用的数据肯定是在step中使用，
    这样我们只需要给step传递数据，如何给step传递参数呢?
    可以使用step级别的监听来传递，这里为了方便就将该类实现 StepExecutionListener
     */
    @Bean
    public Step parameterStep() {
        return stepBuilderFactory.get("parameterStep")
                .listener(this)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        //输出接受到的参数
                        System.out.println(parameters.get("info"));
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }



    @Override
    public void beforeStep(StepExecution stepExecution) {
        //这里在step执行前就将job的参数赋值了，到时再step中就可以取值了
        parameters = stepExecution.getJobParameters().getParameters();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }

}
