package com.github.hilo.view.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.widget.TextView;

import com.github.hilo.R;
import com.github.hilo.di.components.DaggerDetailsComponent;
import com.github.hilo.di.components.DetailsComponent;

import butterknife.Bind;
import butterknife.OnClick;

public class DetailsActivity extends BaseToolbarActivity {

	@Bind(R.id.tvTitle) TextView tvTitle;
	private DetailsComponent detailsComponent;

	@Override protected int getLayoutId() {
		return R.layout.activity_details;
	}

	@Override protected void initInjector() {
		detailsComponent = DaggerDetailsComponent.builder()
																						 .activityModule(getActivityModule())
																						 .applicationComponent(getApplicationComponent())
																						 .build();
	}

	@Override protected void initViews(Bundle savedInstanceState) {
		this.setupEnterActivityAnimation();
	}

	@Override protected void initData() {

	}

	@Override protected void initListeners() {

	}

	@OnClick(R.id.tvTitle) public void onClick() {
		getApplicationComponent().toast().makeText("dont click me plz :(");
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP) private void setupEnterActivityAnimation() {
		getWindow().setEnterTransition(new Fade());
	}

	public DetailsComponent getDetailsComponent() {return detailsComponent;}
}
