package com.csu.xgum.act;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.csu.xgum.R;
import com.csu.xgum.base.BaseActivity;
import com.example.mypublib.utils.MyLog;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DiscreteScrollViewTest extends BaseActivity {

    @BindView(R.id.picker)
    DiscreteScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_discrete_scroll_view_test);
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_discrete_scroll_view_test;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        List deviceDatas = new ArrayList<>();
        deviceDatas.add("");
        deviceDatas.add("");
        deviceDatas.add("");
        deviceDatas.add("");
        deviceDatas.add("");
        deviceDatas.add("");
        deviceDatas.add("");
        deviceDatas.add("");
        deviceDatas.add("");
        deviceDatas.add("");
        MytestAdpater scolladapter = new MytestAdpater(deviceDatas);
        scolladapter.setOnItemClickListener((adapter, view, position) -> {
            scrollView.smoothScrollToPosition(position);
        });
        scrollView.setAdapter(scolladapter);
        scrollView.setItemTransitionTimeMillis(80);//sett或smoothScroll上更改项目所需的时间
        scrollView.setOverScrollEnabled(true);
        scrollView.setSlideOnFling(true);
        scrollView.setSlideOnFlingThreshold(1000);//默认阈值设置为2100.降低阈值，使动画更加流畅。
//        scrollView.setClampTransformProgressAfter(3);
        scrollView.setItemTransformer(new ScaleTransformer.Builder()
                .setMaxScale(1.05f)
                .setMinScale(0.8f)
                .setPivotX(Pivot.X.CENTER) // CENTER is a default one
                .setPivotY(Pivot.Y.CENTER)
                .build());
        scrollView.addOnItemChangedListener(onItemChangedListener);
        scrollView.addScrollStateChangeListener(onStateChangeListener);
        //获取存储的是用户角标
        scrollView.scrollToPosition(1);
//        scrollView.setOffscreenItems(1000);//偏移
    }

    private DiscreteScrollView.OnItemChangedListener<RecyclerView.ViewHolder> onItemChangedListener = (RecyclerView.ViewHolder viewHolder, int adapterPosition) -> {
        MyLog.i("当前的手环index", adapterPosition + "");
    };
    private DiscreteScrollView.ScrollStateChangeListener onStateChangeListener = new DiscreteScrollView.ScrollStateChangeListener<RecyclerView.ViewHolder>() {
        @Override
        public void onScrollStart(@NonNull RecyclerView.ViewHolder currentItemHolder, int adapterPosition) {
        }

        @Override
        public void onScrollEnd(@NonNull RecyclerView.ViewHolder currentItemHolder, int adapterPosition) {
        }

        @Override
        public void onScroll(float scrollPosition, int currentPosition, int newPosition, @Nullable RecyclerView.ViewHolder currentHolder, @Nullable RecyclerView.ViewHolder newCurrent) {

        }
    };

    public class MytestAdpater<T> extends BaseQuickAdapter<T, BaseViewHolder> {
        public MytestAdpater(@Nullable List<T> data) {
            super(R.layout.more_heads_item, data);
        }

        public void upDataContect(List allDatas) {
            setNewData(allDatas);
        }

        @Override
        protected void convert(BaseViewHolder helper, T item) {

            ImageView iv_ic = helper.getView(R.id.iv_ic);
            Glide.with(mContext).load("")
                    .dontAnimate()
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.drawable.ic_launcher)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(iv_ic);
        }
    }
}
