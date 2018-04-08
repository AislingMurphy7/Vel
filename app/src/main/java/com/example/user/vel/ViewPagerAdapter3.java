package com.example.user.vel;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by user on 08/04/2018.
 */

public class ViewPagerAdapter3 extends PagerAdapter
{
    private Context context;
    private LayoutInflater layoutInflater;
    private int [] bmw = {R.drawable.bmw_1,R.drawable.bmw_3, R.drawable.bmw_x5, R.drawable.bmw_z4};

    public ViewPagerAdapter3(Context context)
    {
        this.context = context;
    }

    @Override
    public int getCount()
    {
        return bmw.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_layout, null);
        ImageView imageView = view.findViewById(R.id.imageViewS);
        imageView.setImageResource(bmw[position]);

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;
    }

    public void destroyItem(ViewGroup container, int position, Object object)
    {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}