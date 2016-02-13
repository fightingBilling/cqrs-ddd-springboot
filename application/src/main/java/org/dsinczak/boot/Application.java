package org.dsinczak.boot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.Arrays;

@SpringBootApplication
@ComponentScan(basePackages = { "org.dsinczak.boot" })
@EnableAutoConfiguration
public class Application {

		private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

		public static void main(final String[] args) {
				ApplicationContext ctx = SpringApplication.run(Application.class, args);

				String[] beanNames = ctx.getBeanDefinitionNames();
				Arrays.sort(beanNames);
				for (String beanName : beanNames) {
						LOGGER.debug("Initialize bean {}", beanName);
				}
		}

}