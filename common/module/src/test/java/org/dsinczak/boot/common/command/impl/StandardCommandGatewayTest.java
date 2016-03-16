package org.dsinczak.boot.common.command.impl;

import org.dsinczak.boot.common.cqrs.annotation.Command;
import org.dsinczak.boot.common.cqrs.command.CommandCallback;
import org.dsinczak.boot.common.cqrs.command.handler.CommandHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class StandardCommandGatewayTest {

	private StandardCommandGateway classUnderTest;

	private StandardCommandHandlerContainer commandHandlerContainer;

	@Mock
	private ThreadPoolTaskExecutor executor;

	@Before
	public void setUp() {
		commandHandlerContainer = new StandardCommandHandlerContainer(
				newArrayList(new SyncCommandHandler(), new AsyncCommandHandler(), new FailingSyncCommandHandler()));
		classUnderTest = new StandardCommandGateway(commandHandlerContainer, executor);
	}

	@Test
	public void shouldReturnNonEmptyStringOnSynchronousCall() {
		// Given
		final CommandSync cmd = new CommandSync();

		// When
		final Optional<String> dispatch = classUnderTest.dispatch(cmd);

		// Then
		assertEquals("Result of SyncCommandHandler", dispatch.get());
	}

	@Test
	public void shouldReturnEmptyStringOnAsynchronousCall() {
		// Given
		final CommandAsync cmd = new CommandAsync();

		// When
		final Optional<String> result = classUnderTest.dispatch(cmd);

		// Then
		assertFalse(result.isPresent());
	}

	@Test
	public void shouldCallSuccessCallbackMethodForAsynchronousCommand() {
		// Given
		final CommandAsync cmd = new CommandAsync();
		final TestCallback callback = new TestCallback();

		// When
		classUnderTest.dispatch(cmd, callback);

		// Then
		assertTrue(callback.success);
	}

	@Test
	public void shouldCallSuccessCallbackMethodForSynchronousCommand() {
		// Given
		final CommandSync cmd = new CommandSync();
		final TestCallback callback = new TestCallback();

		// When
		classUnderTest.dispatch(cmd, callback);

		// Then
		assertTrue(callback.success);
	}

	@Test
	public void shouldCallErrorCallbackMethodForSynchronousCommand() {
		// Given
		final FailingCommandSync cmd = new FailingCommandSync();
		final TestCallback callback = new TestCallback();

		// When
		classUnderTest.dispatch(cmd, callback);

		// Then
		assertTrue(callback.error);
	}

	public static class TestCallback implements CommandCallback<String> {

		boolean success = false;
		boolean error = false;


		@Override
		public void onError(Throwable throwable) {
			error = true;
			System.err.println("Error: " + throwable);
		}

		@Override
		public void onSuccess(Optional<String> result) {
			success = true;
			System.out.println("Success: " + result.orElse("NO RESULT"));
		}
	}

	@Command
	private final static class CommandSync {

		@Override
		public String toString() {
			return "CommandSync{}";
		}
	}

	@Command
	private final static class FailingCommandSync {

		@Override
		public String toString() {
			return "FailingCommandSync{}";
		}
	}

	@Command(asynchronous = true)
	private final static class CommandAsync {
		@Override
		public String toString() {
			return "CommandAsync{}";
		}
	}

	private final static class SyncCommandHandler implements CommandHandler<CommandSync, String> {

		@Override
		public Optional<String> handle(CommandSync command) {
			return Optional.of("Result of SyncCommandHandler");
		}

		@Override
		public boolean accept(Object command) {
			return CommandSync.class.isAssignableFrom(command.getClass());
		}

		@Override
		public String toString() {
			return "SyncCommandHandler{}";
		}
	}

	private final static class FailingSyncCommandHandler implements CommandHandler<FailingCommandSync, String> {

		@Override
		public Optional<String> handle(FailingCommandSync command) {
			throw new NullPointerException("Some exception to handle");
		}

		@Override
		public boolean accept(Object command) {
			return FailingCommandSync.class.isAssignableFrom(command.getClass());
		}

		@Override
		public String toString() {
			return "FailingSyncCommandHandler{}";
		}
	}

	private final static class AsyncCommandHandler implements CommandHandler<CommandAsync, String> {

		@Override
		public Optional<String> handle(CommandAsync command) {
			return Optional.of("Result of AsyncCommandHandler");
		}

		@Override
		public boolean accept(Object command) {
			return CommandAsync.class.isAssignableFrom(command.getClass());
		}

		@Override
		public String toString() {
			return "AsyncCommandHandler{}";
		}
	}
}
