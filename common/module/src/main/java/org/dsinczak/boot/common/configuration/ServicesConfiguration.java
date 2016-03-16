package org.dsinczak.boot.common.configuration;

import org.dsinczak.boot.common.cqrs.annotation.CommandValidator;
import org.dsinczak.boot.common.command.validator.ValidationAspect;
import org.dsinczak.boot.common.command.handler.CommandRejectedExecutionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Common beans configuration. No business logic beans here, only application logic.
 */
@Configuration
public class ServicesConfiguration {

		public static final String THREAD_GROUP = "commandExecutionThreadGroup";
		public static final String THREAD_PREFIX = "commandExecution";

		@Autowired
		private ApplicationContext applicationContext;

		@Bean
		Validator commandConstraintValidator() {
				LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
				return validator;
		}

		@Bean
		List<Validator> commandCustomValidator() {
				return applicationContext.getBeansWithAnnotation(CommandValidator.class) //
						.values() //
						.stream() //
						.filter(o -> o instanceof Validator) //
						.map(o -> ((Validator) o)) //
						.collect(Collectors.toList());
		}

		@Bean
		ValidationAspect commandValidationAspect() {
				return new ValidationAspect(commandConstraintValidator(), commandCustomValidator());
		}

		@Bean
		CommandRejectedExecutionHandler commandRejectedExecutionHandler() {
				return new CommandRejectedExecutionHandler();
		}

		@Bean
		ThreadPoolTaskExecutor commandThreadPoolExecutor() {
				ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
				executor.setQueueCapacity(15);
				executor.setCorePoolSize(5);
				executor.setMaxPoolSize(10);
				executor.setWaitForTasksToCompleteOnShutdown(true);
				executor.setThreadGroupName(THREAD_GROUP);
				executor.setThreadNamePrefix(THREAD_PREFIX);
				executor.setRejectedExecutionHandler(commandRejectedExecutionHandler());
				executor.initialize();
				return executor;
		}
}
