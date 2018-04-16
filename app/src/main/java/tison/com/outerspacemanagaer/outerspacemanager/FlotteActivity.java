package tison.com.outerspacemanagaer.outerspacemanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class FlotteActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listShips;

    private Ship[] flotte;
    private Ship[] flotte_empty;
    private Ship[] flotte_ok;
    private ArrayList<Ship> listFlotte;

    public static final String PREFS_NAME = "TOKEN_FILE";
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flotte);

        LinearLayout rl = (LinearLayout) findViewById(R.id.bg_flotte);
        FlowingGradientClass grad = new FlowingGradientClass();
        grad.setBackgroundResource(R.drawable.translate)
                .onLinearLayout(rl)
                .setTransitionDuration(4000)
                .start();

        listShips = (ListView) findViewById(R.id.listViewFlotte);
        listShips.setOnItemClickListener(this);

        listFlotte = new ArrayList<Ship>();

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        token = settings.getString("token","");


        final Retrofit retrofit = new Retrofit.Builder().baseUrl("https://outer-space-manager-staging.herokuapp.com").addConverterFactory(GsonConverterFactory.create()).build();
        final Api service = retrofit.create(Api.class);

        final Retrofit retrofit_2 = new Retrofit.Builder().baseUrl("https://outer-space-manager-staging.herokuapp.com").addConverterFactory(GsonConverterFactory.create()).build();
        final Api service_2 = retrofit_2.create(Api.class);

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                listFlotte = new ArrayList<Ship>();

                Call<Ships> request = service.GetAllShips(token);

                request = service.GetAllShips(token);
                request.enqueue(new Callback<Ships>() {
                    @Override
                    public void onResponse(Call<Ships> call, Response<Ships> response) {
                        if (response.code() != 200) {
                            Toast.makeText(getApplicationContext(), "Une erreur est survenue !", Toast.LENGTH_LONG).show();

                            try {
                                Toast.makeText(getApplicationContext(), response.errorBody().string(), Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                e.printStackTrace();
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
                                                if (s.getShipId() == s_empty.getShipId()) {
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
                                    Toast.makeText(getApplicationContext(), call.toString(), Toast.LENGTH_LONG).show();
                                    Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    }

                    @Override
                    public void onFailure(Call<Ships> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), call.toString(), Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        },0,3000);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Ship ship = flotte[position];
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("combien voulez-vous construire de " + flotte[position].getName() + " ?");
        final String[] options = new String[7];
        options[0] = "1";
        options[1] = "10";
        options[2] = "50";
        options[3] = "100";
        options[4] = "250";
        options[5] = "500";
        options[6] = "1000";
        builder.setSingleChoiceItems(
                options,
                0,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        ship.setAmount(options[i]);


                        Retrofit retrofit= new Retrofit.Builder().baseUrl("https://outer-space-manager-staging.herokuapp.com").addConverterFactory(GsonConverterFactory.create()).build();
                        Api service = retrofit.create(Api.class);
                        Call<CodeResponse> request = service.CreateShips(token, ship, ship.getShipId());

                        request.enqueue(new Callback<CodeResponse>() {
                            @Override
                            public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                                if(response.code() != 200){
                                    if(response.code() == 401)
                                        Toast.makeText(getApplicationContext(), "Vous n'avez pas assez de ressources", Toast.LENGTH_LONG).show();
                                    else
                                        Toast.makeText(getApplicationContext(), "Il ne faut pas spamer", Toast.LENGTH_LONG).show();
                                }else{
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
