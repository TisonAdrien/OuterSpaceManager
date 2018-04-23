package tison.com.outerspacemanagaer.outerspacemanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
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

    private ImageButton nextButton;
    private ImageButton previousButton;

    public static final String PREFS_NAME = "TOKEN_FILE";
    private String token;
    private String pageGalaxy;

    private BackgroundView backgroundView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galaxy);
        viewUsers = (ListView) findViewById(R.id.listViewGalaxy);
        viewUsers.setOnItemClickListener(this);

        nextButton = (ImageButton) findViewById(R.id.nextButtonGalaxy);
        nextButton.setOnClickListener(this);
        previousButton = (ImageButton) findViewById(R.id.previousButtonGalaxy);
        previousButton.setOnClickListener(this);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        backgroundView = (BackgroundView) findViewById(R.id.backgroundViewGalaxy);
        backgroundView.animate(this, size.x, size.y);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        token = settings.getString("token","");
        pageGalaxy = settings.getString("pageGalaxy", "0");

        if(Integer.parseInt(pageGalaxy) > 0){
            previousButton.setVisibility(View.VISIBLE);
        }else{
            previousButton.setVisibility(View.INVISIBLE);
        }

        Retrofit retrofit= new Retrofit.Builder().baseUrl("https://outer-space-manager-staging.herokuapp.com").addConverterFactory(GsonConverterFactory.create()).build();
        Api service = retrofit.create(Api.class);
        Integer offset = Integer.parseInt(pageGalaxy) * 20;
        Call<UserTable> request = service.GetUsers(token, offset.toString() );

        request.enqueue(new Callback<UserTable>() {
            @Override
            public void onResponse(Call<UserTable> call, Response<UserTable> response) {
                if(response.code() != 200){
                    //Toast.makeText(getApplicationContext(), "Une erreur est survenue !", Toast.LENGTH_LONG).show();
                    findViewById(R.id.loadingPanelGalaxy).setVisibility(View.GONE);
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
                    findViewById(R.id.loadingPanelGalaxy).setVisibility(View.GONE);
                    UserAdapter adapter = new UserAdapter(getApplicationContext(), listUsers );
                    viewUsers.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<UserTable> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Aucune réponse, vérifiez votre connection internet", Toast.LENGTH_LONG).show();
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

    @Override
    protected void onPause() {
        super.onPause();
        backgroundView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        backgroundView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        backgroundView.resume();
    }
}
