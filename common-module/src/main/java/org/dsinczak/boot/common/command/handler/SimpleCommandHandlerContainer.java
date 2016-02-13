package org.dsinczak.boot.common.command.handler;

import org.dsinczak.boot.cqrs.command.handler.CommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.text.MessageFormat.format;

@Component
public class SimpleCommandHandlerContainer implements CommandHandlerContainer {

		private static final Logger LOGGER = LoggerFactory.getLogger(SimpleCommandHandlerContainer.class);

		private static final String MSG_NO_HANDLER = "No handler defined for command {0}.";
		private static final String MSG_MULTIPLE_HANDLER = "Multiple handler defined for command {0}.";

		private final Set<CommandHandler> handlers;

		public SimpleCommandHandlerContainer() {
				handlers = new HashSet<>();
		}

		@Autowired
		public SimpleCommandHandlerContainer(final Collection<CommandHandler> handlers) {
				this.handlers = new HashSet<>(handlers);
		}

		@Override
		public Optional<CommandHandler> getHandler(final Object command) {
				LOGGER.debug("Handling command {}", command);
				checkHandlers(command);

				return handlers.stream().filter(h -> h.accept(command)).findFirst();
		}

		private void checkHandlers(final Object command) {
				final long handlersCount = handlers.stream().filter(h -> h.accept(command)).count();
				if (handlersCount < 1L) {
						throw new IllegalStateException(format(MSG_NO_HANDLER, command));
				} else {
						if (handlersCount > 1L) {
								throw new IllegalStateException(format(MSG_MULTIPLE_HANDLER, command));
						}
				}
		}

}
