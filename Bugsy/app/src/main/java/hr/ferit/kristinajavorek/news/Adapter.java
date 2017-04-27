package hr.ferit.kristinajavorek.news;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;

import org.w3c.dom.Text;

public class Adapter extends BaseAdapter {

    LayoutInflater inflation=null;
    Context mycontext=null;
    String urlvalue=null,roottag=null,parseelement=null;
    Parser myparseobj=null;
    String[] title_array=null,image_array=null, category_array=null, pubDate_array=null, link_array=null;
    String selected="All";
    Bitmap b;
    int br;

    Adapter(Context c,String url)
    {
        Log.d("Adapter","Adapter");
        mycontext=c;
        inflation=LayoutInflater.from(mycontext);
        myparseobj=new Parser();
        title_array=myparseobj.xmlParsing(url,"item","title");
        pubDate_array=myparseobj.xmlParsing(url,"item","pubDate");
        image_array=myparseobj.xmlParsing(url,"item","enclosure");
        link_array=myparseobj.xmlParsing(url,"item","link");
        category_array=myparseobj.xmlParsing(url,"item","category");
    }

    public String[] getCategory(){
        return category_array;
    }

    @Override
    public int getCount() {
        return title_array.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public String getLink(int position){
        ArrayList<Integer> itemPosition = new ArrayList<Integer>();
        if(selected.equals("All")) return link_array[position];
        else {
            for (int i = 0; i < category_array.length; i++) {
                if(selected.equals(category_array[i])) {
                    itemPosition.add(i);
                }
            }
            return link_array[itemPosition.get(position)];
        }
    }

    public void changeCategory(String category){
        selected=category;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(category_array[position].equals(selected) || selected.equals("All")) {
            MyHolder holder = new MyHolder();
            convertView=null;
            if (convertView == null) {
                convertView = inflation.inflate(R.layout.item, null);
                holder.tv = (TextView) convertView.findViewById(R.id.mytextview);
                holder.pubDate = (TextView) convertView.findViewById(R.id.date);
                holder.iv = (ImageView) convertView.findViewById(R.id.myimgview);
            } else {
                holder.tv = (TextView) convertView.findViewById(R.id.mytextview);
                holder.pubDate = (TextView) convertView.findViewById(R.id.date);
                holder.iv = (ImageView) convertView.findViewById(R.id.myimgview);
            }
            holder.tv.setText(title_array[position]);
            String dateString = pubDate_array[position];
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss 'GMT'");
            Date convertedDate = new Date();
            try {
                convertedDate = dateFormat.parse(dateString);
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm'h'");
                String sDate = sdf.format(convertedDate);
                holder.pubDate.setText(sDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String imgUrl=image_array[position];
            Ion.with(holder.iv)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .load(imgUrl);
        }
        return convertView;
    }

    static class MyHolder
    {
        TextView tv=null, pubDate=null;
        ImageView iv=null;
    }
}

