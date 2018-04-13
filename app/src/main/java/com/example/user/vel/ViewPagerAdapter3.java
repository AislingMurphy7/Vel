package com.example.user.vel;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/*
    This class handles the Images found within the BMW.class.
    This class allows the user to swipe left and right between
    the images
 */


public class ViewPagerAdapter3 extends PagerAdapter
{
    private Context context;
    private LayoutInflater layoutInflater;
    //Images included on the BMW page
    private int [] bmw = {R.drawable.bmw_1,R.drawable.bmw_3, R.drawable.bmw_x5, R.drawable.bmw_z4};

    public ViewPagerAdapter3(Context context)
    {
        this.context = context;
    }//End ViewPagerAdapter()

    @Override
    //Get image count
    public int getCount()
    {
        return bmw.length;
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
        imageView.setImageResource(bmw[position]);

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
