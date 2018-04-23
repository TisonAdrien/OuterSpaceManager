package tison.com.outerspacemanagaer.outerspacemanager;

import android.content.Context;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.List;


/**
 * Created by atison on 23/01/2018.
 */

public class BuildingAdapter extends ArrayAdapter<Building> {

    private final Context context;
    private final Building[] values;
    private ProgressBar pbPercentBuild;
    public static final String PREFS_NAME = "TOKEN_FILE";
    public BuildingAdapter(Context context, Building[] values) {
        super(context,R.layout.activity_galaxy, values);
        this.context = context;
        this.values = values;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_building, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.labelBuilding);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.iconBuilding);
        ProgressBar pbPercentBuild = (ProgressBar) rowView.findViewById(R.id.progressBarBuilding);


        SharedPreferences settings = getContext().getSharedPreferences(PREFS_NAME, 0);
        Long timeStartBuilding = settings.getLong("timeStartConstruction"+values[position].getName(), 0);
        if (timeStartBuilding != 0)
        {
            if(values[position].getBuilding().equals("false")) {
                settings.edit().remove("timeStartConstruction" + values[position].getName()).commit();
                pbPercentBuild.setVisibility(View.INVISIBLE);
            }
            Integer level0 = Integer.parseInt(values[position].getTimeToBuildLevel0());
            Integer byLevel = Integer.parseInt(values[position].getTimeToBuildByLevel());
            Integer level = Integer.parseInt(values[position].getLevel());
            Integer time =  level0 + (level * byLevel);

            pbPercentBuild.setMax(time);
            Long timeNow = new Date().getTime()/1000;
            Long difference = timeNow - timeStartBuilding;
            Long timeToShow = time - difference;
            values[position].setTimeToShow(timeToShow.toString());
            Double percent = Double.parseDouble(difference+"") / Double.parseDouble(time +"");
            if (percent < 1)
            {
                pbPercentBuild.setProgress(Integer.parseInt(difference+""));
                pbPercentBuild.setVisibility(View.VISIBLE);
            }
        }


        if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            imageView.setVisibility(View.INVISIBLE);
            textView.setText(values[position].getName() + "\n Niveau " + values[position].getLevel());
        }else{
            textView.setText(values[position].toString());
            String bitmap = settings.getString("bitmap_"+values[position].getName(),null);
            if(bitmap != null){
                byte[] imageAsBytes = Base64.decode(bitmap.getBytes(), Base64.DEFAULT);
                imageView.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
            }else{
                new DownLoadImageTask(imageView, getContext(), values[position].getName()).execute(values[position].getImageUrl());
            }
        }

        return rowView;
    }

}

class DownLoadImageTask extends AsyncTask<String,Void,Bitmap> {
    ImageView imageView;
    Context context;
    String salt;
    public DownLoadImageTask(ImageView imageView, Context context, String salt){
        this.imageView = imageView;
        this.context = context;
        this.salt = salt;
    }

    protected Bitmap doInBackground(String...urls){
        String urlOfImage = urls[0];
        Bitmap logo = null;
        try{
            InputStream is = new URL(urlOfImage).openStream();
            logo = BitmapFactory.decodeStream(is);
        }catch(Exception e){ // Catch the download exception
            e.printStackTrace();
        }
        return logo;
    }

    /*
        onPostExecute(Result result)
            Runs on the UI thread after doInBackground(Params...).
     */
    protected void onPostExecute(Bitmap result){
        imageView.setImageBitmap(result);


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        result.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        String encoded = Base64.encodeToString(b, Base64.DEFAULT);


        SharedPreferences settings = context.getSharedPreferences("TOKEN_FILE", 0);
        Date dateCreation = new Date();
        Long timeConstruct = dateCreation.getTime()/1000;
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("bitmap_"+salt, encoded);
        editor.apply();

    }
}

