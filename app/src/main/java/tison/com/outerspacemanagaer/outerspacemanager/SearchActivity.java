package tison.com.outerspacemanagaer.outerspacemanager;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
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

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listSearch;

    private Search[] searches;

    public static final String PREFS_NAME = "TOKEN_FILE";
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        LinearLayout rl = (LinearLayout) findViewById(R.id.bg_search);
        FlowingGradientClass grad = new FlowingGradientClass();
        grad.setBackgroundResource(R.drawable.translate)
                .onLinearLayout(rl)
                .setTransitionDuration(4000)
                .start();

        listSearch = (ListView) findViewById(R.id.listViewSearch);
        listSearch.setOnItemClickListener(this);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        token = settings.getString("token","");

        Retrofit retrofit= new Retrofit.Builder().baseUrl("https://outer-space-manager.herokuapp.com").addConverterFactory(GsonConverterFactory.create()).build();
        Api service = retrofit.create(Api.class);
        Call<Searches> request = service.GetSearchesForUser(token);

        request.enqueue(new Callback<Searches>() {
            @Override
            public void onResponse(Call<Searches> call, Response<Searches> response) {
                if(response.code() != 200){
                    Toast.makeText(getApplicationContext(), "Une erreur est survenue !", Toast.LENGTH_LONG).show();

                    try {
                        Toast.makeText(getApplicationContext(), response.errorBody().string(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    searches = response.body().getSearches();
                    //Toast.makeText(getApplicationContext(), buildings.toString(), Toast.LENGTH_LONG).show();

                    //listSearch.setAdapter(new ArrayAdapter(getApplicationContext(),  android.R.layout.simple_list_item_1, searches));

                    SearchAdpater adapter = new SearchAdpater(getApplicationContext(), searches );
                    listSearch.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<Searches> call, Throwable t) {
                Toast.makeText(getApplicationContext(), call.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Search search = searches[position];
        //Toast.makeText(getApplicationContext(), search.getName(), Toast.LENGTH_LONG).show();

        Retrofit retrofit= new Retrofit.Builder().baseUrl("https://outer-space-manager.herokuapp.com").addConverterFactory(GsonConverterFactory.create()).build();
        Api service = retrofit.create(Api.class);
        Call<CodeResponse> request = service.StartSearchesForUser(token, Integer.toString(position));

        request.enqueue(new Callback<CodeResponse>() {
            @Override
            public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                if(response.code() != 200){
                    if(search.getBuilding().equals("true"))
                        Toast.makeText(getApplicationContext(), "La recherche n'est pas terminée !", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getApplicationContext(), "Vous n'avez pas assez de ressources !", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "La recherche à commencé !", Toast.LENGTH_LONG).show();

                    Double time =  Double.parseDouble(search.getTimeToBuildLevel0()) + ( Double.parseDouble(search.getTimeToBuildByLevel()) * Double.parseDouble(search.getLevel()));

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
                                    .setContentTitle("Outer Space Manager - Recherche terminée")
                                    .setContentText("Hey ! Ta " + search.getName() + " est terminé !")
                                    .setSmallIcon(R.drawable.logo)
                                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                                    .setVibrate(new long[] { 1000, 1000, 0, 0, 1000, 1000})
                                    .setContentIntent(resultPendingIntent)
                                    .setShowWhen(true)
                                    .setWhen(System.currentTimeMillis());

                            // Sets an ID for the notification
                            int mNotificationId = 002;
                            // Gets an instance of the NotificationManager service
                            NotificationManager mNotifyMgr =
                                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            // Builds the notification and issues it.
                            mNotifyMgr.notify(mNotificationId, mBuilder.build());
                        }
                    }, Double.doubleToLongBits(time * 1000));

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
