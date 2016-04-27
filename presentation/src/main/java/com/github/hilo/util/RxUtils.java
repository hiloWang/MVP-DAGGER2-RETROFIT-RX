package com.github.hilo.util;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RxUtils {

	@Inject public RxUtils() {}

	/**
	 * Get {@link Observable.Transformer} that transforms the source observable to subscribe in
	 * the io thread and observe on the Android's UI thread.
	 * <p>
	 * Because it doesn't interact with the emitted items it's safe ignore the unchecked casts.
	 * 订阅者scribe on mainUIThread， 观察者Observable on IO Thread
	 *
	 * @return {@link Observable.Transformer}
	 */
	@SuppressWarnings("unchecked") private <T> Observable.Transformer<T, T> createIOToMainThreadScheduler() {
		return tObservable -> tObservable.subscribeOn(Schedulers.io())
																		 .unsubscribeOn(Schedulers.computation())
																		 .observeOn(AndroidSchedulers.mainThread());
	}
}
