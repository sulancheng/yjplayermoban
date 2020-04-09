package com.csu.xgum.widge;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 作者：sucheng on 2019/8/8 14:48
 */
public class MyNoScrollViewPager extends ViewPager {
    public MyNoScrollViewPager(Context context) {
        super(context);
    }

    public MyNoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public void setCurrentItem(int item) {
        //去除页面切换时的滑动翻页效果
        super.setCurrentItem(item, false);
    }
}

  /*  @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        getParent().requestDisallowInterceptTouchEvent(true);//父类不要拦截
        return super.dispatchTouchEvent(ev);
    }*/
    /*对于底层的View来说，有一种方法可以阻止父层的View截获touch事件，
    就是调用getParent().requestDisallowInterceptTouchEvent(true);方法。
    一旦底层View收到touch的action后调用这个方法那么父层View就不会再调用onInterceptTouchEvent了，也无法截获以后的action。*/
//注释viewpager的回收使用
//private List<ImageView> ivs;
//	private class MyAdapter extends PagerAdapter {
//		public MyAdapter() {
//			ivs=new ArrayList<ImageView>();
//		}
//		// 公有多少页
//		@Override
//		public int getCount() {
//			return imgs.length;
//		}
//
//		// instantiateItem的方法返回的是Object,系统要判断参数2返回的是否是一个view
//		// ViewPager的每个条目都有一个对应的ItemInfo,里面有position和对应的Object,Object就是instantiateItem返回的,
//		//系统会通过isViewFromObject来判断object是否是一个视图,如果是,则会返回条目信息,用于显示当前数据
//		@Override
//		public boolean isViewFromObject(View arg0, Object arg1) {
//			return arg0 == arg1;
//		}
//
//		// 初始化条目
//		// 参数1:ViewPager
//		// 参数2:对应条目的position
//		@Override
//		public Object instantiateItem(ViewGroup container, int position) {
//			if(ivs.isEmpty()){
//				ImageView iv=new ImageView(MainActivity.this);
//				ivs.add(iv);
//			}
//			Log.i("test", "集合大小:"+ivs.size());
//			ImageView iv = ivs.remove(0);
//			iv.setImageResource(imgs[position]);
//			container.addView(iv);
//			return iv;
//		}
//
//		// 销毁条目
//		@Override
//		public void destroyItem(ViewGroup container, int position, Object object) {
//			ImageView iv=(ImageView) object;
//			container.removeView(iv);
//			ivs.add(iv);
//		}
//	}

