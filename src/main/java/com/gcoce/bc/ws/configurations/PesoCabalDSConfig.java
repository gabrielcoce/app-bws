package com.gcoce.bc.ws.configurations;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
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

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "pesoCabalEntityManagerFactory", transactionManagerRef = "pesoCabalTransactionManager", basePackages = {"com.gcoce.bc.ws.repositories.peso_cabal"})
public class PesoCabalDSConfig {
    @Autowired
    private Environment env;

    @Bean(name = "pesoCabalDataSource")
    public DataSource pesoCabalDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(env.getProperty("peso-cabal.datasource.url"));
        dataSource.setUsername(env.getProperty("peso-cabal.datasource.username"));
        dataSource.setPassword(env.getProperty("peso-cabal.datasource.password"));
        dataSource.setDriverClassName(env.getProperty("peso-cabal.datasource.driver-class-name"));
        return dataSource;
    }

    @Bean(name = "pesoCabalEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(pesoCabalDataSource());
        factoryBean.setPackagesToScan("com.gcoce.bc.ws.entities.peso_cabal");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        factoryBean.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("peso-cabal.jpa.hibernate.ddl-auto"));
        //properties.put("hibernate.show-sql", env.getProperty("peso-cabal.jpa.show-sql"));
        properties.put("hibernate.dialect", env.getProperty("peso-cabal.jpa.database-platform"));
        return factoryBean;
    }

    @Primary
    @Bean(name = "pesoCabalTransactionManager")
    public PlatformTransactionManager platformTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }
}
