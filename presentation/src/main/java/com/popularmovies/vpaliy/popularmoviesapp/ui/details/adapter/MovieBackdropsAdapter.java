package com.popularmovies.vpaliy.popularmoviesapp.ui.details.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.popularmovies.vpaliy.popularmoviesapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.support.annotation.NonNull;
import android.widget.ProgressBar;

import butterknife.ButterKnife;

public class MovieBackdropsAdapter extends PagerAdapter{

    private List<String> movieBackdrops;
    private LayoutInflater inflater;
    private volatile boolean isLoaded=false;
    private Callback callback;

    public MovieBackdropsAdapter(Context context){
        this.movieBackdrops=new ArrayList<>();
        this.inflater=LayoutInflater.from(context);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        View view=inflater.inflate(R.layout.adapter_backdrop_item,container,false);
        ImageView image= ButterKnife.findById(view,R.id.backdropImage);
        ProgressBar progressBar=ButterKnife.findById(view,R.id.progressBar);

        Glide.with(container.getContext())
                .load(movieBackdrops.get(position))
                .asBitmap()
                .priority(Priority.IMMEDIATE)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(new ImageViewTarget<Bitmap>(image) {
                          @Override
                          protected void setResource(Bitmap resource) {
                              image.setImageBitmap(resource);
                              progressBar.setVisibility(View.GONE);
                              if (position == 0 && !isLoaded) {
                                  isLoaded = true;
                                  if (callback != null) {
                                      callback.onTransitionImageLoaded(image,resource);
                                  }
                              }
                          }
                      });
        container.addView(view);
        return view;
    }

    public void appendData(List<String> data){
        this.movieBackdrops=data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(movieBackdrops.size()>=5) return 5;
        return movieBackdrops.size();
    }

    public void setData(@NonNull List<String> backdrops){
        this.movieBackdrops=backdrops;
        notifyDataSetChanged();
    }

    public void setPoster(String poster){
        if(poster!=null){
            movieBackdrops= Collections.singletonList(poster);
            notifyDataSetChanged();
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view=View.class.cast(object);
        container.removeView(view);
    }

    public interface Callback {
        void onTransitionImageLoaded(ImageView image, Bitmap bitmap);
    }
}
