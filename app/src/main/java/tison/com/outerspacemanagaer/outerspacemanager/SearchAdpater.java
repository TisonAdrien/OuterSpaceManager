package tison.com.outerspacemanagaer.outerspacemanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Date;

/**
 * Created by atison on 23/01/2018.
 */

public class SearchAdpater extends ArrayAdapter<Search> {

    private final Context context;
    private final Search[] values;
    private ProgressBar progressBarSearch;
    public static final String PREFS_NAME = "TOKEN_FILE";

    public SearchAdpater(Context context, Search[] values) {
        super(context, R.layout.activity_flotte, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_search, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.labelSearch);
        ProgressBar progressBarSearch = (ProgressBar) rowView.findViewById(R.id.progressBarSearch);


        SharedPreferences settings = getContext().getSharedPreferences(PREFS_NAME, 0);
        Long timeStartBuilding = settings.getLong("timeStartSearch"+values[position].getName(), 0);
        if (timeStartBuilding != 0)
        {
            if(values[position].getBuilding().equals("false")) {
                settings.edit().remove("timeStartSearch" + values[position].getName()).commit();
                progressBarSearch.setVisibility(View.INVISIBLE);
            }
            Integer level0 = Integer.parseInt(values[position].getTimeToBuildLevel0());
            Integer byLevel = Integer.parseInt(values[position].getTimeToBuildByLevel());
            Integer level = Integer.parseInt(values[position].getLevel());
            Integer time =  level0 + (level * byLevel);

            progressBarSearch.setMax(time);
            Long timeNow = new Date().getTime()/1000;
            Long difference = timeNow - timeStartBuilding;
            Long timeToShow = time - difference;
            values[position].setTimeToShow(timeToShow.toString());
            Double percent = Double.parseDouble(difference+"") / Double.parseDouble(time +"");
            if (percent < 1)
            {
                progressBarSearch.setProgress(Integer.parseInt(difference+""));
                progressBarSearch.setVisibility(View.VISIBLE);
            }
        }


        textView.setText(values[position].toString());

        return rowView;
    }
}