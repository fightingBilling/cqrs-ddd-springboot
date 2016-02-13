package org.dsinczak.boot.common.service;

/**
 * Created by hu2sinc on 11/26/2015.
 */
public class CommandValidationError {

		String test = "bad";

		public String getTest() {
				return test;
		}

		public void setTest(final String test) {
				this.test = test;
		}

		@Override
		public String toString() {
				return "CommandValidationError{" +
						"test='" + test + '\'' +
						'}';
		}
}
