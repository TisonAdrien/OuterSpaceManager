package tison.com.outerspacemanagaer.outerspacemanager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by atison on 23/01/2018.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> implements View.OnClickListener {

    private final UserResponse[] values;
    public static final String PREFS_NAME = "TOKEN_FILE";
    private final Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView v = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user, parent, false);
        v.setOnClickListener(this);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mTextView.setText(values[position].toString());
    }

    @Override
    public int getItemCount() {
        return values.length;
    }

    @Override
    public void onClick(View v) {
        TextView txtView = (TextView) v;
        final String username = txtView.getText().toString().substring(0, txtView.getText().toString().indexOf(":") - 1);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.myDialog));
        builder.setMessage("Voulez-vous attaquer " + username + " ?");
        builder.setCancelable(true);
        builder.setPositiveButton(
                "Oui",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent myIntent = new Intent(context, AttackActivity.class);
                        Gson json = new Gson();
                        String jsonString = json.toJson(username);
                        myIntent.putExtra("USER_TO_ATTACK", jsonString);
                        context.startActivity(myIntent);
                    }
                });

        builder.setNegativeButton(
                "Non",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }
    }

    public UserAdapter(Context context, UserResponse[] values) {
        this.context = context;
        this.values = values;
    }
}
