package com.github.hilo.presenter;

import com.github.hilo.view.MvpView;

import rx.subscriptions.CompositeSubscription;

public abstract class BasePresenter<T extends MvpView> implements Presenter<T> {

	private T mvpView;
	public CompositeSubscription compositeSubscription;

	@Override public void attachView(T mvpView) {
		this.mvpView = mvpView;
		this.compositeSubscription = new CompositeSubscription();
	}

	@Override public void detachView() {
		this.mvpView = null;
		if (!compositeSubscription.isUnsubscribed()) {
			compositeSubscription.unsubscribe();
			compositeSubscription = null;
		}
	}

	public boolean isViewAttached() {
		return mvpView != null;
	}

	public void checkViewAttached() {
		if (!isViewAttached()) throw new MvpViewNotAttachedException();
	}

	public T getMvpView() {
		return mvpView;
	}

	public static class MvpViewNotAttachedException extends RuntimeException {
		public MvpViewNotAttachedException() {
			super("Please call Presenter.attachView(MvpView) before" + " requesting data to the " +
										"Presenter");
		}
	}
}
