package com.epam.esm;

import com.epam.esm.dao.TagDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.epam.esm")
public class Application {

    private final TagDao tagDao;

    @Autowired
    public Application(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
