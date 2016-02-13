package org.dsinczak.boot.common.command.impl;

import org.dsinczak.boot.cqrs.command.handler.CommandHandler;

/**
 * Command execution task for command set to be executed asynchronously.
 */
public class CommandTask implements Runnable {

		private CommandHandler commandHandler;
		private Object command;

		public CommandTask(CommandHandler commandHandler, Object command) {
				this.commandHandler = commandHandler;
				this.command = command;
		}

		@Override
		public void run() {
				commandHandler.handle(command);
		}

		@Override
		public String toString() {
				return String.valueOf(command);
		}
}
