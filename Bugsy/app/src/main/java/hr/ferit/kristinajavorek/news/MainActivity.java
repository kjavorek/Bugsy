package hr.ferit.kristinajavorek.news;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.xml.sax.XMLReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class MainActivity extends Activity {

    ListView listcomp=null;
    Adapter adapt_obj=null; // Create an Object for Adapter Class
    Context myref=null;
    Spinner spinner;
    String[] spinnerCategory;
    ArrayAdapter<String> adapter;
    String selected, previous="";
    private SwipeRefreshLayout swipeContainer;
    boolean created=false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listcomp=(ListView)findViewById(R.id.mylistview);
        spinner = (Spinner)findViewById(R.id.spinner);

        myref=MainActivity.this;
        new MyAsyncTask().execute(); // Call the Async Task


        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    public void fetchTimelineAsync(int page) {
        created=true;
        new MyAsyncTask().execute();
    }


    private class MyAsyncTask extends AsyncTask<Void,Void,Void>{

        private final ProgressDialog dialog=new ProgressDialog(MainActivity.this);

        @Override
        protected Void doInBackground(Void... params) {
            adapt_obj=new Adapter(myref,"http://www.bug.hr/rss/vijesti/");
            return null;
        }

        @Override
        protected void onPreExecute()
        {
            if(!created) {
                dialog.setMessage("Loading ...");
                dialog.show();
                dialog.setCancelable(false);
            }
        }

        @Override
        protected void onPostExecute(Void result)
        {
            if(dialog.isShowing() == true)
            {
                dialog.dismiss();
            }
            else swipeContainer.setRefreshing(false);

            spinnerCategory=adapt_obj.getCategory();
            List<String> list = new ArrayList<>(new TreeSet<>(Arrays.asList(spinnerCategory)));
            list.add(0, "All");
            final ArrayAdapter<String>adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_spinner_item,list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    selected = spinner.getSelectedItem().toString();
                    if(selected.equals(previous)){return;}
                    else {
                        previous=selected;
                        adapt_obj.changeCategory(selected);
                        adapt_obj.notifyDataSetChanged();
                        listcomp.setAdapter(adapt_obj);
                    }
                }
                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });

            listcomp.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    listcomp.setAdapter(adapt_obj);
                    view.getResources();
                    String link = adapt_obj.getLink(position);
                    Uri uri = Uri.parse(link);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });

            listcomp.setAdapter(adapt_obj);
            adapt_obj.notifyDataSetChanged();
            spinner.setAdapter(adapter);

        }
    }



}

