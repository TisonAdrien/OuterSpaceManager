package tison.com.outerspacemanagaer.outerspacemanager;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by atison on 23/01/2018.
 */

public class FlotteAdapter extends ArrayAdapter<Ship> {

    private final Context context;
    private final Ship[] values;
    public FlotteAdapter(Context context, Ship[] values) {
        super(context,R.layout.activity_flotte, values);
        this.context = context;
        this.values = values;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_flotte, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.labelFlotte);

        textView.setText(values[position].toString());

        return rowView;
    }


}