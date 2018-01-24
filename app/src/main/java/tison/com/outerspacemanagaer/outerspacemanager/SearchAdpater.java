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
        View rowView = inflater.inflate(R.layout.row_search, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.labelSearch);

        textView.setText(values[position].toString());

        return rowView;
    }
}