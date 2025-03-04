package com.example.weatherApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/weatherApp");
        dataSource.setUsername("postgres");
        dataSource.setPassword("123123");
        dataSource.setDriverClassName("org.postgresql.Driver");
        return dataSource;
    }
}
