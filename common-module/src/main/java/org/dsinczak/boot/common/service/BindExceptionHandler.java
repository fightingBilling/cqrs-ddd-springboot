package org.dsinczak.boot.common.service;

import org.dsinczak.boot.cqrs.annotation.CommandController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception handler for BindException. Exception handler is being attached to all services annotated with
 * {@link CommandController}. When exception is thrown then all the validation information is being mapped
 * to POJO class {@link CommandValidationError}.
 */
@ControllerAdvice(annotations = CommandController.class)
public class BindExceptionHandler {

		private static final Logger LOGGER = LoggerFactory.getLogger(BindExceptionHandler.class);

		@ResponseStatus(HttpStatus.BAD_REQUEST)
		@ExceptionHandler(BindException.class)
		@ResponseBody
		public CommandValidationError bindException(BindException bindException) {
				final CommandValidationError commandValidationError = new CommandValidationError();
				commandValidationError.setTest(bindException.toString());
				// TODO CommandValidationError should carry required and needed information
				LOGGER.debug("Handling bind exception {} for {}", bindException.toString(),
						commandValidationError.toString());
				return commandValidationError;
		}

}
