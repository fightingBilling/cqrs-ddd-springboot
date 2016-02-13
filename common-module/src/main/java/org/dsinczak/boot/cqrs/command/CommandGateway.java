package org.dsinczak.boot.cqrs.command;

import org.springframework.validation.BindException;

import java.util.Optional;

/**
 * Main command dispatching interface.
 */
public interface CommandGateway {

		<T> Optional<T> dispatch(Object command) throws BindException;

}
