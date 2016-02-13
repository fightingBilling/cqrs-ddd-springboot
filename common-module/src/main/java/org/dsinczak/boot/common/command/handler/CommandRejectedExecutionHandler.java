package org.dsinczak.boot.common.command.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class CommandRejectedExecutionHandler implements RejectedExecutionHandler {

		private static final Logger LOGGER = LoggerFactory.getLogger(CommandRejectedExecutionHandler.class);

		@Override
		public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
				LOGGER.error("Command {} has been rejected by executor {}.", r, executor);
				// TODO we can consider retrieving command from runnable and saving it se we can re-run it
		}
}
