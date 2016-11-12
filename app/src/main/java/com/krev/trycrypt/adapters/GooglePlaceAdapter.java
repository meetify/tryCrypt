package com.krev.trycrypt.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.krev.trycrypt.R;
import com.krev.trycrypt.asynctasks.Consumer;
import com.krev.trycrypt.asynctasks.DownloadImageTask;
import com.krev.trycrypt.model.GooglePlace;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Dima on 28.10.2016.
 */
public class GooglePlaceAdapter extends BaseAdapter {
    private static final String TAG = GooglePlaceAdapter.class.toString();
    private List<GooglePlace.Result> results;
    private List<ImageView> imageViews;
    private LayoutInflater layoutInflater;

    public GooglePlaceAdapter(GooglePlace googlePlace, Activity activity) {
        this.results = googlePlace.getResults();
        this.imageViews = Arrays.asList(new ImageView[results.size()]);
        this.layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return results.size();
    }

    @Override
    public Object getItem(int i) {
        return results.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int position, View contentView, ViewGroup viewGroup) {
        ViewHolder holder = new ViewHolder();
        if (contentView == null) {
            contentView = layoutInflater.inflate(R.layout.place, viewGroup, false);
        }
        holder.icon = (ImageView) contentView.findViewById(R.id.place_icon);
        holder.name = (TextView) contentView.findViewById(R.id.place_name);
        holder.address = (TextView) contentView.findViewById(R.id.place_address);
        holder.types = (TextView) contentView.findViewById(R.id.place_types);
        Log.d(TAG, "getView: " + position);
        GooglePlace.Result place = results.get(position);
        if (imageViews.get(position) == null) {
            imageViews.set(position, holder.icon);
            try {
                new DownloadImageTask(imageViews.get(position),
                        new Consumer<Object>() {
                            @Override
                            public void accept(Object o) {
                                notifyDataSetChanged();
                            }
                        }).execute(place.getPhotos().get(0).getPhotoReference());
            } catch (NullPointerException ignored) {
            }
        } else {
            holder.icon.setImageBitmap(((BitmapDrawable) imageViews.get(position).getDrawable()).getBitmap());
        }
        holder.name.setText(place.getName());
        holder.address.setText(place.getVicinity());
        holder.types.setText(place.getTypes().toString());
        contentView.setTag(holder);
        return contentView;
    }

    private class ViewHolder {
        ImageView icon;
        TextView name;
        TextView address;
        TextView types;
    }
}