package ru.miniprog.minicrm;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
public class MinicrmApplication {

    public static void main(String[] args) {
        SpringApplication.run(MinicrmApplication.class, args);
    }

}
