package tison.com.outerspacemanagaer.outerspacemanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dynamitechetan.flowinggradient.FlowingGradientClass;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GalaxyActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{

    private UserResponse[] listUsers;
    private ListView viewUsers;

    private Button nextButton;
    private Button previousButton;

    public static final String PREFS_NAME = "TOKEN_FILE";
    private String token;
    private String pageGalaxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galaxy);
        viewUsers = (ListView) findViewById(R.id.listViewGalaxy);
        viewUsers.setOnItemClickListener(this);

        nextButton = (Button) findViewById(R.id.nextButtonGalaxy);
        nextButton.setOnClickListener(this);
        previousButton = (Button) findViewById(R.id.previousButtonGalaxy);
        previousButton.setOnClickListener(this);

        LinearLayout rl = (LinearLayout) findViewById(R.id.bg_galaxy);
        FlowingGradientClass grad = new FlowingGradientClass();
        grad.setBackgroundResource(R.drawable.translate)
                .onLinearLayout(rl)
                .setTransitionDuration(4000)
                .start();

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        token = settings.getString("token","");
        pageGalaxy = settings.getString("pageGalaxy", "0");

        if(Integer.parseInt(pageGalaxy) > 0){
            previousButton.setVisibility(View.VISIBLE);
        }else{
            previousButton.setVisibility(View.INVISIBLE);
        }

        Retrofit retrofit= new Retrofit.Builder().baseUrl("https://outer-space-manager.herokuapp.com").addConverterFactory(GsonConverterFactory.create()).build();
        Api service = retrofit.create(Api.class);
        Integer offset = Integer.parseInt(pageGalaxy) * 20;
        Call<UserTable> request = service.GetUsers(token, offset.toString() );

        request.enqueue(new Callback<UserTable>() {
            @Override
            public void onResponse(Call<UserTable> call, Response<UserTable> response) {
                if(response.code() != 200){
                    //Toast.makeText(getApplicationContext(), "Une erreur est survenue !", Toast.LENGTH_LONG).show();

                    try {
                        Toast.makeText(getApplicationContext(), response.errorBody().string(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    //Toast.makeText(getApplicationContext(), "Connection...", Toast.LENGTH_LONG).show();
                    listUsers = response.body().getUsers();

                    if(listUsers.length == 21){
                        nextButton.setVisibility(View.VISIBLE);
                    }else{
                        nextButton.setVisibility(View.INVISIBLE);
                    }

                    //viewUsers.setAdapter(new ArrayAdapter(getApplicationContext(),  android.R.layout.simple_list_item_1, listUsers));

                    UserAdapter adapter = new UserAdapter(getApplicationContext(), listUsers );
                    viewUsers.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<UserTable> call, Throwable t) {
                Toast.makeText(getApplicationContext(), call.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        if(v.getId() == nextButton.getId()){
            Integer offset = Integer.parseInt(pageGalaxy) + 1;
            editor.putString("pageGalaxy",offset.toString());
        }else if(v.getId() == previousButton.getId()){
            Integer offset = Integer.parseInt(pageGalaxy) - 1;
            editor.putString("pageGalaxy",offset.toString());
        }
        editor.commit();

        Intent myIntent = new Intent(getApplicationContext(), GalaxyActivity.class);
        startActivity(myIntent);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final String user_to_attack = listUsers[position].getUsername();
        AlertDialog.Builder builder = new AlertDialog.Builder(GalaxyActivity.this);
        builder.setMessage("Voulez-vous attaquer " + listUsers[position].getUsername() + " ?");
        builder.setCancelable(true);
        builder.setPositiveButton(
                "Oui",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent myIntent = new Intent(getApplicationContext(), AttackActivity.class);
                        Gson json = new Gson();
                        String jsonString = json.toJson(user_to_attack);
                        myIntent.putExtra("USER_TO_ATTACK", jsonString);
                        startActivity(myIntent);
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
}
