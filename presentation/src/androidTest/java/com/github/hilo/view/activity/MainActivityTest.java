package com.github.hilo.view.activity;

import android.app.Fragment;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import com.github.hilo.R;
import com.github.hilo.di.components.UserComponent;

import org.junit.Assert;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class MainActivityTest extends ActivityInstrumentationTestCase2 {

	private MainActivity mainActivity;
	@Mock UserComponent userComponent;

	public MainActivityTest() {
		super(MainActivity.class);
	}

	/** 标记单元测试的数据初始化开始 */
	@Override protected void setUp() throws Exception {
		super.setUp();
		MockitoAnnotations.initMocks(this);
		this.setActivityIntent(new Intent(getInstrumentation().getTargetContext(), MainActivity.class));
		mainActivity = (MainActivity)getActivity();
	}

	/** 标记单元测试完成并开始回收初始化数据垃圾 */
	@Override protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testContainsUserListFragment() {
		Fragment userListFragment = mainActivity.getFragmentManager().findFragmentById(R.id.fragmentContainer);
		Assert.assertThat(userListFragment,is(notNullValue()));
		Assert.assertThat(userComponent,is(notNullValue()));
	}

	public void testUserComponentMethod() {
		MainActivity mainActivity = (MainActivity)userComponent.getActivity();
		Assert.assertEquals(mainActivity,MainActivity.class);
	}

	public void testContainsProperTitle() {
		String actualTitle = mainActivity.getTitle().toString().trim();
		Assert.assertThat(actualTitle,is("MVP-DAGGER2-RETROFIT-RX"));
	}
}
