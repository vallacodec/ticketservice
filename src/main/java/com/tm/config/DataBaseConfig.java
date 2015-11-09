package com.tm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;


/**
 * Created by svallaban1 on 11/5/2015.
 */
@Configuration
public class DataBaseConfig {

    @Bean
    public DataSource dataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        EmbeddedDatabase db = builder
                .setType(EmbeddedDatabaseType.H2)
                .addScript("db/sql/create-db.sql")
                .addScript("db/sql/insert-level.sql")
                .addScript("db/sql/insert-seat-balcony1.sql")
                .addScript("db/sql/insert-seat-balcony2.sql")
                .addScript("db/sql/insert-seat-main.sql")
                .addScript("db/sql/insert-seat-orchestra.sql")
                .build();
        return db;
    }
}

