package hr.ferit.kristinajavorek.news;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Adapter extends BaseAdapter {

    LayoutInflater inflation=null;
    Context mycontext=null;
    String urlvalue=null,roottag=null,parseelement=null;
    Parser myparseobj=null;
    String[] title_array=null,image_array=null, category_array=null, pubDate_array=null;

    Adapter(Context c,String url)
    {
        Log.d("Adapter","Adapter");
        mycontext=c;
        inflation=LayoutInflater.from(mycontext);
        myparseobj=new Parser();
        title_array=myparseobj.xmlParsing(url,"item","title");
        pubDate_array=myparseobj.xmlParsing(url,"item","pubDate");
        image_array=myparseobj.xmlParsing(url," item ","image");
        category_array=myparseobj.xmlParsing(url,"item","category");
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyHolder holder=new MyHolder();;
        if(convertView == null)
        {
            convertView=inflation.inflate(R.layout.item, null);

            holder.tv=(TextView)convertView.findViewById(R.id.mytextview);
            holder.pubDate=(TextView)convertView.findViewById(R.id.date);
            holder.iv=(ImageView)convertView.findViewById(R.id.myimgview);


        }
        else
        {
            holder.tv=(TextView)convertView.findViewById(R.id.mytextview);
            holder.pubDate=(TextView)convertView.findViewById(R.id.date);
            holder.iv=(ImageView)convertView.findViewById(R.id.myimgview);

        }

        holder.tv.setText(title_array[position]);
        holder.pubDate.setText(pubDate_array[position]);
        try{
            String temp=image_array[position];
            InputStream is= new java.net.URL(temp).openStream();
            Bitmap b=BitmapFactory.decodeStream(is);
            holder.iv.setImageBitmap(b);
        }
        catch(Exception e){}

        return convertView;
    }

    static class MyHolder
    {
        TextView tv=null, pubDate=null;
        ImageView iv=null;
    }
}

