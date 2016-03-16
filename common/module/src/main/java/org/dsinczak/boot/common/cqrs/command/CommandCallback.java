package org.dsinczak.boot.common.cqrs.command;


import java.util.Optional;

public interface CommandCallback<R> {

	void onError(Throwable throwable);

	void onSuccess(Optional<R> result);
}
