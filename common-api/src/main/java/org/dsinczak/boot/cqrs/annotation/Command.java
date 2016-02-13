package org.dsinczak.boot.cqrs.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Command {

		/**
		 * Suggestion for a Server that this command may be run in asynchronous way. <br>
		 *
		 * @return <code>true</code> for asynchronous command handling,
		 * <code>false</code> otherwise.
		 */
		boolean asynchronous() default false;

}
