package com.example.mypublib.widge;

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
    public boolean onInterceptTouchEvent(MotionEvent ev) {
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

/**
 * 总结：
       view viewgroup生命周期：：构造方法、onFinishInflate、onMeasure、onSizeChanged、onLayout、onDraw、dispatchDraw。
        viewgroup中ondraw不会调用  有dispathdraw
        viewgroup会调用：dispatchDraw();
        View绘制分三个步骤，顺序是：onMeasure，onLayout，onDraw。经代码亲测，log输出显示：调用invalidate（必须工作线程）方法postInvalidate（可以子线程）只会执行onDraw方法；
        调用requestLayout方法只会执行onMeasure方法和onLayout方法，并不会执行onDraw方法。所以当我们进行View更新时，若仅View的显示内容发生改变且新显示内容不影响View的大小、位置，
        则只需调用invalidate方法；若View宽高、位置发生改变且显示内容不变，只需调用requestLayout方法；
        若两者均发生改变，则需调用两者，按照View的绘制流程，推荐先调用requestLayout方法再调用invalidate方法。
    第一种在xml里直接引用的，执行顺序一般是：
        构造方法->onFinishInflate()(只执行一次)->onMeasure()(可能多次执行)->
        onSizeChanged()(在重新onMeasure的时候发现跟之前测量的尺寸不一样的时候就会回调此方法)
        ->onLayout()(布置子View)->onMeasure()->onLayout().......

        第二种在Activity中setContentView( newCustomView(this))引用的，执行顺序与第一种相比，
        除了构造方法引用的不一致和不执行onFinishInflate()外，其他基本一致。

        调用view.invalidate(),会触发onDraw和computeScroll()。前提是该view被附加在当前窗口上

        view.postInvalidate(); //是在非UI线程上调用的
 * */

