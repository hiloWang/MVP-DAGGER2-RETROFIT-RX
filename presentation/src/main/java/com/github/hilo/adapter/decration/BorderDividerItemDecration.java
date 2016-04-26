package com.github.hilo.adapter.decration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2016/4/17.
 * ItemDecoration：在每个条目的视图的周围或上面绘制一些装饰视图。
 * <p>
 * ItemDecoration虚基类包含以下三个方法：
 * public void onDraw(Canvas c, RecyclerView parent)
 * public void onDrawOver(Canvas c, RecyclerView parent)
 * public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent)
 * <p>
 * onDraw中绘制的内容可能会被条目内容所覆盖，如果要在条目上绘制内容需要使用onDrawOver()方法。
 * 如果需要条目间距来绘制分割线，那么使用以上两个方法的差别不大，但是如果真的要添加装饰效果，还是需要使用onDrawOver()。
 * <p>
 * LayoutManager在测量阶段会调用getItemOffset()方法来正确计算每个条目视图的大小。
 * outRect参数可能第一眼看来有些奇怪，为什么不使用一个返回值呢？但是这样的参数设置确实是有意义的，
 * 它使得对于所有的子视图RecyclerView可以重用同一个Rect对象，节省了资源开销。这并不是必须的，但确实是很有效率的。
 */
public class BorderDividerItemDecration extends RecyclerView.ItemDecoration {

    private final int verticalItemSpacingInPx;
    private final int horizontalItemSpacingInPx;

    public BorderDividerItemDecration(int verticalItemSpacingInPx, int horizontalItemSpacingInPx) {
        this.verticalItemSpacingInPx = verticalItemSpacingInPx;
        this.horizontalItemSpacingInPx = horizontalItemSpacingInPx;
    }

    /**
     * 正确计算每个条目视图的大小
     *
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        int itemPosition = layoutParams.getViewLayoutPosition();
        int itemCount = parent.getAdapter().getItemCount();

        int left = horizontalItemSpacingInPx;
        int right = horizontalItemSpacingInPx;
        int top = getItemTopSpacing(itemPosition);
        int bottom = getItemBottomSpacing(itemPosition, itemCount);

        // 高效率，复用机制；
        outRect.set(left, top, right, bottom);
    }

    private int getItemTopSpacing(int itemPosition) {
        if (isFirstItem(itemPosition))
            return verticalItemSpacingInPx;
        return verticalItemSpacingInPx / 2;

    }

    private int getItemBottomSpacing(int itemPosition, int itemCount) {
        if (isLastItem(itemPosition, itemCount))
            return verticalItemSpacingInPx;
        return verticalItemSpacingInPx / 2;
    }

    private boolean isFirstItem(int itemPosition) {
        return itemPosition == 0;
    }

    private boolean isLastItem(int itemPosition, int itemCount) {
        return itemPosition == itemCount - 1;
    }
}
