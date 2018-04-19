package tison.com.outerspacemanagaer.outerspacemanager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dynamitechetan.flowinggradient.FlowingGradientClass;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BuildingActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {


    private ListView listBuilding;

    private Building[] buildings;

    public static final String PREFS_NAME = "TOKEN_FILE";
    private String token;

    private Timer timer;

    private BackgroundView backgroundView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);

        listBuilding = (ListView) findViewById(R.id.listViewBuilding);
        listBuilding.setOnItemClickListener(this);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        backgroundView = (BackgroundView) findViewById(R.id.backgroundViewBuilding);
        backgroundView.animate(this, size.x, size.y);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        token = settings.getString("token","");

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Building building = buildings[position];
        //Toast.makeText(getApplicationContext(), search.getName(), Toast.LENGTH_LONG).show();

        Retrofit retrofit= new Retrofit.Builder().baseUrl("https://outer-space-manager-staging.herokuapp.com").addConverterFactory(GsonConverterFactory.create()).build();
        Api service = retrofit.create(Api.class);
        Call<CodeResponse> request = service.CreateBuilding(token, Integer.toString(position));

        request.enqueue(new Callback<CodeResponse>() {
            @Override
            public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                if(response.code() != 200){
                    if(building.getBuilding().equals("true")) {
                        Toast.makeText(getApplicationContext(), "La construction n'est pas terminée !", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Vous n'avez pas assez de ressources !", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "La construction à commencé !", Toast.LENGTH_LONG).show();
                    Double time =  Double.parseDouble(building.getTimeToBuildLevel0()) + ( Double.parseDouble(building.getTimeToBuildByLevel()) * Double.parseDouble(building.getLevel()));


                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    Date dateCreation = new Date();
                    Long timeConstruct = dateCreation.getTime()/1000;
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putLong("timeStartConstruction"+building.getName(), timeConstruct);
                    editor.apply();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                            PendingIntent resultPendingIntent =
                                    PendingIntent.getActivity(
                                            getApplicationContext(),
                                            0,
                                            resultIntent,
                                            PendingIntent.FLAG_UPDATE_CURRENT
                                    );
                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                                    .setContentTitle("Outer Space Manager - Construction terminée")
                                    .setContentText("Hey ! Ton " + building.getName() + " est terminé !")
                                    .setContentIntent(resultPendingIntent)
                                    .setSmallIcon(R.drawable.logo)
                                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                                    .setVibrate(new long[]{1000, 1000, 0, 0, 1000, 1000})
                                    .setShowWhen(true)
                                    .setWhen(System.currentTimeMillis());

                            // Sets an ID for the notification
                            int mNotificationId = 001;
                            // Gets an instance of the NotificationManager service
                            NotificationManager mNotifyMgr =
                                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            // Builds the notification and issues it.
                            mNotifyMgr.notify(mNotificationId, mBuilder.build());
                        }
                    }, Double.doubleToLongBits(time*1000));
                }
            }

            @Override
            public void onFailure(Call<CodeResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), call.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        backgroundView.resume();
        final Retrofit retrofit= new Retrofit.Builder().baseUrl("https://outer-space-manager-staging.herokuapp.com").addConverterFactory(GsonConverterFactory.create()).build();
        final Api service = retrofit.create(Api.class);

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Call<Buildings> request = service.GetBuildings(token);
                request.enqueue(new Callback<Buildings>() {
                    @Override
                    public void onResponse(Call<Buildings> call, Response<Buildings> response) {
                        if (response.code() != 200) {
                            Toast.makeText(getApplicationContext(), "Une erreur est survenue !", Toast.LENGTH_LONG).show();
                            findViewById(R.id.loadingPanelBuilding).setVisibility(View.GONE);
                            try {
                                Toast.makeText(getApplicationContext(), response.errorBody().string(), Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            buildings = response.body().getBuildings();
                            findViewById(R.id.loadingPanelBuilding).setVisibility(View.GONE);
                            BuildingAdapter adapter = new BuildingAdapter(getApplicationContext(), buildings);
                            listBuilding.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<Buildings> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), call.toString(), Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
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
