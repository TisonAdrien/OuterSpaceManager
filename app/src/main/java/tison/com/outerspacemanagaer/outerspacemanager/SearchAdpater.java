package tison.com.outerspacemanagaer.outerspacemanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by atison on 23/01/2018.
 */

public class SearchAdpater extends ArrayAdapter<Search> {

    private final Context context;
    private final Search[] values;

    public SearchAdpater(Context context, Search[] values) {
        super(context, R.layout.activity_flotte, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_flotte, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        if(values[position].getBuilding().equals("true"))
            textView.setText(values[position].getName() + " En cours de recherche (" + values[position].getTimeToBuildByLevel() + "s)");
        else
            textView.setText(values[position].getName() + " level  " + values[position].getLevel() + " " + values[position].getBuilding());

        return rowView;
    }
}