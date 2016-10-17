package com.example.ramrooter.popularmovies;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ram Rooter on 10/16/2016.
 */

public class MovieFetcher {
    private static final String TAG = "MovieFetcher";
    private static final String API_KEY = "9e569a6b83b9f50cd24ba1789431722e";

    public byte[] getUrlBytes(String urlSpec) throws IOException{
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        try{
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK){
                throw new IOException(connection.getResponseMessage() + " :with"+ urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while((bytesRead = in.read(buffer))>0){
                out.write(buffer,0,bytesRead);
            }

            out.close();
            return out.toByteArray();
        }

        finally{
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException{
        return new String(getUrlBytes(urlSpec));
    }

    public List<Movie> fetchItems(){
       List<Movie> movies = new ArrayList<>();
        try{
            String url = Uri.parse("http://api.themoviedb.org/3/discover/movie")
                    .buildUpon()
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("language","en-us")
                    .appendQueryParameter("sort_by","popularity.desc")
                    .build()
                    .toString();
            String jsonString = getUrlString(url);
            Log.i(TAG, "Recieved JSON: "+ jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseItems(movies, jsonBody);
        }

        catch(JSONException je){
            Log.e(TAG,"Failed to parse JSON", je);
        }

        catch(IOException ioe){
            Log.e(TAG, "Failed to fetch items", ioe);
        }
        return movies;
    }

    private void parseItems(List<Movie>movies, JSONObject jsonBody) throws IOException, JSONException{
        JSONObject backDropPathJsonObject = jsonBody.getJSONObject("results");
        JSONArray photoJSONArray = backDropPathJsonObject.getJSONArray("results");

        for(int i=0; i<photoJSONArray.length();i++){
            backDropPathJsonObject =photoJSONArray.getJSONObject(i);

            Movie movie = new Movie();
            movie.setTitle(backDropPathJsonObject.getString("title"));
            movie.setCaption(backDropPathJsonObject.getString("overview"));

            //if(!backDropPathJsonObject.has("url_s")){
            //    continue;
            //}

            //movie.setUrl(backDropPathJsonObject.getString("url_s"));
            movies.add(movie);
        }
    }
}
