package com.github.hilo.view.activity;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.hilo.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/3/16.
 */
public abstract class BaseNoXmlToolbarActivity extends BaseAppCompatActivity {

	protected ToolbarHelper toolBarHelper;
	protected Toolbar toolbar;
	protected TextView toolbarTitle;

	@Override public void setContentView(@LayoutRes int layoutResID) {
		toolBarHelper = new ToolbarHelper(this,layoutResID);
		toolbar = toolBarHelper.getToolbar();
		toolbarTitle = toolBarHelper.getToolbarTitle();
		setContentView(toolBarHelper.getContentView());
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		ButterKnife.bind(this);

		toolbar.setNavigationOnClickListener(v -> {
			finish();
			overridePendingTransition(0,R.anim.activity_swipeback_ac_right_out);
		});
	}

	static class ToolbarHelper {

		@Bind(R.id.toolbar) Toolbar toolbar;
		@Bind(R.id.toolbarTitle) TextView toolbarTitle;
		private Context mContext;
		private FrameLayout baseContentView;
		private View constomView;
		private LayoutInflater mLayoutInflater;

		private static int[] ATTRS = {R.attr.windowActionBarOverlay,R.attr.actionBarSize};

		ToolbarHelper(Context context,int layoutResID) {
			mContext = context;
			mLayoutInflater = LayoutInflater.from(mContext);

			this.initContentView();
			this.initConstomView(layoutResID);
			this.initToolbar();
		}

		private void initContentView() {
			baseContentView = new FrameLayout(mContext);
			ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
							ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
			baseContentView.setLayoutParams(params);
		}


		private void initConstomView(int layoutResID) {
			constomView = mLayoutInflater.inflate(layoutResID,null);
			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
							ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
			TypedArray ta = mContext.getTheme().obtainStyledAttributes(ATTRS);
			// 获取toolbar悬浮标志
			boolean overly = ta.getBoolean(0,false);
			// 获取toolbar高度
			int toolbarHeight = (int)ta.getDimension(0,mContext.getResources()
																												 .getDimension(R.dimen.status_bar_height));
			ta.recycle();
			params.topMargin = overly ? 0 : toolbarHeight;
			baseContentView.addView(constomView,params);
		}

		private void initToolbar() {
			View toolbarView = mLayoutInflater.inflate(R.layout.toolbar_default,baseContentView);
			ButterKnife.bind(this,toolbarView);
			toolbar.setTitle("");
		}

		Toolbar getToolbar() {
			return toolbar;
		}

		TextView getToolbarTitle() {
			return toolbarTitle;
		}

		FrameLayout getContentView() {
			return baseContentView;
		}
	}
}
