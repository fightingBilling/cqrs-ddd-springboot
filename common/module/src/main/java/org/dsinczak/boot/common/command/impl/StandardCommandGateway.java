package org.dsinczak.boot.common.command.impl;

import javaslang.control.Try;
import org.dsinczak.boot.common.cqrs.annotation.Command;
import org.dsinczak.boot.common.cqrs.command.CommandCallback;
import org.dsinczak.boot.common.cqrs.command.CommandGateway;
import org.dsinczak.boot.common.cqrs.command.handler.CommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static java.text.MessageFormat.format;

/**
 * Implementation of {@link CommandGateway} for single server application instance. Gateways dispatches commands
 * depending on {@link Command#asynchronous()} flag.
 */
@Component
public class StandardCommandGateway implements CommandGateway {

	private static final Logger LOGGER = LoggerFactory.getLogger(StandardCommandGateway.class);

	private static final String MSG_ILLEGAL_COMMAND = "Object {0} passed to handle {1} is null or not annotated with {2}.";

	private final StandardCommandHandlerContainer commandBus;

	private final ThreadPoolTaskExecutor executor;

	@Autowired
	public StandardCommandGateway(final StandardCommandHandlerContainer commandBus,
								  @Qualifier("commandThreadPoolExecutor")
								  final ThreadPoolTaskExecutor executor) {
		super();
		this.commandBus = commandBus;
		this.executor = executor;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <R> Optional<R> dispatch(final Object command) {
		checkCommand(command);
		CommandHandler commandHandler = commandBus.getHandler(command).get();

		LOGGER.debug("Dispatching command: {}", command);

		if (isAsynchronous(command)) {
			LOGGER.debug("Dispatching command asynchronously: {}", command);
			executor.execute(new CommandTask(commandHandler, command));
			return Optional.empty();
		}

		LOGGER.debug("Dispatching command synchronously: {}", command);
		return commandHandler.handle(command);

	}

	@Override
	public void dispatch(final Object command, final CommandCallback<?> callback) {
		checkCommand(command);
		CommandHandler commandHandler = commandBus.getHandler(command).get();
		LOGGER.debug("Dispatching command: {} with callback: {}", command, callback);

		// TODO implement asynchronous
		Try.of(() -> commandHandler.handle(command))
				.onSuccess(optional -> callback.onSuccess(optional))
				.onFailure(throwable -> callback.onError((Throwable) throwable));

	}

	private void checkCommand(final Object command) {
		Optional.ofNullable(command).filter(c -> c.getClass() //
				.isAnnotationPresent(Command.class)) //
				.orElseThrow(() -> new IllegalArgumentException(
						format(MSG_ILLEGAL_COMMAND,
								command,
								String.valueOf(command),
								Command.class.getCanonicalName())));

	}

	private boolean isAsynchronous(final Object command) {
		final Command annotation = command.getClass().getAnnotation(Command.class);
		return annotation.asynchronous();
	}

}
