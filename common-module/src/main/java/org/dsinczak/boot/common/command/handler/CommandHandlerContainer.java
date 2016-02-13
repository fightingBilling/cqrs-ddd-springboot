package org.dsinczak.boot.common.command.handler;

import org.dsinczak.boot.cqrs.command.handler.CommandHandler;

import java.util.Optional;

public interface CommandHandlerContainer {

		Optional<CommandHandler> getHandler(final Object command);

}
