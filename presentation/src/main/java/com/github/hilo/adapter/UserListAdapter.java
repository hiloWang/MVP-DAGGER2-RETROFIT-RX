package com.github.hilo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.github.hilo.R;
import com.github.hilo.interfaces.OnNoDoubleClickListener;
import com.github.hilo.model.UserModel;
import com.github.hilo.util.DateUtils;
import com.github.hilo.util.ToastUtils;

import java.io.File;
import java.util.Date;

import javax.inject.Inject;

public class UserListAdapter extends BaseRecyclerViewAdapter {

    public static final int VIEW_TYPE_DEFAULT = 1;
    private Context context;
    private final int[] drawableIcons = new int[]{R.drawable.center_1, R.drawable.center_2, R.drawable.center_3, R.drawable.center_4, R.drawable.center_5};
    @Inject
    ToastUtils toastUtils;

    public UserListAdapter(Context context) {
        super();
        this.context = context;
    }

    @Override
    public int[] getItemLayouts() {
        return new int[] {
                R.layout.item_userlist_fragment
        };
    }

    @Override
    public void onBindRecycleViewHolder(BaseRecyclerViewHolder viewHolder, int position) {
        int itemViewType = getRecycleViewItemType(position);
        switch (itemViewType) {
            case VIEW_TYPE_DEFAULT:
                bindDefaultView(viewHolder, position);
                break;
        }
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return VIEW_TYPE_DEFAULT;
    }

    private void bindDefaultView(BaseRecyclerViewHolder viewHolder, int position) {
        UserModel data = getItemByPosition(position);
        if (data == null) return;
        TextView mDailyTitleTv = viewHolder.findViewById(R.id.daily_title_tv);
        TextView mDailyDateTv = viewHolder.findViewById(R.id.daily_date_tv);
        ImageView mDailyIv = viewHolder.findViewById(R.id.daily_iv);

        mDailyTitleTv.setText("测试内容" + position + ": 这脑洞太大我无力承受。[笑cry]");
        mDailyDateTv.setText(DateUtils.date2yyyyMMdd(new Date()));

        mDailyIv.setScaleX(0.f);
        mDailyIv.setScaleY(0.f);

        Glide.with(context)
                .load(drawableIcons[(int) (Math.random() * 5)])
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.mipmap.ic_launcher)
                .into(mDailyIv);

        mDailyIv.animate()
                .scaleX(1.f).scaleY(1.f)
                .setInterpolator(new OvershootInterpolator())
                .setDuration(1000);

            mDailyIv.setOnClickListener(new OnNoDoubleClickListener() {
            @Override
            protected void onNoDoubleClickListener(View v) {
//                Glide.get(context).clearMemory();
            }
        });
    }
}
