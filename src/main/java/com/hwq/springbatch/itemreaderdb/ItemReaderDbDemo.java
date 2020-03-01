package com.hwq.springbatch.itemreaderdb;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by hwq on 2020/2/29.
 * 实现从数据库中读取数据
 */
@Configuration
@EnableBatchProcessing
public class ItemReaderDbDemo {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private DataSource dataSource;

    @Autowired
    @Qualifier("dbJdbcWriter")
    private ItemWriter <User> dbJdbcWriter;

    @Bean
    public Job ItemReaderDbJob(){
        return jobBuilderFactory.get("ItemReaderDbJob")
                .start(itemReaderDbStep())
                .build();
    }

    @Bean
    public Step itemReaderDbStep() {
        return stepBuilderFactory.get("itemReaderDbStep")
                .<User,User>chunk(2)
                .reader(dbJdbcReader())
                .writer(dbJdbcWriter)
                .build();
    }

    @Bean
    @StepScope
    public JdbcPagingItemReader<User> dbJdbcReader() {
        //从DB里都
        JdbcPagingItemReader <User> reader = new JdbcPagingItemReader<User>();
        reader.setDataSource(dataSource);
        reader.setFetchSize(2);
        //把数据库中读取出来的数据映射成对象
        reader.setRowMapper(new RowMapper(){
            @Override
            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
                User user = new User();
                user.setId(resultSet.getInt(1));
                user.setUsername(resultSet.getString(2));
                user.setPassword(resultSet.getString(3));
                user.setAge(resultSet.getInt(4));
                return user;
            }
        });
        //指定sql语句
        MySqlPagingQueryProvider provider = new MySqlPagingQueryProvider();
        provider.setSelectClause("id,username,password,age");
        provider.setFromClause("from user");
        //执行根据那个字段进行排序
        Map<String,Order> sort = new HashMap<String,Order>(1);
        sort.put("id",Order.ASCENDING);
        provider.setSortKeys(sort);

        reader.setQueryProvider(provider);

        return reader;
    }
}
