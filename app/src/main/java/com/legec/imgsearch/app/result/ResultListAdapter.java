package com.legec.imgsearch.app.result;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;


@EBean
public class ResultListAdapter extends BaseAdapter {

    private final List<ResultEntry> results = new ArrayList<>();
    private final Map<String, Bitmap> images = new HashMap<>();
    private final Semaphore mImageSemaphore = new Semaphore(1);

    private static final int IMAGE_RECEIVED = 0;

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
        resultItemView.bind(entry, images.get(entry.urlImage), mImageSemaphore);

        return resultItemView;
    }

    @Background
    public void getImages() {
        for (ResultEntry entry : results) {

            String url = entry.urlImage;
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
            try {
                InputStream in = new java.net.URL(url).openStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = in.read(buffer)) != -1)
                    byteBuffer.write(buffer, 0, len);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            entry.imageData = byteBuffer.toByteArray();

            try {
                Bitmap bitmap = BitmapFactory.decodeByteArray(entry.imageData,
                        0, entry.imageData.length);
                mImageSemaphore.acquire();
                images.put(entry.urlImage, bitmap);
                mImageSemaphore.release();
                mHandler.sendEmptyMessage(IMAGE_RECEIVED);
            } catch (NullPointerException e) {
                Log.e("ResultListAdapter", "Image " + entry.id + " could not be loaded.");
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case IMAGE_RECEIVED:
                    ResultListAdapter.this.notifyDataSetChanged();
                    break;
            }
        }
    };

}