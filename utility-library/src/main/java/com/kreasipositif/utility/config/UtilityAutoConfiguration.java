package com.kreasipositif.utility.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class to enable component scanning for utility library
 */
@Configuration
@ComponentScan(basePackages = "com.kreasipositif.utility")
public class UtilityAutoConfiguration {
}
