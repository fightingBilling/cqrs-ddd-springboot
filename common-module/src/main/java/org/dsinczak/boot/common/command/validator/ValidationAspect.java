package org.dsinczak.boot.common.command.validator;

import org.dsinczak.boot.cqrs.annotation.CommandValidator;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.dsinczak.boot.cqrs.command.CommandGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Aspect cuts in between caller and call to method {@link CommandGateway#dispatch(Object)}
 * adding validation cross cutting concern. First aspect validates with standard {@link Validator} allowing to check
 * fields validations like {@link javax.validation.constraints.NotNull}. Then as second step if validates command with
 * every class extending {@link Validator} and annotated with {@link CommandValidator}. Each validator result (if any)
 * is being accumulated in {@link BindException}. When there are any errors then BindException is being thrown.
 */
@Aspect
public class ValidationAspect {

		private static final Logger LOGGER = LoggerFactory.getLogger(ValidationAspect.class);

		private final Validator constraintValidator;
		private final List<Validator> customValidators;

		public ValidationAspect(Validator constraintValidator, List<Validator> customValidators) {
				this.constraintValidator = constraintValidator;
				this.customValidators = customValidators;
		}

		/**
		 * Validation method. <br/>
		 * TODO Add constraint to do not add validation when it is called from custom {@link Validator} implementations.
		 *
		 * @param command Command to be validated
		 * @throws BindException Accumulated error
		 */
		@Before("execution(* org.dsinczak.boot.cqrs.command.CommandGateway+.dispatch(Object)) && args(command)")
		public void validateCommandDispatch(Object command) throws BindException {
				LOGGER.debug("Command validation {}", command);
				BindException errors = new BindException(command, command.getClass().getSimpleName());
				constraintValidator.validate(command, errors);
				if (!errors.hasErrors()) {
						// do not start custom validation if basic is failing
						customValidators.stream().filter(validator -> validator.supports(command.getClass()))
								.forEach(validator -> validator.validate(command, errors));
				}
				if (errors.hasErrors()) {
						LOGGER.warn("Command {} has has validation errors {}", command,
								errors.getAllErrors().stream().map(objectError -> objectError.toString())
										.collect(Collectors.joining(", ")));
						throw errors;
				}
		}

}
