package br.com.selat.appsample.config;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Optional;

@Configuration
@EnableTransactionManagement
public class DatasourceConfig {

    @Bean
    public DataSource dataSource(Environment e) {
        PoolProperties p = new PoolProperties();
        p.setDriverClassName("com.mysql.cj.jdbc.Driver");
        p.setUrl(Optional.ofNullable(System.getenv("DATABASE_URL")).orElse(e.getProperty("databaseUrl")));
        p.setUsername(Optional.ofNullable(System.getenv("DATABASE_USER")).orElse(e.getProperty("databaseUser")));
        p.setPassword(Optional.ofNullable(System.getenv("DATABASE_PASSWORD")).orElse(e.getProperty("databasePassword")));
        p.setJmxEnabled(true);
        p.setTestWhileIdle(false);
        p.setTestOnBorrow(true);
        p.setValidationQuery("SELECT 1");
        p.setTestOnReturn(false);
        p.setValidationInterval(30000);
        p.setTimeBetweenEvictionRunsMillis(30000);
        p.setMaxActive(100);
        p.setInitialSize(10);
        p.setMaxWait(10000);
        p.setRemoveAbandonedTimeout(60);
        p.setMinEvictableIdleTimeMillis(30000);
        p.setMinIdle(10);
        p.setLogAbandoned(true);
        p.setRemoveAbandoned(true);
        p.setJdbcInterceptors(
                "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;" +
                        "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
        p.setDefaultAutoCommit(true);
        DataSource datasource = new DataSource();
        datasource.setPoolProperties(p);

        return datasource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(Environment e) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource(e));

        return transactionManager;
    }


}
