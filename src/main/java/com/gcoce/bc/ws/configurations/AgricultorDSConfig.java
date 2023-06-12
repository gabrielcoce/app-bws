package com.gcoce.bc.ws.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "agricultorEntityManagerFactory", transactionManagerRef = "agricultorTransactionManager", basePackages = {"com.gcoce.bc.ws.repositories.agricultor"})
public class AgricultorDSConfig {
    @Autowired
    private Environment env;

    @Bean(name = "agricultorDataSource")
    public DataSource agricultorDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(env.getProperty("agricultor.datasource.url"));
        dataSource.setUsername(env.getProperty("agricultor.datasource.username"));
        dataSource.setPassword(env.getProperty("agricultor.datasource.password"));
        dataSource.setDriverClassName(Objects.requireNonNull(env.getProperty("agricultor.datasource.driver-class-name")));
        return dataSource;
    }

    @Bean(name = "agricultorEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(agricultorDataSource());
        factoryBean.setPackagesToScan("com.gcoce.bc.ws.entities.agricultor");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        factoryBean.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("agricultor.jpa.hibernate.ddl-auto"));
        //properties.put("hibernate.show-sql", env.getProperty("agricultor.jpa.show-sql"));
        properties.put("hibernate.dialect", env.getProperty("agricultor.jpa.database-platform"));
        return factoryBean;
    }

    @Primary
    @Bean(name = "agricultorTransactionManager")
    public PlatformTransactionManager platformTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }
}
