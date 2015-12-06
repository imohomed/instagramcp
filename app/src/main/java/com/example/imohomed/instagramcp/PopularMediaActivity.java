package com.example.imohomed.instagramcp;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class PopularMediaActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeContainer;
    public static final String CLIENT_ID = "e05c462ebd86446ea48a5af73769b602";
    public static final String TAG = "PopularMediaActivity";
    private ArrayList<InstagramMedia> mediaItems;
    private InstagramMediaAdapter aMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_media);
        mediaItems = new ArrayList<>();

        // Create adapter and link to source
        aMedia = new InstagramMediaAdapter(this,mediaItems);
        ListView lvMedia = (ListView) findViewById(R.id.lvMedia);
        lvMedia.setAdapter(aMedia);

        // Send out a test request
        //fetchPopularMedia();

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        fetchTimelineAsync(0);
    }

    public void fetchTimelineAsync(int page) {
        AsyncHttpClient client = new AsyncHttpClient();
        final String urlForPopularMedia = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        client.get(urlForPopularMedia, new JsonHttpResponseHandler()
        {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray mediaJSON = null;
                aMedia.clear();
                try{
                    mediaJSON = response.getJSONArray("data");
                    Log.e(TAG, mediaJSON.length() + "");

                    for(int i=0;i<mediaJSON.length();i++)
                    {
                        // Get JSON object for media item
                        JSONObject singleMediaJSON = mediaJSON.getJSONObject(i);
                        if (singleMediaJSON.getString("type").equals("image")) {
                            InstagramMedia media = new InstagramMedia();
                            try {
                                media.author = singleMediaJSON.getJSONObject("user").getString("username");
                                media.caption = singleMediaJSON.getJSONObject("caption").getString("text");
                                media.mediaURL = singleMediaJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                                media.mediaHeight = singleMediaJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                                media.likeCount = singleMediaJSON.getJSONObject("likes").getInt("count");
                                media.createdTime = singleMediaJSON.getString("created_time");
                                media.profilePhoto = singleMediaJSON.getJSONObject("user").getString("profile_picture");
                                media.fullName = singleMediaJSON.getJSONObject("user").getString("full_name");
                                mediaItems.add(media);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                // ignore individual errors
                                Log.e(TAG,"ERROR PROCESSING JSON OBJECT!");
                            }
                        }
                    }
                }catch(JSONException e)
                {
                    e.printStackTrace();
                }
                // List of media was updated
                //aMedia.notifyDataSetChanged();
                aMedia.addAll(mediaItems);
                swipeContainer.setRefreshing(false);
            }

//            public void onSuccess(JSONArray json) {
//                // Remember to CLEAR OUT old items before appending in the new ones
//                adapter.clear();
//                // ...the data has come back, add new items to your adapter...
//                adapter.addAll(...);
//                // Now we call setRefreshing(false) to signal refresh has finished
//                swipeContainer.setRefreshing(false);
//            }

            public void onFailure(Throwable e) {
                Log.d("DEBUG", "Fetch timeline error: " + e.toString());
            }
        });


    }

    public void fetchPopularMedia()
    {
        AsyncHttpClient client = new AsyncHttpClient();
        final String urlForPopularMedia = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        client.get(urlForPopularMedia,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray mediaJSON = null;
                try{
                    mediaJSON = response.getJSONArray("data");
                    Log.e(TAG, mediaJSON.length() + "");

                    for(int i=0;i<mediaJSON.length();i++)
                    {
                        // Get JSON object for media item
                        JSONObject singleMediaJSON = mediaJSON.getJSONObject(i);
                        if (singleMediaJSON.getString("type").equals("image")) {
                            InstagramMedia media = new InstagramMedia();
                            try {
                                media.author = singleMediaJSON.getJSONObject("user").getString("username");
                                media.caption = singleMediaJSON.getJSONObject("caption").getString("text");
                                media.mediaURL = singleMediaJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                                media.mediaHeight = singleMediaJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                                media.likeCount = singleMediaJSON.getJSONObject("likes").getInt("count");
                                media.createdTime = singleMediaJSON.getString("created_time");
                                media.profilePhoto = singleMediaJSON.getJSONObject("user").getString("profile_picture");
                                media.fullName = singleMediaJSON.getJSONObject("user").getString("full_name");
                                mediaItems.add(media);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                // ignore individual errors
                                Log.e(TAG,"ERROR PROCESSING JSON OBJECT!");
                            }
                        }
                    }
                }catch(JSONException e)
                {
                    e.printStackTrace();
                }
                // List of media was updated
                aMedia.notifyDataSetChanged();
            }
        });
    }

}
