package com.example.user.vel;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/*
    This class handles the Images found within the Renault.class.
    This class allows the user to swipe left and right between
    the images
 */

public class ViewPagerAdapter5 extends PagerAdapter
{
    private Context context;
    private LayoutInflater layoutInflater;
    //Images included on the Renault page
    private int [] ren = {R.drawable.renault_fluence,R.drawable.ren_clio, R.drawable.ren_kad, R.drawable.ren_megane};

    public ViewPagerAdapter5(Context context)
    {
        this.context = context;
    }//End ViewPagerAdapter()

    @Override
    //Get image count
    public int getCount()
    {
        return ren.length;
    }//End count()

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view == object;
    }//End()

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_layout, null);
        ImageView imageView = view.findViewById(R.id.imageViewS);
        imageView.setImageResource(ren[position]);

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
