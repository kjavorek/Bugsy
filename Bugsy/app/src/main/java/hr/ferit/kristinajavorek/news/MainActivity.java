package hr.ferit.kristinajavorek.news;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

public class MainActivity extends Activity {

    ListView listcomp=null;
    Adapter adapt_obj=null; // Create an Object for Adapter Class
    Context myref=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listcomp=(ListView)findViewById(R.id.mylistview);
        myref=MainActivity.this;
        new MyAsyncTask().execute(); // Call the Async Task

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
            dialog.setMessage("Loading ...");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected void onPostExecute(Void result)
        {
            if(dialog.isShowing() == true)
            {
                dialog.dismiss();
            }
            listcomp.setAdapter(adapt_obj);
            adapt_obj.notifyDataSetChanged();
        }
    }
}

