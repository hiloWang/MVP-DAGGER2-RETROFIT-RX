package com.github.hilo.presenter;

import com.github.hilo.view.MvpView;

import rx.subscriptions.CompositeSubscription;

public abstract class BasePresenter<T extends MvpView> implements Presenter<T> {

	private T mMvpView;
	public CompositeSubscription mCompositeSubscription;

	@Override public void attachView(T mvpView) {
		this.mMvpView = mvpView;
		this.mCompositeSubscription = new CompositeSubscription();
	}

	@Override public void detachView() {
		this.mMvpView = null;
		this.mCompositeSubscription.unsubscribe();
		this.mCompositeSubscription = null;
	}

	public boolean isViewAttached() {
		return mMvpView != null;
	}

	public void checkViewAttached() {
		if (!isViewAttached()) throw new MvpViewNotAttachedException();
	}

	public T getMvpView() {
		return mMvpView;
	}

	public static class MvpViewNotAttachedException extends RuntimeException {
		public MvpViewNotAttachedException() {
			super("Please call Presenter.attachView(MvpView) before" + " requesting data to the " +
										"Presenter");
		}
	}
}
