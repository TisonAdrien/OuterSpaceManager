package tison.com.outerspacemanagaer.outerspacemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dynamitechetan.flowinggradient.FlowingGradientClass;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    private TextView txtPoints;
    private TextView txtUsername;
    private Button butDisconnect;
    private Button butGeneral;
    private Button butBuilding;
    private Button butFlotte;
    private Button butSearch;
    private Button butChantier;
    private Button butGalaxy;


    public static final String PREFS_NAME = "TOKEN_FILE";
    private String token;

    private Timer timer;

    private BackgroundView backgroundView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        backgroundView = (BackgroundView) findViewById(R.id.backgroundViewMain);
        backgroundView.animate(this, size.x, size.y);

        txtPoints = (TextView) findViewById(R.id.textViewScore);
        txtUsername = (TextView) findViewById(R.id.textViewUsername);
        butDisconnect = (Button) findViewById(R.id.buttonDisconnect);
        butGeneral = (Button) findViewById(R.id.buttonGeneral);
        butBuilding = (Button) findViewById(R.id.buttonBuilding);
        butFlotte = (Button) findViewById(R.id.buttonFlotte);
        butSearch = (Button) findViewById(R.id.buttonSearch);
        butChantier = (Button) findViewById(R.id.buttonChantier);
        butGalaxy = (Button) findViewById(R.id.buttonGalaxy);

        butDisconnect.setOnClickListener(this);
        butGeneral.setOnClickListener(this);
        butBuilding.setOnClickListener(this);
        butFlotte.setOnClickListener(this);
        butSearch.setOnClickListener(this);
        butChantier.setOnClickListener(this);
        butGalaxy.setOnClickListener(this);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        token = settings.getString("token","");
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == butDisconnect.getId()){
            //Toast.makeText(getApplicationContext(), "Déconnexion...", Toast.LENGTH_LONG).show();
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("token", "");
            // Commit the edits!
            editor.commit();

            Intent myIntent = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(myIntent);
        }else if(v.getId() == butGalaxy.getId()){
            Intent myIntent = new Intent(getApplicationContext(), GalaxyActivity.class);
            startActivity(myIntent);
        }else if(v.getId() == butBuilding.getId()){
            Intent myIntent = new Intent(getApplicationContext(), BuildingActivity.class);
            startActivity(myIntent);
        }else if(v.getId() == butChantier.getId()){
            Intent myIntent = new Intent(getApplicationContext(), ChantierActivity.class);
            startActivity(myIntent);
        }else if(v.getId() == butFlotte.getId()){
            Intent myIntent = new Intent(getApplicationContext(), FlotteActivity.class);
            startActivity(myIntent);
        }else if(v.getId() == butGeneral.getId()){
            Intent myIntent = new Intent(getApplicationContext(), GeneralActivity.class);
            startActivity(myIntent);
        }else if(v.getId() == butSearch.getId()){
            Intent myIntent = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(myIntent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        backgroundView.resume();
        final Retrofit retrofit= new Retrofit.Builder().baseUrl("https://outer-space-manager-staging.herokuapp.com").addConverterFactory(GsonConverterFactory.create()).build();
        final Api service = retrofit.create(Api.class);

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run() {
                Call<UserResponse> request = service.GetUserInfo(token);

                request.enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        if (response.code() != 200) {
                            Toast.makeText(getApplicationContext(), "Une erreur est survenue !", Toast.LENGTH_LONG).show();
                        } else {
                            //Toast.makeText(getApplicationContext(), "Connection...", Toast.LENGTH_LONG).show();
                            txtPoints.setText("Points : " + response.body().getPoints());
                            txtUsername.setText(response.body().getUsername());
                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), call.toString(), Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        },0,5000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer.purge();
        backgroundView.pause();
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
        timer.purge();
        backgroundView.pause();
    }
}
