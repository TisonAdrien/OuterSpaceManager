package tison.com.outerspacemanagaer.outerspacemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
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

public class GeneralActivity extends AppCompatActivity {

    private TextView txtInfos;

    public static final String PREFS_NAME = "TOKEN_FILE";
    private String token;

    private Timer timer;

    private BackgroundView backgroundView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        backgroundView = (BackgroundView) findViewById(R.id.backgroundViewGeneral);
        backgroundView.animate(this, size.x, size.y);

        txtInfos = (TextView) findViewById(R.id.txtInfos);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        token = settings.getString("token","");
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
            public void run(){
                Call<UserResponse> request = service.GetUserInfo(token);
                request.enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        if(response.code() != 200){
                            findViewById(R.id.loadingPanelGeneral).setVisibility(View.GONE);
                            switch (response.code()){
                                case 401:
                                    //Pas assez de ressources
                                    Toast.makeText(getApplicationContext(), "Vous n'avez pas assez de ressources", Toast.LENGTH_LONG).show();
                                    break;
                                case 403 :
                                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                                    settings.edit().remove("token").apply();
                                    Toast.makeText(getApplicationContext(), "Veuillez vous réauthentifier s'il vous plait", Toast.LENGTH_LONG).show();
                                    Intent myIntent = new Intent(getApplicationContext(), SignUpActivity.class);
                                    startActivity(myIntent);
                                    break;
                                case 500:
                                    //Erreur serveur
                                    Toast.makeText(getApplicationContext(), "Une erreur interne s'est produite, réessayez plus tard...", Toast.LENGTH_LONG).show();
                                    break;
                                default:
                                    //Stop crack
                                    Toast.makeText(getApplicationContext(), "Une erreur s'est produite, elle vient de vous. Vous êtes une erreur.", Toast.LENGTH_LONG).show();
                                    break;
                            }
                        }else{
                            //Toast.makeText(getApplicationContext(), "Connection...", Toast.LENGTH_LONG).show();
                            UserResponse user = response.body();
                            String toDisplay = user.getUsername() + " (" + user.getPoints() + " points)\n";
                            Double gas = Double.parseDouble(user.getGas());
                            Double minerals = Double.parseDouble(user.getMinerals());
                            toDisplay += "Gaz : " + Math.round(gas) + "\n";
                            toDisplay += "Mineraux : " +  Math.round(minerals) + "\n";
                            findViewById(R.id.loadingPanelGeneral).setVisibility(View.GONE);
                            txtInfos.setText(toDisplay);
                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Aucune réponse, vérifiez votre connection internet", Toast.LENGTH_LONG).show();
                    }
                });
            }
        },0,1000);
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
