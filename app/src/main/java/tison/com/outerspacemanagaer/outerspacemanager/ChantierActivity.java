package tison.com.outerspacemanagaer.outerspacemanager;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dynamitechetan.flowinggradient.FlowingGradientClass;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChantierActivity extends AppCompatActivity {

    private ListView listViewReport;

    private Report[] reports;

    public static final String PREFS_NAME = "TOKEN_FILE";
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chantier);

        LinearLayout rl = (LinearLayout) findViewById(R.id.bg_chantier);
        FlowingGradientClass grad = new FlowingGradientClass();
        grad.setBackgroundResource(R.drawable.translate)
                .onLinearLayout(rl)
                .setTransitionDuration(4000)
                .start();

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        token = settings.getString("token","");


        listViewReport = (ListView) findViewById(R.id.listViewReport);


        Retrofit retrofit= new Retrofit.Builder().baseUrl("https://outer-space-manager.herokuapp.com").addConverterFactory(GsonConverterFactory.create()).build();
        Api service = retrofit.create(Api.class);
        Call<Reports> request = service.GetReports(token);

        request.enqueue(new Callback<Reports>() {
            @Override
            public void onResponse(Call<Reports> call, Response<Reports> response) {
                if(response.code() != 200){
                    try {
                        Toast.makeText(getApplicationContext(), response.errorBody().string(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    reports = response.body().getReports();
                    if(reports.length == 0){
                        Toast.makeText(ChantierActivity.this, "Il n'y a aucun rapport pour le moment", Toast.LENGTH_SHORT).show();
                    }
                    ReportAdapter adapter = new ReportAdapter(getApplicationContext(), reports );
                    listViewReport.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<Reports> call, Throwable t) {
                Toast.makeText(getApplicationContext(), call.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
