package org.dsinczak.boot.common.cqrs.command.handler;

import java.util.Optional;

public interface CommandHandler<C, R> {

	Optional<R> handle(C command);

	/**
	 * TODO implement accept as annotation
	 */
	boolean accept(Object command);
}
