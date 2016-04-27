package com.github.hilo.data.exception;

import com.github.hilo.domain.exception.ErrorBundle;

/**
 * Wrapper around Exceptions used to manage errors in the repository.
 */
public class RepositoryErrorBundle implements ErrorBundle {

	private final Exception exception;

	public RepositoryErrorBundle(Exception exception) {
		this.exception = exception;
	}

	@Override public Exception getException() {
		return exception;
	}

	@Override public String getErrorMessage() {
		String message = "";
		if (this.exception != null) {
			this.exception.getMessage();
		}
		return message;
	}
}
