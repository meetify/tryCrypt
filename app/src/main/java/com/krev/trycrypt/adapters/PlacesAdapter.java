package com.krev.trycrypt.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.krev.trycrypt.R;
import com.krev.trycrypt.server.model.entity.Place;
import com.krev.trycrypt.utils.Consumer;
import com.krev.trycrypt.utils.DownloadImageTask;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Dima on 14.11.2016.
 */

public class PlacesAdapter extends BaseAdapter {
    private final LayoutInflater layoutInflater;
    private List<Place> places;
    private List<ImageView> imageViews;

    public PlacesAdapter(List<Place> places, Activity activity) {
        this.places = places;
        this.imageViews = Arrays.asList(new ImageView[this.places.size()]);
        this.layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return places.size();
    }

    @Override
    public Object getItem(int i) {
        return places.get(i);
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
        Place place = places.get(position);
        if (imageViews.get(position) == null) {
            imageViews.set(position, holder.icon);
            try {
                new DownloadImageTask(imageViews.get(position),
                        new Consumer<ImageView>() {
                            @Override
                            public void accept(ImageView o) {
                                notifyDataSetChanged();
                            }
                        }).execute(place.getPhoto());
            } catch (NullPointerException ignored) {
            }
        } else {
            holder.icon.setImageBitmap(((BitmapDrawable) imageViews.get(position).getDrawable()).getBitmap());
        }
        holder.name.setText(place.getName());
        contentView.setTag(holder);
        return contentView;
    }

    private class ViewHolder {
        ImageView icon;
        TextView name;
    }
}
