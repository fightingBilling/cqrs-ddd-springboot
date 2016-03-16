package org.dsinczak.boot.common.controller;

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
