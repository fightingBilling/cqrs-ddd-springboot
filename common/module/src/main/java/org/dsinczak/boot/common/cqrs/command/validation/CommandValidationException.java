package org.dsinczak.boot.common.cqrs.command.validation;

/**
 * TODO: implement proper error container
 */
public class CommandValidationException extends RuntimeException {


	public CommandValidationException(String message) {
		super(message);
	}
}
