package com.hwq.springbatch.itemprocessor;

import com.hwq.springbatch.itemreaderfile.Customer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * Created by hwq on 2020/3/1.
 */
@Component("firstNameUpperProcessor")
public class FirstNameUpperProcessor implements ItemProcessor<Customer,Customer> {
    @Override
    public Customer process(Customer customer) throws Exception {
        Customer cus = new Customer();
        cus.setId(customer.getId());
        cus.setLastName(customer.getLastName());
        cus.setBirthday(customer.getBirthday());
        cus.setFirstName(customer.getFirstName().toUpperCase());
        return cus;
    }
}
