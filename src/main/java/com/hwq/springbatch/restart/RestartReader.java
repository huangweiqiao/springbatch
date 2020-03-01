package com.hwq.springbatch.restart;

import com.hwq.springbatch.itemreaderfile.Customer;
import org.springframework.batch.item.*;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

/**
 * Created by hwq on 2020/3/1.
 * 批次任务执行时出现异常，想在下次执行的时候接着失败时的数据进行执行，这时reader可以实现  那么实现ItemStreamReader,
 * 如果想处理异常的信息，那么实现ItemStreamReader ,
 */
@Component("restartReader")
public class RestartReader implements ItemStreamReader<Customer>{
    private FlatFileItemReader <Customer> customerFlatFileItemReader = new FlatFileItemReader();
    private Long curLine=0L;
    private boolean restart = false;
    private ExecutionContext executionContext;

    public RestartReader(){
        customerFlatFileItemReader.setResource(new ClassPathResource("restart.txt"));
        //解析数据
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        //指定表头
        tokenizer.setNames(new String[]{"id","firstName","lastName","birthday"});
        //把解析出来的一行数据映射从Customer对象
        DefaultLineMapper<Customer> mapper = new DefaultLineMapper<>();
        mapper.setLineTokenizer(tokenizer);
        mapper.setFieldSetMapper(new FieldSetMapper<Customer>() {
            @Override
            public Customer mapFieldSet(FieldSet fieldSet) throws BindException {
                Customer customer = new Customer();
                customer.setId(fieldSet.readLong("id"));
                customer.setFirstName(fieldSet.readString("firstName"));
                customer.setLastName(fieldSet.readString("lastName"));
                customer.setBirthday(fieldSet.readString("birthday"));
                return customer;
            }
        });
        //做一下检查
        mapper.afterPropertiesSet();
        customerFlatFileItemReader.setLineMapper(mapper);
    }

    @Override
    public Customer read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        Customer customer=null;
        this.curLine++;
        if (restart){
            customerFlatFileItemReader.setLinesToSkip(this.curLine.intValue()-1);
            restart = false;
            System.out.println("Start reading from line:"+this.curLine);
        }
        customerFlatFileItemReader.open(this.executionContext);
        customer = customerFlatFileItemReader.read();
        if (customer!=null && customer.getFirstName().equals("Li")){
            throw new RuntimeException("error,customer.id="+customer.getId());
        }
        return customer;
    }

    //step执行前执行
    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        this.executionContext = executionContext;
        if (executionContext.containsKey("curLine")){
            this.curLine = executionContext.getLong("curLine");
            this.restart = true;
        }else{
            this.curLine = 0L;
            executionContext.put("curLine",this.curLine);
            System.out.println("Start reading from line:"+this.curLine+1);
        }
    }

    //每一批chunk数据执行成功后会执行
    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
            executionContext.put("curLine",this.curLine);
            System.out.println("curLine="+this.curLine);
    }

    @Override
    public void close() throws ItemStreamException {

    }
}
