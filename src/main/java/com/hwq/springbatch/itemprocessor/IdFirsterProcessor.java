package com.hwq.springbatch.itemprocessor;

import com.hwq.springbatch.itemreaderfile.Customer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * Created by hwq on 2020/3/1.
 */
@Component("idFirsterProcessor")
public class IdFirsterProcessor implements ItemProcessor<Customer,Customer> {
    @Override
    public Customer process(Customer customer) throws Exception {
        if (customer.getId()%2==0)
            return customer;
        else
            return null;  //返回为空表示过滤掉了
    }
}
