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

public class ViewPagerAdapter2 extends PagerAdapter
{
    private Context context;
    private LayoutInflater layoutInflater;
    private int [] volks = {R.drawable.vw_golf,R.drawable.vw_passat, R.drawable.vw_tig, R.drawable.volkswagen_jetta};

    public ViewPagerAdapter2(Context context)
    {
        this.context = context;
    }

    @Override
    public int getCount()
    {
        return volks.length;
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
        imageView.setImageResource(volks[position]);

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
