package com.example.user.vel;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/*
    This class handles the Images found within the Ford.class.
    This class allows the user to swipe left and right between
    the images
 */

public class ViewPagerAdapter4 extends PagerAdapter
{
    private Context context;
    private LayoutInflater layoutInflater;
    //Images included on the Audi page
    private int [] ford = {R.drawable.ford_fie,R.drawable.ford_focus, R.drawable.ford_kuga, R.drawable.ford_mondeo};

    public ViewPagerAdapter4(Context context)
    {
        this.context = context;
    }//End ViewPagerAdapter()

    @Override
    //Get image count
    public int getCount()
    {
        return ford.length;
    }//End count()

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }//End()

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_layout, null);
        //XML variable
        ImageView imageView = view.findViewById(R.id.imageViewS);
        //Sets images into the imageView
        imageView.setImageResource(ford[position]);

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;
    }//End()

    public void destroyItem(ViewGroup container, int position, Object object)
    {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }//End()
}//End()
