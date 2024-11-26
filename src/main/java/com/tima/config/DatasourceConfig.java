package com.tima.config;

import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DatasourceConfig {
    @Value("${SPRING.DATASOURCE.DRIVERCLASSNAME}")
    private String datasourceDriverClassName;
    @Value("${SPRING.DATASOURCE.URL}")
    private String datasourceUrl;
    @Value("${SPRING.DATASOURCE.USERNAME}")
    private String datasourceUsername;
    @Value("${SPRING.DATASOURCE.PASSWORD}")
    private String datasourcePassword;
    @Value("${SPRING.DATASOURCE.MAXIMUM-POOL-SIZE}")
    private int maxPoolSize;

    @Bean
    @Primary
    public DataSource datasource() {
        final HikariDataSource ds = new HikariDataSource();
        ds.setDriverClassName(datasourceDriverClassName);
        ds.setJdbcUrl(datasourceUrl);
        ds.setUsername(datasourceUsername);
        ds.setPassword(datasourcePassword);
        ds.setMaximumPoolSize(maxPoolSize);

        return ds;
    }

    @Bean(initMethod = "migrate")
    @Autowired
    public Flyway flyway(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .baselineOnMigrate(true)
                .validateOnMigrate(false)
                .outOfOrder(true)
                .table("schema_version")
                .load();
    }
}
