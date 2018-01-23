package tison.com.outerspacemanagaer.outerspacemanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by atison on 23/01/2018.
 */

public class BuildingAdapter extends ArrayAdapter<Building> {

    private final Context context;
    private final Building[] values;
    public BuildingAdapter(Context context, Building[] values) {
        super(context,R.layout.activity_galaxy, values);
        this.context = context;
        this.values = values;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_user, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        if(values[position].getBuilding().equals("true"))
            textView.setText(values[position].getName() + " En cours de construction (" + values[position].getTimeToBuildByLevel() + "s)");
        else
            textView.setText(values[position].getName() + " level " + values[position].getLevel() + " " + values[position].getBuilding());

        UrlImageViewHelper.setUrlDrawable(imageView, values[position].getImageUrl());

        return rowView;
    }


}

