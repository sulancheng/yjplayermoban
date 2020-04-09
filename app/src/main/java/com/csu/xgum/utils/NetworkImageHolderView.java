package com.csu.xgum.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by Sai on 15/8/4.
 * 网络图片加载例子
 */
public class NetworkImageHolderView<T> implements Holder<T> {
    private ImageView imageView;

    @Override
    public View createView(Context context) {
        //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
        imageView = new ImageView(context);
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, T data) {
        try {
            Glide.with(context)
                    .load(OkutilsObserver.img_f_path + (String) data)
                    .dontAnimate()
//                    .placeholder(R.drawable.lunbo_test)
//                    .error(R.drawable.lunbo_test)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        try {
//            LunboBean.DataBean dataBean = (LunboBean.DataBean) data;
//            //imageView.setImageResource(R.drawable.login_box);
//            //使用缓存策略

//        } catch (Exception e) {
//            e.printStackTrace();
//            Glide.with(context)
//                    .load("")
//                    .dontAnimate()
//                    .placeholder(R.drawable.lunbo_test)
//                    .error(R.drawable.lunbo_test)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(imageView);
//        }

    }
}
