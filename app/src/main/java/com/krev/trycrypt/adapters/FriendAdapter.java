package com.krev.trycrypt.adapters;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.krev.trycrypt.R;
import com.krev.trycrypt.server.model.entity.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Dima on 14.11.2016.
 */

public class FriendAdapter extends BaseAdapter {
    private final LayoutInflater layoutInflater;
    private List<User> users;
    private List<ImageView> imageViews;
    private AppCompatActivity activity;

    public FriendAdapter(List<User> users, AppCompatActivity activity) {
        this.users = users;
        this.imageViews = new ArrayList<>();
        this.layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.activity = activity;
        update();
    }

    private void update() {
        while (imageViews.size() < users.size()) {
            imageViews.add(null);
        }
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void addAll(Collection<User> users) {
        this.users.addAll(users);
        update();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public View getView(int position, View contentView, ViewGroup viewGroup) {
        ViewHolder holder = new ViewHolder();
        if (contentView == null) {
            contentView = layoutInflater.inflate(R.layout.user, viewGroup, false);
        }
        holder.icon = (ImageView) contentView.findViewById(R.id.user_icon);
        holder.name = (TextView) contentView.findViewById(R.id.user_name);
        User user = users.get(position);
        if (position >= imageViews.size()) {
            Log.d("FriendAdapter", imageViews.size() + " " + users.size());
            holder.name.setText(user.getName());
            contentView.setTag(holder);
            return contentView;
        }
        if (imageViews.get(position) == null) {
            imageViews.set(position, holder.icon);
//            try {
//                new DownloadImageTask(
//                        new Consumer<ImageView>() {
//                            @Override
//                            public void accept(ImageView o) {
//                                notifyDataSetChanged();
//                            }
//                        }).execute(user.getPhoto());
//            } catch (NullPointerException ignored) {
//            }
        } else {
            holder.icon.setImageBitmap(((BitmapDrawable) imageViews.get(position).getDrawable()).getBitmap());
        }
        holder.name.setText(user.getName());
        contentView.setTag(holder);
        return contentView;
    }

    private class ViewHolder {
        ImageView icon;
        TextView name;
    }
}
