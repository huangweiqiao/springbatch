package com.hwq.springbatch.itemwriterfile;

import com.hwq.springbatch.itemreaderfile.Customer;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hwq on 2020/3/1.
 */
@Configuration
public class DbJdbcReaderConfig {
    @Autowired
    private DataSource dataSource;
    
    @Bean
    public JdbcPagingItemReader<Customer> dbJdbcReader() {
        //从DB里都
        JdbcPagingItemReader <Customer> reader = new JdbcPagingItemReader<Customer>();
        reader.setDataSource(dataSource);
        reader.setFetchSize(2);
        //把数据库中读取出来的数据映射成对象
        reader.setRowMapper(new RowMapper(){
            @Override
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                Customer customer = new Customer();
                customer.setId(resultSet.getLong(1));
                customer.setFirstName(resultSet.getString(2));
                customer.setLastName(resultSet.getString(3));
                customer.setBirthday(resultSet.getString(4));
                return customer;
            }
        });
        //指定sql语句
        MySqlPagingQueryProvider provider = new MySqlPagingQueryProvider();
        provider.setSelectClause("id,firstname,lastname,birthday");
        provider.setFromClause("from CUSTOMER");
        //执行根据那个字段进行排序
        Map<String,Order> sort = new HashMap<String,Order>(1);
        sort.put("id",Order.ASCENDING);
        provider.setSortKeys(sort);

        reader.setQueryProvider(provider);

        return reader;
    }
}
