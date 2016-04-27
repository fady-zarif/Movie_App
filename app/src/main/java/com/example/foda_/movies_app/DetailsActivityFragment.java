package com.example.foda_.movies_app;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {
    TextView text1,date,vote,movie_title;
    TextView trailer,review;
    Button favoriet;
    public ArrayList<String> arrayList_Movie_keys=new ArrayList<String>();// to store the keys of Trailers
    public ArrayList<String> arrayList_Movie_Reviews=new ArrayList<String>();// to store the content of reviews
    ImageView poster_image;
    videos_adapter adapter;
    ListView listView;
    public Movies favoriet_movie_object;

    public DetailsActivityFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        arrayList_Movie_keys.clear();           // clear all data of arrayList_Movie_keys
        arrayList_Movie_Reviews.clear();        // clear all data of arrayList_Movie_Reviews
        Fetch_Video fetch_video=new Fetch_Video();
        String Id=getArguments().getString("Id"); // get the Id from the Mainactivity and store it into variable id
        fetch_video.execute("http://api.themoviedb.org/3/movie/" + Id + "/videos"); // passing the url of trailers in Asynctask
        Fetch_Reviews  fetch_reviews=new Fetch_Reviews();
        fetch_reviews.execute("http://api.themoviedb.org/3/movie/" + Id + "/reviews"); // passing the url of reviews in Asynctask
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        boolean x=true;
        ProgressDialog progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Loading ... ");

        String Id=getArguments().getString("Id");
        if (!Id.equals("0000"))
        {progressDialog.show();
            inflater.inflate(R.menu.share_button, menu);
            MenuItem menuItem = menu.findItem(R.id.action_provider);
            while(x==true)
            {
                if (arrayList_Movie_keys.size()!=0)
                { x=false;
                  progressDialog.dismiss();
                }
            } /// to check the arraylist of movies because i will share the first video link

            ShareActionProvider mShareActionProvider =
                    (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

            if (mShareActionProvider != null ) {
                mShareActionProvider.setShareIntent(createShareForecastIntent());
            }
        }
    }
    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        String key = arrayList_Movie_keys.get(0);
        String Url= "http://www.youtube.com/watch?v=" + key;
        shareIntent.putExtra(Intent.EXTRA_TEXT,Url);
        return shareIntent;
    }
    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.fragment_details, container, false);
        final String overview=getArguments().getString("data").toString();          /// get all data from Arguments that recived From the  main activity
        final String released_date=getArguments().getString("released_date").toString();
        final String title=getArguments().getString("title").toString();
        final String vote_average=getArguments().getString("vote_average").toString();
        final String poster=getArguments().getString("poster").toString();
        String Id=getArguments().getString("Id");
        if (Id.equals("0000"))               /// if Id =0000 so it means that it will be in offline mode
        {
        trailer=(TextView)root.findViewById(R.id.Trailers_word);
        review=(TextView)root.findViewById(R.id.Review_word);
            trailer.setText("");
            review.setText("");
        }
        text1=(TextView)root.findViewById(R.id.details_textview);
        movie_title=(TextView)root.findViewById(R.id.Movie_Title);
        date=(TextView)root.findViewById(R.id.released_date);
        vote=(TextView)root.findViewById(R.id.vote);
        poster_image=(ImageView)root.findViewById(R.id.details_image);
        movie_title.setText(title);
        text1.setText(overview);
        date.setText(released_date.substring(0,4));
        vote.setText(vote_average + " /10");
        Picasso.with(getActivity())
                .load(poster)
                .into(poster_image);

        favoriet=(Button)root.findViewById(R.id.Favoriet_Button);
            if (exist(title)==1) /// it will Search for title  in the database  if it return true that is mean that is already in the database
        {
            favoriet.setBackgroundResource(android.R.drawable.star_big_on); // turn on the Star
        }
        else
           {
               favoriet.setBackgroundResource(android.R.drawable.star_big_off); // Star off
           }
            favoriet_movie_object=new Movies(poster,overview,released_date,title,vote_average); // make object from Movies  class to store the movie data
            favoriet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // when favoriet Button clicked it will search for the title in the database

                if (exist(title)==1) // if it return 1 it is mean that the movie exist so
                {                   // it will show message that the movie is already saved to his Favoriet
                    Toast.makeText(getContext(), "The Movie already Exists ", Toast.LENGTH_LONG).show();
                }
                else
                     {

                         Toast.makeText(getContext(), " Saving....", Toast.LENGTH_SHORT).show();
                         ContentValues detailsvalues = new ContentValues();
                         detailsvalues.put(MoviesProvider.ORIGINAL_TITLE,
                                 title);
                         detailsvalues.put(MoviesProvider.POSTER,
                                 poster);
                         detailsvalues.put(MoviesProvider.DATE,
                                 released_date.substring(0, 4));
                         detailsvalues.put(MoviesProvider.RATE,
                                 vote_average);
                         detailsvalues.put(MoviesProvider.OVERVIEW,
                                 overview);
                         Uri uri = getActivity().getContentResolver().insert(
                                 MoviesProvider.CONTENT_URI, detailsvalues);
                         Toast.makeText(getContext(), " successfully saved  ", Toast.LENGTH_SHORT).show();
                         favoriet.setBackgroundResource(android.R.drawable.star_big_on);
                    }
                }
        });
    return root;}

//// Method for searching in the database for the title of the movie
    public int exist(String Title)
    {   int flag=0;
        String URL = "content://com.movies.provider.movies/details";
        Uri movies = Uri.parse(URL);
        Cursor cursor = getActivity().getContentResolver().query(movies, null, null, null, "Id");
        if (!cursor.moveToFirst()) {
            flag=0;
        } else {
            do {
                if (Title.equals(cursor.getString(cursor.getColumnIndex(MoviesProvider.ORIGINAL_TITLE)))) {
                    flag = 1;
                    return flag;
                }
            } while (cursor.moveToNext());
        }
        return flag;
    }//end of the method



public class Fetch_Video extends AsyncTask<String, Integer, ArrayList<String>> {
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;
    String JsonStr = null;
    @Override
    protected ArrayList<String> doInBackground(String... params) {
        URL url = null;
        // implementing try and catch to handle error
        try {
            // make a connection
            String BaseUrl = params[0];
            String apikey="?api_key="+BuildConfig.OPEN_Movie_API_KEY;
            String x=BaseUrl.concat(apikey);
            url = new URL(BaseUrl.concat(apikey));
            Log.e("whole Url is : ", x);
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                return null;
            }
            JsonStr = buffer.toString(); // All json string
            // parsing json
            JSONObject jsonObject=new JSONObject(JsonStr);
            JSONArray jsonArray=jsonObject.getJSONArray("results");

            for ( int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject2=jsonArray.getJSONObject(i);
                String key=jsonObject2.getString("key");
                arrayList_Movie_keys.add(key);
            }
           //////////////////////////////////////////////

            publishProgress(arrayList_Movie_keys.size());
            Log.e("x is",JsonStr);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("Error", "Error closing stream", e);
                }
            }
        }
        return arrayList_Movie_keys;
    }
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);// will return arraylist size
        int[] array=new int[values[0]];
        for (int i=0;i<array.length;i++)
        {
            array[i]=R.drawable.youtube2; // making array to store images with the same size of arraylist element
        }
        listView=(ListView)getActivity().findViewById(R.id.list);
        adapter=new videos_adapter(getActivity(),array);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        Hight_of_list.setListViewHeightBasedOnItems(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String key = arrayList_Movie_keys.get(position);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + key))); // start new intent for trailer
            }
        });
    }
}
    public class  Fetch_Reviews extends  AsyncTask<String,Integer,ArrayList<String>>
    {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String JsonStr = null;
        @Override
        protected ArrayList<String> doInBackground(String... params) {
            URL url = null;
            // implementing try and catch to handle error
            try {
                // make a connection
                String BaseUrl = params[0];
                String apikey="?api_key="+BuildConfig.OPEN_Movie_API_KEY;
                String x=BaseUrl.concat(apikey);
                url = new URL(BaseUrl.concat(apikey));
                Log.e("whole Url is : ", x);
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    return null;
                }
                JsonStr = buffer.toString(); // All json string
                // parsing json
                JSONObject jsonObject=new JSONObject(JsonStr);
                JSONArray jsonArray=jsonObject.getJSONArray("results");
                arrayList_Movie_Reviews.clear();
                for ( int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject2=jsonArray.getJSONObject(i);
                    String Review=jsonObject2.getString("content");
                    arrayList_Movie_Reviews.add(Review);
                }
                //////////////////////////////////////////////
                publishProgress(arrayList_Movie_Reviews.size());
                Log.e("x is",JsonStr);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("Error", "Error closing stream", e);
                    }
                }
            }
            return arrayList_Movie_Reviews;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            LinearLayout linearLayout=(LinearLayout)getActivity().findViewById(R.id.linear);
            TextView textView ;
            ImageView imageView;
             for (int i=0; i<values[0];i++) {
             LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
             ViewGroup.LayoutParams.WRAP_CONTENT);
             params.setMargins(30, 20, 0, 30);
             textView=new TextView(getActivity());
             textView.setText("" + arrayList_Movie_Reviews.get(i));
             textView.setTextSize(16);
             imageView=new ImageView(getActivity());
             imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 10));
             imageView.setBackgroundColor(Color.BLACK);
             textView.setLayoutParams(params);
             linearLayout.addView(textView);
             linearLayout.addView(imageView);
         }
        }
    }
}
