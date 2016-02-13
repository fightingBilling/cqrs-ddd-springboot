package org.dsinczak.boot.cqrs.command.handler;

import java.util.Optional;

public interface CommandHandler<C, R> {

		Optional<R> handle(C command);

		boolean accept(Object command);
}
