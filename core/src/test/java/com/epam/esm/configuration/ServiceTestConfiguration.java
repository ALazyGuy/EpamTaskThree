package com.epam.esm.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(DaoTestConfiguration.class)
public class ServiceTestConfiguration {
}
