package com.github.hilo.data.net;

import java.util.concurrent.Callable;

public interface ApiConnection extends Callable<RestApi> {

	@Override
	RestApi call() throws Exception;

	RestApi requestSyncCall();

}
