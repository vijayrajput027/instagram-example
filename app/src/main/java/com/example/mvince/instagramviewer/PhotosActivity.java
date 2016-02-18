package com.example.mvince.instagramviewer;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import android.graphics.Bitmap;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URL;
import java.util.ArrayList;


public class PhotosActivity extends ActionBarActivity {
    public static final String CLIENT_ID = "c8e2cde3f35d402687512d9004ee7b12";
    //new
    //public static final String CLIENT_ID ="c7828b72107344a99f6954a700f3278f";
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdapter aPhotos;
    private SwipeRefreshLayout swipeContainer;
    private EditText editText;
    private Button button;
    String imagename;
    Bitmap bitmapImage;
    URL url;
    ImageStorage imageStorage;
    String username,result;
    GridView gridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        editText=(EditText)findViewById(R.id.search);
        button=(Button)findViewById(R.id.submit);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //fetchPopularPhotos();
            }
        });
        imageStorage=new ImageStorage();
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result = editText.getText().toString();
                fetchPopularPhotos(result);
            }
        });
    }



    private void fetchPopularPhotos(String hashtag) {

        photos = new ArrayList<InstagramPhoto>(); // initialize arraylist
        aPhotos = new InstagramPhotosAdapter(this, photos,result);
         gridview=(GridView)findViewById(R.id.gridView1);
        gridview.setAdapter(aPhotos);
        String popularUrl="https://api.instagram.com/v1/tags/"+hashtag+"/media/recent?client_id="+ CLIENT_ID;
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(popularUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                JSONArray photosJSON = null;
                JSONArray commentsJSON = null;

                try {
                    photos.clear();
                    photosJSON = response.getJSONArray("data");

                    for (int i = 0; i < photosJSON.length(); i++) {
                        JSONObject photoJSON = photosJSON.getJSONObject(i); // 1, 2, 3, 4
                        InstagramPhoto photo = new InstagramPhoto();
                        photo.username = photoJSON.getJSONObject("user").getString("username");
                        username=photo.username;
                        photo.imageUrl = photoJSON.getJSONObject("images").getJSONObject("low_resolution").getString("url");
                        photo.id = photoJSON.getString("id");

                        photos.add(photo);
                    }
                    aPhotos.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

    }


}
