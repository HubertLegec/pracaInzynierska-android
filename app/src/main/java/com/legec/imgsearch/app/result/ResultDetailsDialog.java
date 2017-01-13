package com.legec.imgsearch.app.result;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.legec.imgsearch.app.R;
import com.legec.imgsearch.app.restConnection.dto.ImageDetails;

public class ResultDetailsDialog {
    public static final int SHOW_BUTTON = -1;
    private AlertDialog dialog;

    public ResultDetailsDialog(ImageDetails details, Activity activity, DetailsDialogCallback callback) {
        AlertDialog.Builder builder = getBuilder(activity, callback);
        dialog = builder.create();
        View dialogLayout = activity.getLayoutInflater().inflate(R.layout.result_details, null);
        ProgressBar progressBar = (ProgressBar) dialogLayout.findViewById(R.id.detailsProgress);
        dialog.setView(dialogLayout);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setOnShowDialog(details, activity, progressBar);
    }

    private AlertDialog.Builder getBuilder(Context context, final DetailsDialogCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                callback.onDialogClose(which);
            }
        };
        return builder.setPositiveButton("Show original", listener)
                .setNegativeButton("Back", listener);
    }

    private void setOnShowDialog(final ImageDetails details, final Context context, final ProgressBar progress) {
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface d) {
                ImageView image = (ImageView) dialog.findViewById(R.id.image);
                Glide.with(context)
                        .load(details.getUrl())
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                progress.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                progress.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .fitCenter()
                        .crossFade()
                        .into(image);
            }
        });
    }

    public void show() {
        dialog.show();
    }
}
