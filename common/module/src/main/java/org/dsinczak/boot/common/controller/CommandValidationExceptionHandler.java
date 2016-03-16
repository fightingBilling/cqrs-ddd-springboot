package org.dsinczak.boot.common.controller;

import org.dsinczak.boot.common.cqrs.annotation.CommandController;
import org.dsinczak.boot.sharedkernel.exception.CommandValidationException;
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
public class CommandValidationExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommandValidationExceptionHandler.class);

		@ResponseStatus(HttpStatus.BAD_REQUEST)
		@ExceptionHandler(BindException.class)
		@ResponseBody
		public CommandValidationError bindException(CommandValidationException validationException) {
				final CommandValidationError commandValidationError = new CommandValidationError();
			commandValidationError.setTest(validationException.getMessage());
				// TODO CommandValidationError should carry required and needed information
			LOGGER.debug("Handling bind exception {} for {}", validationException.toString(),
						commandValidationError.toString());
				return commandValidationError;
		}

}
