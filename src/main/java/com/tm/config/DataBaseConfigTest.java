package com.tm.config;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;


import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class DataBaseConfigTest {

    private EmbeddedDatabase db;
    JdbcTemplate jdbcTemplate;





    @Before
    public void setUp() {
        //db = new EmbeddedDatabaseBuilder().addDefaultScripts().build();
        db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("db/sql/create-db.sql")
                .addScript("db/sql/insert-data.sql")
                .build();
    }

    @Test
    public void testFindByname() {
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(db);

        Map<String,String> paramMap = new HashMap<String, String>();




    }

    @After
    public void tearDown() {
        db.shutdown();
    }

    class User {

        private Long id;

        private String name;

        private String emailId;

        User(Long id, String name, String emailId) {

            this.id = id;
            this.name = name;
            this.emailId = emailId;

        }


    }

}

