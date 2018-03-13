package tison.com.outerspacemanagaer.outerspacemanager;

import android.content.SharedPreferences;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        Retrofit retrofit= new Retrofit.Builder().baseUrl("https://outer-space-manager.herokuapp.com").addConverterFactory(GsonConverterFactory.create()).build();
        Api service = retrofit.create(Api.class);

        Call<Ships> request = service.GetAllShips(token);

        request = service.GetAllShips(token);
        request.enqueue(new Callback<Ships>() {
            @Override
            public void onResponse(Call<Ships> call, Response<Ships> response) {
                if(response.code() != 200){
                    Toast.makeText(getApplicationContext(), "Une erreur est survenue !", Toast.LENGTH_LONG).show();

                    try {
                        Toast.makeText(getApplicationContext(), response.errorBody().string(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    flotte_empty = response.body().getShips();
                    listFlotte.addAll(Arrays.asList(flotte_empty));




                    Retrofit retrofit_2 = new Retrofit.Builder().baseUrl("https://outer-space-manager.herokuapp.com").addConverterFactory(GsonConverterFactory.create()).build();
                    Api service_2 = retrofit_2.create(Api.class);
                    Call<Ships> request_2 = service_2.GetShips(token);
                    request_2.enqueue(new Callback<Ships>() {
                        @Override
                        public void onResponse(Call<Ships> call, Response<Ships> response) {
                            if(response.code() != 200){
                                Toast.makeText(getApplicationContext(), "Une erreur est survenue !", Toast.LENGTH_LONG).show();

                                try {
                                    Toast.makeText(getApplicationContext(), response.errorBody().string(), Toast.LENGTH_LONG).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }else{
                                flotte_ok = response.body().getShips();

                                ArrayList<Ship> oldFlotte = listFlotte;
                                int index = 0;
                                for (Ship s: flotte_ok) {
                                    index = 0;
                                    for (Ship s_empty: oldFlotte){
                                        if(s.getShipId() == s_empty.getShipId()){
                                            listFlotte.set(index, s);
                                        }
                                        index++;
                                    }
                                }


                                flotte = listFlotte.toArray(new Ship[0]);
                                FlotteAdapter adapter = new FlotteAdapter(getApplicationContext(), flotte );
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Ship ship = flotte[position];
        ship.setAmount("1");
        //Toast.makeText(getApplicationContext(), ship.toString(), Toast.LENGTH_LONG).show();

        Retrofit retrofit= new Retrofit.Builder().baseUrl("https://outer-space-manager.herokuapp.com").addConverterFactory(GsonConverterFactory.create()).build();
        Api service = retrofit.create(Api.class);
        Call<CodeResponse> request = service.CreateShips(token, ship, ship.getShipId());

        request.enqueue(new Callback<CodeResponse>() {
            @Override
            public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                if(response.code() != 200){
                    Toast.makeText(getApplicationContext(), "Une erreur est survenue, ne spam pas !", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "La construction à commencé !", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CodeResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), call.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
