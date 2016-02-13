package org.dsinczak.boot.common.command.impl;

import org.dsinczak.boot.cqrs.annotation.Command;
import org.dsinczak.boot.cqrs.command.CommandGateway;
import org.dsinczak.boot.cqrs.command.handler.CommandHandler;
import org.dsinczak.boot.common.command.handler.SimpleCommandHandlerContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

import java.util.Optional;

import static java.text.MessageFormat.format;

/**
 * Implementation of {@link CommandGateway} for single server application instance. Gateways dispatched commands
 * depending on {@link Command#asynchronous()} flag.
 */
@Component
public class StandardCommandGateway implements CommandGateway {

		private static final Logger LOGGER = LoggerFactory.getLogger(StandardCommandGateway.class);

		private static final String MSG_ILLEGAL_COMMAND = "Object passed to handle {0} is null or not annotated with {1}.";

		private final SimpleCommandHandlerContainer commandHandlerContainer;

		private final ThreadPoolTaskExecutor executor;

		@Autowired
		public StandardCommandGateway(final SimpleCommandHandlerContainer commandHandlerContainer,
				@Qualifier("commandThreadPoolExecutor")
				final ThreadPoolTaskExecutor executor) {
				super();
				this.commandHandlerContainer = commandHandlerContainer;
				this.executor = executor;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> Optional<T> dispatch(final Object command) throws BindException {
				checkCommand(command);

				CommandHandler commandHandler = commandHandlerContainer.getHandler(command).get();

				if (isAsynchronous(command)) {
						LOGGER.debug("Dispatching command asynchronously: {}", command);
						executor.execute(new CommandTask(commandHandler, command));
						return Optional.empty();
				}

				LOGGER.debug("Dispatching command synchronously: {}", command);
				return commandHandler.handle(command);
		}

		private void checkCommand(final Object command) {
				final Optional<Object> cmd = Optional.ofNullable(command);
				cmd.filter(c -> c.getClass().isAnnotationPresent(Command.class)) //
						.orElseThrow(() -> new IllegalArgumentException(
								format(MSG_ILLEGAL_COMMAND, String.valueOf(command),
										Command.class.getCanonicalName())));

		}

		private boolean isAsynchronous(final Object command) {
				final Command annotation = command.getClass().getAnnotation(Command.class);
				return annotation.asynchronous();
		}

}
