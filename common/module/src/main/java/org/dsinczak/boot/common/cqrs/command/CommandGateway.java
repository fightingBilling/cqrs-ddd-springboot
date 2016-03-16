package org.dsinczak.boot.common.cqrs.command;

import java.util.Optional;

/**
 * Main command dispatching interface.
 */
public interface CommandGateway {

	/**
	 * Method dispatches command do command handler.
	 *
	 * @param command command a.k.a caller intent
	 * @param <R>     return type
	 * @return handler return value
	 */
	<R> Optional<R> dispatch(Object command);

	/**
	 * Method dispatches command do command handler and returns result to callback object.
	 *
	 * @param command  command a.k.a caller intent
	 * @param callback callback object.
	 */
	void dispatch(Object command, CommandCallback<?> callback);

}
