package ru.panfio.telescreen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import ru.panfio.telescreen.config.SimpleFilter;

import java.util.stream.Stream;

//CHECKSTYLE:OFF
@SpringBootApplication
@EnableZuulProxy
public class TelescreenServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TelescreenServerApplication.class, args);
    }

    @Bean
    public SimpleFilter simpleFilter() {
        return new SimpleFilter();
    }
//CHECKSTYLE:ON
}
