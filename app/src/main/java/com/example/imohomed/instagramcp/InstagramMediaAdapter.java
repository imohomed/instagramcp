package com.example.imohomed.instagramcp;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

/**
 * Created by i.mohomed on 12/6/15.
 */
public class InstagramMediaAdapter extends ArrayAdapter<InstagramMedia>{

    private Transformation transformation;
    public InstagramMediaAdapter(Context context, List<InstagramMedia> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(0)
                .cornerRadiusDp(30)
                .oval(false)
                .build();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InstagramMedia mediaItem = getItem(position);
        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_popular_media,parent,false);
        }
        TextView tvAuthor = (TextView) convertView.findViewById(R.id.tvAuthor);
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
        TextView tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);
        TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);
        ImageView ivProfilePhoto = (ImageView) convertView.findViewById(R.id.ivProfilePhoto);

        tvAuthor.setText(mediaItem.fullName);
        tvCaption.setText(mediaItem.author + ": " + mediaItem.caption);
        tvLikes.setText(mediaItem.likeCount + "");

        long now = System.currentTimeMillis();
//        Log.e("NOW",now + "");
//        Log.e("CREATED",mediaItem.createdTime);
        String timeStr = DateUtils.getRelativeTimeSpanString(Long.parseLong(mediaItem.createdTime) * 1000,now,0,DateUtils.FORMAT_ABBREV_RELATIVE).toString();
        tvTime.setText(timeStr);
        // Insert the image using Picaso
        ivPhoto.setImageResource(0);
        //Picasso.with(getContext()).load(mediaItem.mediaURL).into(ivPhoto);
        Picasso.with(getContext()).load(mediaItem.mediaURL).fit().centerCrop().placeholder(R.drawable.ic_texture_24dp).into(ivPhoto);
        Picasso.with(getContext()).load(mediaItem.profilePhoto).placeholder(R.drawable.ic_texture_24dp).transform(transformation).into(ivProfilePhoto);
Log.e("URL",mediaItem.profilePhoto);
        return convertView;
    }
}
