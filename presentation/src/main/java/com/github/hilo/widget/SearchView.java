package com.github.hilo.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.ResultReceiver;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.hilo.R;

import java.lang.reflect.Method;

/**
 * Simplified version of SearchView, only EditText and a clear text button is supported
 * Thanks to code of SearchView in AppCompat
 */
public class SearchView extends LinearLayout {
    static final AutoCompleteTextViewReflector HIDDEN_METHOD_INVOKER = new AutoCompleteTextViewReflector();

    private boolean mClearingFocus;

    private SearchAutoComplete mQueryTextView;
    private View mSearchPlate;
    private ImageView mClearTextButton;
    private OnQueryTextListener mOnQueryChangeListener;
    private CharSequence mQueryHint;
    private Runnable mShowImeRunnable = () -> {
        InputMethodManager imm = (InputMethodManager)
                getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm != null) {
            HIDDEN_METHOD_INVOKER.showSoftInputUnchecked(imm, SearchView.this, 0);
        }
    };

    private Runnable mUpdateDrawableStateRunnable = this::updateFocusedState;

    public SearchView(Context context) {
        this(context, null);
    }

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.search_view,this,true);

        mQueryTextView = (SearchAutoComplete) findViewById(R.id.search_src_text);
        mQueryTextView.setSearchView(this);

        mSearchPlate = findViewById(R.id.search_plate);
        mClearTextButton = (ImageView) findViewById(R.id.search_close_btn);

        mClearTextButton.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(mQueryTextView.getText())) {
                mQueryTextView.setText("");
                mQueryTextView.requestFocus();
                setImeVisibility(true);
            }
        });

        mQueryTextView.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int before, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int after) {
            }

            public void afterTextChanged(Editable s) {
                updateCloseButton();
            }
        });

        mQueryTextView.setOnEditorActionListener((v, actionId, event) -> {
            onSubmitQuery();
            return true;
        });

        setFocusable(true);
        this.updateViewsVisibility();
    }

    static boolean isLandscapeMode(Context context) {
        return context.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
    }

    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        if (mClearingFocus) return false;
        if (!isFocusable()) return false;

        boolean result = mQueryTextView.requestFocus(direction, previouslyFocusedRect);
        if (result) {
            this.updateViewsVisibility();
        }

        return result;
    }

    @Override
    public void clearFocus() {
        mClearingFocus = true;
        this.setImeVisibility(false);
        super.clearFocus();
        mQueryTextView.clearFocus();
        mClearingFocus = false;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        this.postUpdateFocusedState();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        switch (widthMode) {
            case MeasureSpec.AT_MOST:
                width = Math.min(getPreferredWidth(), width);
                break;
            case MeasureSpec.EXACTLY:
                break;
            case MeasureSpec.UNSPECIFIED:
                width = getPreferredWidth();
                break;
        }
        widthMode = MeasureSpec.EXACTLY;
        super.onMeasure(MeasureSpec.makeMeasureSpec(width, widthMode), heightMeasureSpec);
    }

    @Override
    protected void onDetachedFromWindow() {
        removeCallbacks(mUpdateDrawableStateRunnable);
        super.onDetachedFromWindow();
    }

    private int getPreferredWidth() {
        return (int) (320 * getContext().getResources().getDisplayMetrics().density + 0.5f);
    }

    public void setOnQueryTextListener(OnQueryTextListener listener) {
        mOnQueryChangeListener = listener;
    }

    private void forceSuggestionQuery() {
        HIDDEN_METHOD_INVOKER.doBeforeTextChanged(mQueryTextView);
        HIDDEN_METHOD_INVOKER.doAfterTextChanged(mQueryTextView);
    }

    void onTextFocusChanged() {
        this. updateViewsVisibility();
        this.postUpdateFocusedState();
        if (mQueryTextView.hasFocus()) {
            this.forceSuggestionQuery();
        }
    }

    private void updateViewsVisibility() {
        this.updateCloseButton();
    }

    private void updateCloseButton() {
        final boolean hasText = !TextUtils.isEmpty(mQueryTextView.getText());
        mClearTextButton.setVisibility(hasText ? VISIBLE : GONE);
        mClearTextButton.getDrawable().setState(hasText ? ENABLED_STATE_SET : EMPTY_STATE_SET);
    }

    private void postUpdateFocusedState() {
        post(mUpdateDrawableStateRunnable);
    }

    private void updateFocusedState() {
        boolean focused = mQueryTextView.hasFocus();
        mSearchPlate.getBackground().setState(focused ? FOCUSED_STATE_SET : EMPTY_STATE_SET);
        invalidate();
    }

    private void setImeVisibility(final boolean visible) {
        if (visible) {
            post(mShowImeRunnable);
        } else {
            removeCallbacks(mShowImeRunnable);
            InputMethodManager imm = (InputMethodManager)
                    getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

            if (imm != null) {
                imm.hideSoftInputFromWindow(getWindowToken(), 0);
            }
        }
    }

    public void setQueryHint(CharSequence hint) {
        mQueryHint = hint;
        this.updateQueryHint();
    }

    private CharSequence getDecoratedHint(CharSequence hintText) {
        Spannable ssb = new SpannableString(hintText);
        //noinspection deprecation
        ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.search_view_hint_color)),
                0, hintText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssb;
    }

    private void updateQueryHint() {
        if (mQueryHint != null) {
            mQueryTextView.setHint(getDecoratedHint(mQueryHint));
        } else {
            mQueryTextView.setHint(getDecoratedHint(""));
        }
    }

    private void onSubmitQuery() {
        CharSequence query = mQueryTextView.getText();
        if (query != null && TextUtils.getTrimmedLength(query) > 0) {
            if (mOnQueryChangeListener != null) {
                mOnQueryChangeListener.onQueryTextSubmit(query.toString());
            }
        }
    }

    public interface OnQueryTextListener {
        boolean onQueryTextSubmit(String query);
    }

    public static class SearchAutoComplete extends AutoCompleteTextView {
        private int mThreshold;
        private SearchView mSearchView;

        public SearchAutoComplete(Context context, AttributeSet attrs) {
            super(context, attrs);
            mThreshold = getThreshold();
        }

        void setSearchView(SearchView searchView) {
            mSearchView = searchView;
        }

        @Override
        public void setThreshold(int threshold) {
            super.setThreshold(threshold);
            mThreshold = threshold;
        }

        @Override
        protected void replaceText(CharSequence text) {
        }

        @Override
        public void performCompletion() {
        }

        @Override
        public void onWindowFocusChanged(boolean hasWindowFocus) {
            super.onWindowFocusChanged(hasWindowFocus);

            if (hasWindowFocus && mSearchView.hasFocus() && getVisibility() == VISIBLE) {
                InputMethodManager inputManager = (InputMethodManager) getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(this, 0);
                if (isLandscapeMode(getContext())) {
                    HIDDEN_METHOD_INVOKER.ensureImeVisible(this, true);
                }
            }
        }

        @Override
        protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
            mSearchView.onTextFocusChanged();
        }

        @Override
        public boolean enoughToFilter() {
            return mThreshold <= 0 || super.enoughToFilter();
        }

        @Override
        public boolean onKeyPreIme(int keyCode, @SuppressWarnings("NullableProblems") KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                    KeyEvent.DispatcherState state = getKeyDispatcherState();
                    if (state != null) {
                        state.startTracking(event, this);
                    }
                    return true;
                } else if (event.getAction() == KeyEvent.ACTION_UP) {
                    KeyEvent.DispatcherState state = getKeyDispatcherState();
                    if (state != null) {
                        state.handleUpEvent(event);
                    }
                    if (event.isTracking() && !event.isCanceled()) {
                        mSearchView.clearFocus();
                        mSearchView.setImeVisibility(false);
                        return true;
                    }
                }
            }
            return super.onKeyPreIme(keyCode, event);
        }
    }

    private static class AutoCompleteTextViewReflector {
        private Method doBeforeTextChanged, doAfterTextChanged;
        private Method ensureImeVisible;
        private Method showSoftInputUnchecked;

        AutoCompleteTextViewReflector() {
            try {
                doBeforeTextChanged = AutoCompleteTextView.class
                        .getDeclaredMethod("doBeforeTextChanged");
                doBeforeTextChanged.setAccessible(true);
            } catch (NoSuchMethodException ignored) {

            }
            try {
                doAfterTextChanged = AutoCompleteTextView.class
                        .getDeclaredMethod("doAfterTextChanged");
                doAfterTextChanged.setAccessible(true);
            } catch (NoSuchMethodException ignored) {

            }
            try {
                ensureImeVisible = AutoCompleteTextView.class
                        .getMethod("ensureImeVisible", boolean.class);
                ensureImeVisible.setAccessible(true);
            } catch (NoSuchMethodException ignored) {

            }
            try {
                showSoftInputUnchecked = InputMethodManager.class.getMethod(
                        "showSoftInputUnchecked", int.class, ResultReceiver.class);
                showSoftInputUnchecked.setAccessible(true);
            } catch (NoSuchMethodException ignored) {

            }
        }

        void doBeforeTextChanged(AutoCompleteTextView view) {
            if (doBeforeTextChanged != null) {
                try {
                    doBeforeTextChanged.invoke(view);
                } catch (Exception ignored) {

                }
            }
        }

        void doAfterTextChanged(AutoCompleteTextView view) {
            if (doAfterTextChanged != null) {
                try {
                    doAfterTextChanged.invoke(view);
                } catch (Exception ignored) {

                }
            }
        }

        void ensureImeVisible(AutoCompleteTextView view, boolean visible) {
            if (ensureImeVisible != null) {
                try {
                    ensureImeVisible.invoke(view, visible);
                } catch (Exception ignored) {

                }
            }
        }

        void showSoftInputUnchecked(InputMethodManager imm, View view, int flags) {
            if (showSoftInputUnchecked != null) {
                try {
                    showSoftInputUnchecked.invoke(imm, flags, null);
                    return;
                } catch (Exception ignored) {

                }
            }

            imm.showSoftInput(view, flags);
        }
    }
}
