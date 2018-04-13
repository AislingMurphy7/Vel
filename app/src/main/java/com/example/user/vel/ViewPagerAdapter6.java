package com.example.user.vel;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/*
    This class handles the Images found within the Toyota.class.
    This class allows the user to swipe left and right between
    the images
 */

public class ViewPagerAdapter6 extends PagerAdapter
{
    private Context context;
    private LayoutInflater layoutInflater;
    //Images included on the Toyota page
    private int [] toyota = {R.drawable.toyota_avensis,R.drawable.toyota_landcruiser, R.drawable.toy_yaris, R.drawable.toy_corolla};

    public ViewPagerAdapter6(Context context)
    {
        this.context = context;
    }//End ViewPagerAdapter()

    @Override
    //Get image count
    public int getCount()
    {
        return toyota.length;
    }//End count()

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }//End()

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_layout, null);
        ImageView imageView = view.findViewById(R.id.imageViewS);
        imageView.setImageResource(toyota[position]);

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
