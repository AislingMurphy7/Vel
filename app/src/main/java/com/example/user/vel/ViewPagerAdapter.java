package com.example.user.vel;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/*
    This class handles the Images found within the Audi.class.
    This class allows the user to swipe left and right between
    the images
 */

public class ViewPagerAdapter extends PagerAdapter
{
    private Context context;
    //Images included on the Audi page
    private int [] audi = {R.drawable.audi_a4,R.drawable.audi_a6, R.drawable.audi_tt, R.drawable.audi_q5};

    ViewPagerAdapter(Context context)
    {
        this.context = context;
    }//End ViewPagerAdapter()

    @Override
    //Get image count
    public int getCount()
    {
        return audi.length;
    }//End count()

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view == object;
    }//End()

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_layout, null);
        //XML variable
        ImageView imageView = view.findViewById(R.id.imageViewS);
        //Sets images into the imageView
        imageView.setImageResource(audi[position]);

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
