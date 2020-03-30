package com.rice.tool;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerHelper {

    /**
     * RecyclerView 移动到当前位置，
     *
     * @param mRecyclerView 当前的RecyclerView
     * @param n             要跳转的位置
     */
    public static void moveToPosition(RecyclerView mRecyclerView, int n) {
        RecyclerView.LayoutManager manager = mRecyclerView.getLayoutManager();
        if (manager instanceof LinearLayoutManager) {
            int firstItem = ((LinearLayoutManager) manager).findFirstVisibleItemPosition();
            int lastItem = ((LinearLayoutManager) manager).findLastVisibleItemPosition();
            if (n <= firstItem) {
                mRecyclerView.scrollToPosition(n);
            } else if (n <= lastItem) {
                int top = mRecyclerView.getChildAt(n - firstItem).getTop();
                mRecyclerView.scrollBy(0, top);
            } else {
                mRecyclerView.scrollToPosition(n);
            }
        }

    }

}
