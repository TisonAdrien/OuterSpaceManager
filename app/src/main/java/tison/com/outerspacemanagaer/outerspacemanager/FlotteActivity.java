package tison.com.outerspacemanagaer.outerspacemanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dynamitechetan.flowinggradient.FlowingGradientClass;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.lang.Double.parseDouble;

public class FlotteActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listShips;

    private Ship[] flotte;
    private Ship[] flotte_empty;
    private Ship[] flotte_ok;
    private ArrayList<Ship> listFlotte;

    private String minerals;
    private String gas;

    public static final String PREFS_NAME = "TOKEN_FILE";
    private String token;

    private Timer timer;

    private BackgroundView backgroundView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flotte);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        backgroundView = (BackgroundView) findViewById(R.id.backgroundViewFlotte);
        backgroundView.animate(this, size.x, size.y);

        listShips = (ListView) findViewById(R.id.listViewFlotte);
        listShips.setOnItemClickListener(this);

        listFlotte = new ArrayList<Ship>();

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        token = settings.getString("token","");

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Ship ship = flotte[position];

        Integer minerals_cost = Integer.parseInt(ship.getMineralCost());
        Integer gas_cost = Integer.parseInt(ship.getGasCost());

        Long nb_minerals = Math.round(Double.parseDouble(minerals));
        Long nb_gas = Math.round(Double.parseDouble(gas));

        Long max = Long.getLong("0");
        if(minerals_cost > 0)
            max = nb_minerals / minerals_cost;
        else if(gas_cost > 0)
            max = nb_gas / gas_cost;

        if( (nb_gas / gas_cost) < max){
            max = nb_gas / gas_cost;
        }
        
        Integer maximum = Integer.parseInt(max.toString());
        final String[] options;
        if(maximum <= 0){
            options = new String[1];
            options[0] = "Pas assez de ressources";
        }else if(maximum < 3 ){
            options = new String[maximum];
            Integer i = 0;
            while (i < maximum){
                Integer val = i + 1;
                options[i] = val.toString();
                i = val;
            }
        }else{
            options = new String[3];
            options[0] = "1";
            Integer middle = maximum / 2;
            options[1] = middle.toString();
            options[2] = maximum.toString();
        }
        
        if(maximum <= 0){
            Toast.makeText(this, "Pas assez de ressources...", Toast.LENGTH_SHORT).show();
        }else {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("combien voulez-vous construire de " + flotte[position].getName() + " ?");
            builder.setSingleChoiceItems(
                    options,
                    0,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int i) {
                            ship.setAmount(options[i]);


                            Retrofit retrofit = new Retrofit.Builder().baseUrl("https://outer-space-manager-staging.herokuapp.com").addConverterFactory(GsonConverterFactory.create()).build();
                            Api service = retrofit.create(Api.class);
                            Call<CodeResponse> request = service.CreateShips(token, ship, ship.getShipId());

                            request.enqueue(new Callback<CodeResponse>() {
                                @Override
                                public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                                    if (response.code() != 200) {
                                        if (response.code() == 401)
                                            Toast.makeText(getApplicationContext(), "Vous n'avez pas assez de ressources", Toast.LENGTH_LONG).show();
                                        else
                                            Toast.makeText(getApplicationContext(), "Il ne faut pas spamer", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "La construction de " + ship.getAmount() + " " + ship.getName() + " à commencé !", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<CodeResponse> call, Throwable t) {
                                    Toast.makeText(getApplicationContext(), call.toString(), Toast.LENGTH_LONG).show();
                                    Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
                                }
                            });


                            dialog.dismiss();
                        }
                    }
            );
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        backgroundView.resume();
        final Retrofit retrofit = new Retrofit.Builder().baseUrl("https://outer-space-manager-staging.herokuapp.com").addConverterFactory(GsonConverterFactory.create()).build();
        final Api service = retrofit.create(Api.class);

        final Retrofit retrofit_2 = new Retrofit.Builder().baseUrl("https://outer-space-manager-staging.herokuapp.com").addConverterFactory(GsonConverterFactory.create()).build();
        final Api service_2 = retrofit_2.create(Api.class);

        final Retrofit retrofit_3 = new Retrofit.Builder().baseUrl("https://outer-space-manager-staging.herokuapp.com").addConverterFactory(GsonConverterFactory.create()).build();
        final Api service_3 = retrofit_3.create(Api.class);

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                listFlotte = new ArrayList<Ship>();

                Call<Ships> request = service.GetAllShips(token);

                request = service.GetAllShips(token);
                request.enqueue(new Callback<Ships>() {
                    @Override
                    public void onResponse(Call<Ships> call, Response<Ships> response) {
                        if (response.code() != 200) {
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
                        } else {
                            flotte_empty = response.body().getShips();
                            listFlotte.addAll(Arrays.asList(flotte_empty));



                            Call<Ships> request_2 = service_2.GetShips(token);
                            request_2.enqueue(new Callback<Ships>() {
                                @Override
                                public void onResponse(Call<Ships> call, Response<Ships> response) {
                                    if (response.code() != 200) {
                                        Toast.makeText(getApplicationContext(), "Une erreur est survenue !", Toast.LENGTH_LONG).show();
                                        findViewById(R.id.loadingPanelFlotte).setVisibility(View.GONE);
                                        try {
                                            Toast.makeText(getApplicationContext(), response.errorBody().string(), Toast.LENGTH_LONG).show();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        flotte_ok = response.body().getShips();

                                        ArrayList<Ship> oldFlotte = listFlotte;
                                        int index = 0;
                                        for (Ship s : flotte_ok) {
                                            index = 0;
                                            for (Ship s_empty : oldFlotte) {
                                                if (s.getShipId().equals(s_empty.getShipId())) {
                                                    listFlotte.set(index, s);
                                                }
                                                index++;
                                            }
                                        }

                                        flotte = listFlotte.toArray(new Ship[0]);
                                        findViewById(R.id.loadingPanelFlotte).setVisibility(View.GONE);
                                        FlotteAdapter adapter = new FlotteAdapter(getApplicationContext(), flotte);
                                        listShips.setAdapter(adapter);
                                    }
                                }

                                @Override
                                public void onFailure(Call<Ships> call, Throwable t) {
                                    Toast.makeText(getApplicationContext(), "Aucune réponse, vérifiez votre connection internet", Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    }

                    @Override
                    public void onFailure(Call<Ships> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Aucune réponse, vérifiez votre connection internet", Toast.LENGTH_LONG).show();
                    }
                });


                Call<UserResponse> request3 = service_3.GetUserInfo(token);
                request3.enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        if (response.code() == 200) {
                            minerals = response.body().getMinerals();
                            gas = response.body().getGas();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Aucune réponse, vérifiez votre connection internet", Toast.LENGTH_LONG).show();
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
