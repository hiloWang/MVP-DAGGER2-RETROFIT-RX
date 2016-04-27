/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.hilo.domain.interactor;


import com.github.hilo.domain.executor.PostExecutionThread;
import com.github.hilo.domain.executor.ThreadExecutor;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * Abstract class for a Use Case (Interactor in terms of Clean Architecture).
 * This interface represents a execution unit for different use cases (this means any use case
 * in the application should implement this contract).
 * <p>
 * By convention each UseCase implementation will return the result using a {@link Subscriber}
 * that will execute its job in a background thread and will post the result in the UI thread.
 */
public abstract class UseCase {

	private final ThreadExecutor threadExecutor;
	private final PostExecutionThread postExecutionThread;

	/**
	 * not a Subscriber like UseCaseSubscriber
	 */
	private Subscription subscription = Subscriptions.empty();

	protected UseCase(ThreadExecutor threadExecutor,PostExecutionThread postExecutionThread) {
		this.threadExecutor = threadExecutor;
		this.postExecutionThread = postExecutionThread;
	}

	/**
	 * Builds an {@link Observable} which will be used when executing the current {@link UseCase}.
	 */
	protected abstract Observable buildUseCaseObservable();

	/**
	 * Executes the current use case.
	 *
	 * @param UseCaseSubscriber The guy who will be listen to the observable build
	 *                          with {@link #buildUseCaseObservable()}.
	 */
	@SuppressWarnings("unchecked") public void execute(Subscriber UseCaseSubscriber) {
		this.subscription = this.buildUseCaseObservable()
														.subscribeOn(Schedulers.from(threadExecutor))
														.observeOn(postExecutionThread.getScheduler())
														.subscribe(UseCaseSubscriber);
	}

	@SuppressWarnings("unchecked") public Observable execute() {
		return this.buildUseCaseObservable()
							 .subscribeOn(Schedulers.from(threadExecutor))
							 .observeOn(postExecutionThread.getScheduler());
	}

	/**
	 * Unsubscribes from current {@link Subscription}.
	 */
	public void unsubscribe() {
		if (!subscription.isUnsubscribed()) {
			subscription.unsubscribe();
		}
	}
}
