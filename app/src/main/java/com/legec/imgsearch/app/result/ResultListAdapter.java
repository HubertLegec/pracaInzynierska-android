package com.legec.imgsearch.app.result;
import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;


@EBean
public class ResultListAdapter extends BaseAdapter {

    private final List<ResultEntry> results = new ArrayList<>();

    @RootContext
    Context context;


    @Override
    public int getCount() {
        return results.size();
    }

    @Override
    public ResultEntry getItem(int position) {
        return results.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void addItem(ResultEntry entry){
        results.add(entry);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ResultItemView resultItemView;
        if (convertView == null) {
            resultItemView = ResultItemView_.build(context);
        } else {
            resultItemView = (ResultItemView) convertView;
        }
        ResultEntry entry = getItem(position);
        resultItemView.bind(entry);

        return resultItemView;
    }

}