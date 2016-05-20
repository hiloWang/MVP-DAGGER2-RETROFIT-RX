package com.github.hilo.view.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;

import com.github.hilo.R;

public class DetailsActivity extends BaseToolbarActivity {

	@Override protected int getLayoutId() {
		return R.layout.activity_details;
	}

	@Override protected void initInjector() {

	}

	@Override protected void initViews(Bundle savedInstanceState) {
		this.setupEnterActivityAnimation();
	}

	@Override protected void initData() {

	}

	@Override protected void initListeners() {

	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP) private void setupEnterActivityAnimation() {
		/*getWindow().setEnterTransition(new Fade());*/
	}
}
