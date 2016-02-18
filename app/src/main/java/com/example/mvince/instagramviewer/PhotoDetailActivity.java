package com.example.mvince.instagramviewer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class PhotoDetailActivity extends ActionBarActivity {

    ApplicationDatabaseHelper applicationDatabaseHelper;
    String picturePath,hashtag,imagename;
     Bitmap bitmapImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        Intent intent = getIntent();
        String username=intent.getStringExtra("id");
        String image = intent.getStringExtra("image");
        hashtag=intent.getStringExtra("tagename");

        try {
            imagename = image.substring(image.lastIndexOf("/") + 1, image.lastIndexOf('?'));
            String imageurl =image.substring(0, image.lastIndexOf('?'));
            URL url = new URL(imageurl);
            bitmapImage = BitmapFactory.decodeStream(url.openConnection().getInputStream());;

        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        applicationDatabaseHelper=new ApplicationDatabaseHelper(getApplicationContext());

        if (!applicationDatabaseHelper.checkCommodityImage(username)) {
            applicationDatabaseHelper.saveToSdCard(bitmapImage,imagename,username);
        }

        ImageView imageView = (ImageView) findViewById(R.id.imagfe);
        Button btnshare=(Button)findViewById(R.id.share);

        applicationDatabaseHelper=new ApplicationDatabaseHelper(getApplicationContext());
        picturePath = applicationDatabaseHelper.getImagePath(username);

        Picasso.with(getApplicationContext()).load(image).placeholder(R.drawable.instagram_glyph_on_white).into(imageView);

        btnshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String type = "image/*";
                createInstagramIntent(type, picturePath);

            }
        });

    }

    private void createInstagramIntent(String type, String mediaPath) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType(type);
        share.setType("message/rfc822");
        File media = new File(mediaPath);
        Uri uri = Uri.fromFile(media);
        share.putExtra(Intent.EXTRA_SUBJECT, hashtag);
        share.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(share, "Share to"));
    }
}
