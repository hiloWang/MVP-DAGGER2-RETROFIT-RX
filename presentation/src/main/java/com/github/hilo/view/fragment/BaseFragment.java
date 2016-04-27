package com.github.hilo.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.hilo.di.interfaces.HasComponent;
import com.github.hilo.view.activity.MainActivity;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {

	protected View rootView;
	/**
	 * 临时的Bundle对象，用于存储用户的数据，如果用户保存了数据，那么最终将该对象put到arguments的bundle对象中
	 */
	private Bundle savedStateBundle;
	public static final String ARGS_BUNDLE_KEY = "internalSavedViewState8954201239547";

	/**
	 * Fragment 要有一个默认的构造函数，Fragment在重新创建/还原的时候会调用默认的构造函数，会在重新创建时将状态保存到一个包（Bundle）对象，
	 * 当还原时，将参数包还原到新建的Fragment。该Fragment执行的后续回调能够访问这些参数，可以将碎片还原到上一个状态；
	 */
	public BaseFragment() {
		super();
	}

	@Override public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
							"Activity must be implement BaseFragment of callbacks method.");
		}
		mCallbacks = (Callbacks)activity;
	}

	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.initInjector();
	}

	@Override public View onCreateView(LayoutInflater inflater,ViewGroup container,
					Bundle savedInstanceState) {
		super.onCreateView(inflater,container,savedInstanceState);
		/**
		 * 避免同一个activity有多个fragment切换时，多次调用onCreateView，将rootView做成成员变量实现缓存rootView
		 * 处理，还要判断当前是否有父布局，做些逻辑处理，不然会报错；
		 */
		if (this.rootView == null) {
			this.rootView = inflater.inflate(this.getLayoutId(),container,false);
		}
		if (this.rootView.getParent() != null) {
			ViewGroup parent = (ViewGroup)this.rootView.getParent();
			parent.removeView(this.rootView);
		}

		ButterKnife.bind(this,this.rootView);

		this.initViews(this.rootView,savedInstanceState);
		this.initListeners();
		this.initData();
		return this.rootView;
	}

	/**
	 * 恢复数据是在这里处理的，onActivityCreated方法的回调是基于Activity onCreate回调执行完才会执行此方法， 那么当app
	 * 切到桌面时，实际上并没有调用onDestroyView，所以下次回到app时，并不会触发该方法
	 * 当切到别的fragment时，下次进入会调用该方法。
	 */
	@Override public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// Restore State Here
		if (!viewStateRestoredFromArguments()) {
			// First Time, Initialize something here
			initData();
		}
	}

	@Override public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		saveInstanceState();
	}

	@Override public void onPause() {
		beforePause();
		super.onPause();
	}

	@Override public void onResume() {
		super.onResume();
		afterResume();
	}

	@Override public void onDestroyView() {
		super.onDestroyView();
		/**
		 * 当fragment从回退栈返回时（addToBackStack）， 可能不会调用onSavedInstance() 只会销毁视图， 所以这里也要保存一份数据；
		 */
		saveInstanceState();
		ButterKnife.unbind(this);
	}

	@Override public void onDestroy() {
		beforeDestroy();
		super.onDestroy();
	}

	@Override public void onDetach() {
		super.onDetach();
		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;
		afterOnDetach();
	}

	protected void afterOnDetach() {}

	protected void afterResume() {}

	protected void beforePause() {}

	protected void beforeDestroy() {}

	/**
	 * Fill in layout id
	 *
	 * @return layout id
	 */
	protected abstract int getLayoutId();

	/**
	 * Initialize the view in the layout
	 *
	 * @param self               rootView
	 * @param savedInstanceState savedInstanceState
	 */
	protected abstract void initViews(View self,Bundle savedInstanceState);

	/**
	 * Initialize the View of the listener
	 */
	protected abstract void initListeners();

	/**
	 * Initialize the Activity data
	 */
	protected abstract void initData();

	/**
	 * Initialize the activity Component
	 */
	protected abstract void initInjector();

	/**
	 * 恢复数据回调
	 *
	 * @param savedInstanceState 存储在Arguments里的onSaveState Bundle对象
	 */
	protected abstract void mOnViewStateRestored(Bundle savedInstanceState);

	/**
	 * 保存数据回调
	 */
	protected abstract void mOnSaveInstanceState(Bundle savedInstanceState);

	/**
	 * 保存用户数据到arguments的bundle里
	 */
	private void saveInstanceState() {
		/**
		 * 当回退栈中，存在1个以上fragment时，通过俩次旋转屏幕，第一次从竖屏到横屏，会调用onSavedInstance，在切回竖屏时，可能会出现crash，
		 * 当你旋转屏幕，回退栈中Fragment的view将会销毁，同时在返回之前不会重建。这就导致了当你再一次旋转屏幕，
		 * 没有可以保存数据的view。saveState()将会引用到一个不存在的view而导致空指针异常NullPointerException
		 * 因此需要先检查view是否存在。如果存在保存其状态数据，将Argument中的数据再次保存一遍，或者干脆啥也不做，因为第一次已经保存了。
		 */

		if (getView() != null) {
			savedStateBundle = this.validateSavedStateBundleIsNull();
		}
		if (savedStateBundle != null) {
			Bundle argsBundle = getArguments();
			argsBundle.putBundle(ARGS_BUNDLE_KEY,savedStateBundle);
		}
	}

	/**
	 * onActivityCreated 里恢复数据
	 */
	private boolean viewStateRestoredFromArguments() {
		Bundle argumentsBundle = getArguments();
		this.validateArguments(argumentsBundle);
		savedStateBundle = argumentsBundle.getBundle(ARGS_BUNDLE_KEY);
		if (savedStateBundle != null) {
			this.mOnViewStateRestored(savedStateBundle);
			return true;
		}
		return false;
	}

	/**
	 * nothing to do if savedStatebundle is Null,  otherwise return savedStatebundle;
	 */
	private Bundle validateSavedStateBundleIsNull() {
		Bundle initSavedStateBundleData = new Bundle();
		this.mOnSaveInstanceState(initSavedStateBundleData);
		return initSavedStateBundleData;
	}


	private Bundle validateArguments(Bundle argumentsBundle) {
		if (argumentsBundle == null)
			throw new IllegalArgumentException("The Activity must be setArguments to Fragment");
		return argumentsBundle;
	}

	/**
	 * Gets a component for dependency injection by its type.
	 */
	@SuppressWarnings("unchecked") protected <C> C getComponent(Class<C> componentType) {
		return componentType.cast(((HasComponent<C>)getActivity()).getComponent());
	}

	protected void showToast(String msg) {
		((MainActivity)getActivity()).getApplicationComponent().toast().makeText(msg);
	}

	protected void showToast(String msg,int duration) {
		if (msg == null) return;
		if (duration == Toast.LENGTH_SHORT || duration == Toast.LENGTH_LONG) {
			((MainActivity)getActivity()).getApplicationComponent().toast().makeText(msg,duration);
		} else {
			showToast(msg);
		}
	}

	/**
	 * Find the view by id
	 *
	 * @param id  id
	 * @param <V> V
	 * @return V
	 */
	@SuppressWarnings("unchecked") protected <V extends View> V findView(int id) {
		return (V)this.rootView.findViewById(id);
	}

	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	protected Callbacks mCallbacks = sDummyCallbacks;

	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of item
	 * selections.
	 */
	public interface Callbacks {
		void onFragmentItemSelectedCallback(int position,String text);

		void controlFabBehaviorCallback(boolean scrollingDown);
	}

	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks sDummyCallbacks = new Callbacks() {
		@Override public void onFragmentItemSelectedCallback(int position,String text) {

		}

		@Override public void controlFabBehaviorCallback(boolean scrollingDown) {

		}
	};
}
