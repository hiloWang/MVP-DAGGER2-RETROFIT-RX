package com.github.hilo.data.net;

import java.util.concurrent.Callable;

public interface ApiConnection extends Callable<RestApiA> {

	@Override
	RestApiA call() throws Exception;

	RestApiA requestSyncCall();

}
