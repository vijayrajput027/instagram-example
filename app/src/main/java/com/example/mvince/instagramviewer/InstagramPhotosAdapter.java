package com.example.mvince.instagramviewer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;
public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {
    List<InstagramPhoto> photos;
    String tag;

    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> photos,String tag) {
        super(context, android.R.layout.simple_list_item_1, photos);
        this.photos=photos;
        this.tag=tag;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Take the data source at position (e.g. 0)
        // Get the data item
        final InstagramPhoto photo = getItem(position);

        // Check if we are using a recycled view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }

          ImageView imgPhoto = (ImageView) convertView.findViewById(R.id.imgPhoto);

        imgPhoto.setImageResource(0);
        Picasso.with(getContext()).load(photo.imageUrl).placeholder(R.drawable.instagram_glyph_on_white).into(imgPhoto);
        // Return the view for that data item

        imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),PhotoDetailActivity.class);
                intent.putExtra("image",photo.imageUrl);
                intent.putExtra("id",photo.username);
                intent.putExtra("tagename",tag);
                getContext().startActivity(intent);
            }
        });
        return convertView;
    }


    @Override
    public boolean isEnabled(int position) {
        // disables selection
        return false;
    }
}
