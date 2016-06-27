package com.github.hilo.util;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

// this is the middleman object
@Singleton
public class RxBus {

	@Inject public RxBus() {}

	private final Subject<Object, Object> _bus = new SerializedSubject<>(PublishSubject.create());

	/** send the event */
	public void send(Object o) {
		_bus.onNext(o);
	}

	/** subscribe to the event */
	public Observable<Object> toObserverable() {
		return _bus;
	}

	public <T> Observable<T> toObserverable(final Class<T> eventType) {
		return _bus.filter(o -> eventType.isInstance(o)).cast(eventType);
	}
}
