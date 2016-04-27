package com.example.foda_.movies_app;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

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
public class MainActivityFragment extends Fragment {
public String y=null;


    private ArrayList<Movies> movie_data=new ArrayList<Movies>();
    private ArrayList<Movies> array_list_favoiret=new ArrayList<Movies>();
    public Movies movie_object;
    private ImageAdapter imageAdapter ;
    GridView gridView;
    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.movies_order, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       int id=item.getItemId();
        if (id==R.id.original)
        {
            FetchMovie Movie=new FetchMovie();
            Movie.execute("http://api.themoviedb.org/3/movie/upcoming");
            y="http://api.themoviedb.org/3/movie/upcoming";
            if (MainActivity.istablet)
            {
                Fragment fragment=new Fragment();
                getFragmentManager().beginTransaction().replace(R.id.Second_Framelayout_tablet, fragment).commit();

            }
        }
        else if (id==R.id.popular_movies)
        {
            FetchMovie Movie=new FetchMovie();
            Movie.execute("http://api.themoviedb.org/3/movie/popular");
            y="http://api.themoviedb.org/3/movie/popular";
            if (MainActivity.istablet)
            {
                Fragment fragment=new Fragment();
                getFragmentManager().beginTransaction().replace(R.id.Second_Framelayout_tablet, fragment).commit();

            }
        }
        else if (id==R.id.Top_rated)
        {
            FetchMovie Movie=new FetchMovie();
            Movie.execute("http://api.themoviedb.org/3/movie/top_rated");
            y="http://api.themoviedb.org/3/movie/top_rated";
            if (MainActivity.istablet)
            {
                Fragment fragment=new Fragment();
                getFragmentManager().beginTransaction().replace(R.id.Second_Framelayout_tablet, fragment).commit();

            }

        }
        else if(id==R.id.Favorit_setting)
        {
          retrieve_data();
            y="1";
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
         super.onStart();
        FetchMovie Movie=new FetchMovie();
      // because when i move to detail activity of top rated and go back return to the first activity not
        // the top rated
       if (y=="1")
       {
           retrieve_data();
       }
        else if (y!=null) {

            Movie.execute(y);
        }
        else
            Movie.execute("http://api.themoviedb.org/3/discover/movie");
        }
    public int retrieve_data()
    {
        String URL = "content://com.movies.provider.movies/details";
        Uri movies = Uri.parse(URL);
        Cursor c = getActivity().getContentResolver().query(movies, null, null, null, "Id");   array_list_favoiret.clear();
        ImageAdapter favourit_adapter;
        Movies movie;
        if (c.getCount()==0)
        {
            return -1;
        }
        else
        {
            while (c.moveToNext()) {
                String poster=c.getString(1).toString();
                String overview = c.getString(5).toString();
                String  date=c.getString(4).toString();
                String title=c.getString(2).toString();
                String rate= c.getString(3).toString();
                Log.e("poster is",poster);
                Log.e("datae is",date);
                Log.e("overview is",overview);
                Log.e("title is",title);
                Log.e("rate is", rate);
                movie =new Movies(poster,overview,date,title,rate);
                 array_list_favoiret.add(movie);
            }
            favourit_adapter=new ImageAdapter(getContext(),array_list_favoiret);
            gridView.setAdapter(favourit_adapter);
            if (MainActivity.istablet)
            {
                Fragment fragment=new Fragment();
                getFragmentManager().beginTransaction().replace(R.id.Second_Framelayout_tablet, fragment).commit();

            }
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override // when item clicked will take all the data of this item and send it to the details fragment
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String overview = array_list_favoiret.get(position).overview;
                    String title = array_list_favoiret.get(position).original_title;
                    String release_date = array_list_favoiret.get(position).release_date;
                    String poster = array_list_favoiret.get(position).JsonPath;
                    String vote_average = array_list_favoiret.get(position).vote_average;

                    //favourite
                    DetailsActivityFragment fragment=new DetailsActivityFragment();
                    Bundle extra=new Bundle();
                    extra.putString("data", overview);
                    extra.putString("title", title);
                    extra.putString("released_date", release_date);
                    extra.putString("poster", poster);
                    extra.putString("vote_average", vote_average);
                    extra.putString("Id", "0000"); // if the movie seeing in offline mode
                    fragment.setArguments(extra);
                    FragmentManager  manager=getFragmentManager();
                    if (MainActivity.istablet ==true){
                    // if tablet will make the Second part for the details Fragment
                        manager.beginTransaction().replace( R.id.Second_Framelayout_tablet,fragment).commit();
                    }
                    else {
                        /// phone
                        // will replace the main screen to the details fragment
                        manager.beginTransaction().addToBackStack("aa").replace(R.id.framelayout, fragment).commit();
                    }
                }
            });
            return 1;
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

                final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        gridView =  (GridView)rootView.findViewById(R.id.grid_view)  ;

        imageAdapter = new ImageAdapter(getActivity(), movie_data);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String overview = movie_data.get(position).overview;
                String title = movie_data.get(position).original_title;
                String release_date = movie_data.get(position).release_date;
                String Id = movie_data.get(position).id;
                String poster = movie_data.get(position).JsonPath;
                String vote_average = movie_data.get(position).vote_average;
                listner mlistner=(listner)getActivity() ; // the data will be send to the main Activity to determine
                // where will go ( Phone Or Tablet) // MainActivity Implement the listner interface
                mlistner.setselectedname(Id, overview, title, release_date, poster, vote_average);
            }
        });
        return rootView;
    }

    public class FetchMovie extends AsyncTask<String,Void,String[]>
    {
        private static final String LOG_TAG ="LOG_TaG" ;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String JsonStr = null;
        @Override
        // this is about background thread
        protected String[] doInBackground(String... params) {

            URL url = null;
          // implementing try and catch to handle error
            try {
                // make a connection
                String BaseUrl = params[0];
                String apikey="?api_key="+BuildConfig.OPEN_Movie_API_KEY;
                String x=BaseUrl.concat(apikey);
                url = new URL(BaseUrl.concat(apikey));
                Log.e("the url is",x);
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
                movie_data.clear();
                for ( int i=0;i<jsonArray.length();i++)
                {
                   JSONObject JsonObject2=jsonArray.getJSONObject(i);
                    String id=JsonObject2.getString("id");
                    String JsonPath=JsonObject2.getString("poster_path");
                    String overview=JsonObject2.getString("overview");
                    String release_date=JsonObject2.getString("release_date");
                    String original_title=JsonObject2.getString("original_title");
                    String backdrop_path=JsonObject2.getString("backdrop_path");
                    String vote_average=JsonObject2.getString("vote_average");
                    String base_path="http://image.tmdb.org/t/p/w185/";
                    movie_object=new Movies(id,base_path.concat(JsonPath),overview,release_date,original_title,backdrop_path,vote_average);
                    movie_data.add(movie_object);
                }
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
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return null;
        }// the end
        // of background thread method
        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            gridView.setAdapter(imageAdapter);
        }
    }
}


