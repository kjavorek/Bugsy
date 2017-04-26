package hr.ferit.kristinajavorek.news;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Adapter extends BaseAdapter {

    LayoutInflater inflation=null;
    Context mycontext=null;
    String urlvalue=null,roottag=null,parseelement=null;
    Parser myparseobj=null;
    String[] title_array=null,image_array=null, category_array=null, pubDate_array=null, link_array=null;
    String selected="All";

    Adapter(Context c,String url)
    {
        Log.d("Adapter","Adapter");
        mycontext=c;
        inflation=LayoutInflater.from(mycontext);
        myparseobj=new Parser();
        title_array=myparseobj.xmlParsing(url,"item","title");
        pubDate_array=myparseobj.xmlParsing(url,"item","pubDate");
        image_array=myparseobj.xmlParsing(url," item ","image");
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

    public void changeCategory(String category){
        selected=category;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(category_array[position].equals(selected) || selected.equals("All")) {
            MyHolder holder = new MyHolder();
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
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss zzz");
            Date convertedDate = new Date();
            try {
                convertedDate = dateFormat.parse(dateString);
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
                String sDate = sdf.format(convertedDate);
                holder.pubDate.setText(sDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            try {
                String temp = image_array[position];
                InputStream is = new java.net.URL(temp).openStream();
                Bitmap b = BitmapFactory.decodeStream(is);
                holder.iv.setImageBitmap(b);
            } catch (Exception e) {
            }
        }
        return convertView;
    }

    static class MyHolder
    {
        TextView tv=null, pubDate=null;
        ImageView iv=null;
    }
}

