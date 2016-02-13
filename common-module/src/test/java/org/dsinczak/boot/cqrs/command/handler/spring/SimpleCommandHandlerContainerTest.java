package org.dsinczak.boot.cqrs.command.handler.spring;

import org.dsinczak.boot.common.command.handler.SimpleCommandHandlerContainer;
import org.dsinczak.boot.cqrs.annotation.Command;
import org.dsinczak.boot.cqrs.annotation.CommandHandlerAnnotation;
import org.dsinczak.boot.cqrs.command.handler.CommandHandler;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class SimpleCommandHandlerContainerTest {

	private SimpleCommandHandlerContainer container;

	@Before
	public void setUp() {
		container = new SimpleCommandHandlerContainer(
				Arrays.asList(new Command1Handler(), new Command2Handler()));
	}

	@Test(expected = IllegalStateException.class)
	public void shouldThrowExceptionForCommandWithNoHandler() {
		// Given, When
		container.getHandler(new Command3());
	}

	@Test(expected = IllegalStateException.class)
	public void shouldThrowExceptionForMultipleHandlers() {
		// Given
		container = new SimpleCommandHandlerContainer(
				Arrays.asList(new Command1Handler(), new Command2Handler(), new Command2AnotherHandler()));
		// When
		container.getHandler(new Command2(Boolean.TRUE));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldProcessCommandSynchronouslyAndReturnResult() {
		// Given
		final Command2 command = new Command2(Boolean.TRUE);
		final CommandHandler<Command2, Boolean> handler = container.getHandler(command).get();

		// When
		final Optional<Boolean> result = handler.handle(command);

		// Then
		assertEquals(Boolean.TRUE, result.get());
	}

	@Command(asynchronous = true)
	private final static class Command1 {

		@Override
		public String toString() {
			return "Command1 []";
		}

	}

	@Command
	private final static class Command2 {

		private final Boolean flag;

		public Command2(final Boolean flag) {
			super();
			this.flag = flag;
		}

		@Override
		public String toString() {
			return "Command2 []";
		}

	}

	@Command
	private final static class Command3 {

		@Override
		public String toString() {
			return "Command3 []";
		}

	}

	@CommandHandlerAnnotation
	private final static class Command1Handler implements CommandHandler<Command1, Void> {

		@Override
		public Optional<Void> handle(final Command1 command) {
			System.out.println("Command1Handler: " + command);
			return null;
		}

		@Override
		public boolean accept(final Object command) {

			return command instanceof Command1;
		}

	}

	@CommandHandlerAnnotation
	private final static class Command2Handler implements CommandHandler<Command2, Boolean> {

		@Override
		public Optional<Boolean> handle(final Command2 command) {
			System.out.println("Command2Handler: " + command);
			return Optional.of(command.flag);
		}

		@Override
		public boolean accept(final Object command) {
			return command instanceof Command2;
		}

	}

	@CommandHandlerAnnotation
	private final static class Command2AnotherHandler implements CommandHandler<Command2, Boolean> {

		@Override
		public Optional<Boolean> handle(final Command2 command) {
			System.out.println("Command2AnotherHandler: " + command);
			return Optional.of(Boolean.TRUE);
		}

		@Override
		public boolean accept(final Object command) {
			return command instanceof Command2;
		}

	}

}
