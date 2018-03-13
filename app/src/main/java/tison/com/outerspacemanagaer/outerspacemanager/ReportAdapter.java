package tison.com.outerspacemanagaer.outerspacemanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by atison on 13/03/2018.
 */

public class ReportAdapter extends ArrayAdapter<Report> {

    private final Context context;
    private final Report[] values;

    public ReportAdapter(Context context, Report[] values) {
        super(context, R.layout.activity_flotte, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_chantier, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.labelReport);

        textView.setText(values[position].toString());

        return rowView;
    }
}