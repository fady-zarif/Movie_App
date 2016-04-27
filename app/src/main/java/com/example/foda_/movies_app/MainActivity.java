package com.example.foda_.movies_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements listner {
public static Boolean istablet=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportFragmentManager().beginTransaction().replace( R.id.framelayout,new MainActivityFragment()).commit();
        if (findViewById(R.id.Second_Framelayout_tablet)==null)
        {
            //mobile
            istablet=false;
        }
        else{
            istablet=true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
}
    @Override
    public void setselectedname(String Id, String overview, String title
            , String release_date, String poster, String vote_average) {
       android.support.v4.app.FragmentManager manager=getSupportFragmentManager();

        DetailsActivityFragment fragment=new DetailsActivityFragment();
        Bundle extra=new Bundle();
        extra.putString("Id", Id);
        extra.putString("data", overview);
        extra.putString("title", title);
        extra.putString("released_date", release_date);
        extra.putString("poster", poster);
        extra.putString("vote_average", vote_average);
        String s=extra.getString("title");
        fragment.setArguments(extra);
       if (istablet==true)
       {
          manager.beginTransaction().replace(R.id.Second_Framelayout_tablet, fragment).commit();
       }
        else
       {
           manager.beginTransaction().addToBackStack(" ").replace(R.id.framelayout, fragment).commit();
       }
    }
}
