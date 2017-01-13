package com.legec.imgsearch.app.result;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.legec.imgsearch.app.restConnection.dto.ImageDetails;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@EBean
public class ResultListAdapter extends BaseAdapter {
    private final List<ImageDetails> results = new ArrayList<>();
    @RootContext
    Activity context;


    @Override
    public int getCount() {
        return results.size();
    }

    @Override
    public ImageDetails getItem(int position) {
        return results.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void addAll(Collection<ImageDetails> entries) { results.addAll(entries); }

    public void clear(){
        results.clear();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ResultItemView resultItemView;
        if (convertView == null) {
            resultItemView = ResultItemView_.build(context);
        } else {
            resultItemView = (ResultItemView) convertView;
        }
        ImageDetails entry = getItem(position);
        resultItemView.bind(entry);

        Glide.with(context)
                .load(entry.getUrl())
                .centerCrop()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        resultItemView.getProgress().setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        resultItemView.getProgress().setVisibility(View.GONE);
                        return false;
                    }
                })
                .crossFade()
                .into(resultItemView.imageView);

        return resultItemView;
    }

}