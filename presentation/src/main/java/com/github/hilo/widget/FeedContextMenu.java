package com.github.hilo.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.hilo.R;
import com.github.hilo.util.UIUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedContextMenu extends LinearLayout {

	private static final int CONTEXT_MENU_WIDTH = UIUtils.dpToPx(180);
	private int position = -1;
	private OnFeedContextMenuClickListener onFeedContextMenuClickListener;

	public FeedContextMenu(Context context) {
		super(context);
		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.view_context_menu,this,true);
		setBackgroundResource(R.drawable.bg_container_shadow);
		setOrientation(VERTICAL);
		setLayoutParams(new LayoutParams(CONTEXT_MENU_WIDTH,ViewGroup.LayoutParams.WRAP_CONTENT));
	}

	@Override protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		ButterKnife.bind(this);
	}

	public void bindToItem(int position) {
		this.position = position;
	}

	public void dismiss() {
		((ViewGroup)getParent()).removeView(FeedContextMenu.this);
	}

	public void setOnFeedContextMenuClickListener(
					OnFeedContextMenuClickListener onFeedContextMenuClickListener) {
		this.onFeedContextMenuClickListener = onFeedContextMenuClickListener;
	}

	@SuppressWarnings("unchecked") @OnClick({R.id.btnReport,R.id.btnSharePhoto,R.id.btnShareUrl,R.id.btnCancel}) public void onClick(
					View view) {
		switch (view.getId()) {
		case R.id.btnReport:
			if (onFeedContextMenuClickListener != null) {
				onFeedContextMenuClickListener.onReportClick(position);
			}
			break;
		case R.id.btnSharePhoto:
			if (onFeedContextMenuClickListener != null) {
				onFeedContextMenuClickListener.onSharePhotoClick(position);
			}
			break;
		case R.id.btnShareUrl:
			if (onFeedContextMenuClickListener != null) {
				onFeedContextMenuClickListener.onCopyShareUrlClick(position);
			}
			break;
		case R.id.btnCancel:
			if (onFeedContextMenuClickListener != null) {
				onFeedContextMenuClickListener.onCancelClick(position);
			}
			break;
		}
	}

	public interface OnFeedContextMenuClickListener {
		void onReportClick(int position);

		void onSharePhotoClick(int position);

		void onCopyShareUrlClick(int position);

		void onCancelClick(int position);
	}
}
