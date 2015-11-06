package com.tm.platform;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.util.SocketUtils;

@SpringBootApplication
@ComponentScan("com.tm")
public class SampleJerseyApplication extends SpringBootServletInitializer {

    @Bean
    public int port() {
        return SocketUtils.findAvailableTcpPort();
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SampleJerseyApplication.class);
    }

    public static void main(String[] args) {
        new SampleJerseyApplication().configure(
                new SpringApplicationBuilder(SampleJerseyApplication.class)).run(args);
    }

}
