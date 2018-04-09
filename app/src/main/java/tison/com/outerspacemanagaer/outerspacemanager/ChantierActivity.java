package tison.com.outerspacemanagaer.outerspacemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class ChantierActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView listViewReport;

    private Button nextButton;
    private Button previousButton;

    private Report[] reports;

    public static final String PREFS_NAME = "TOKEN_FILE";
    private String token;
    private String pageChantier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chantier);

        nextButton = (Button) findViewById(R.id.nextButtonChantier);
        nextButton.setOnClickListener(this);
        previousButton = (Button) findViewById(R.id.previousButtonChantier);
        previousButton.setOnClickListener(this);

        LinearLayout rl = (LinearLayout) findViewById(R.id.bg_chantier);
        FlowingGradientClass grad = new FlowingGradientClass();
        grad.setBackgroundResource(R.drawable.translate)
                .onLinearLayout(rl)
                .setTransitionDuration(4000)
                .start();

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        token = settings.getString("token","");
        pageChantier = settings.getString("pageChantier", "0");
        if(Integer.parseInt(pageChantier) > 0){
            previousButton.setVisibility(View.VISIBLE);
        }else{
            previousButton.setVisibility(View.INVISIBLE);
        }


        listViewReport = (ListView) findViewById(R.id.listViewReport);


        Retrofit retrofit= new Retrofit.Builder().baseUrl("https://outer-space-manager.herokuapp.com").addConverterFactory(GsonConverterFactory.create()).build();
        Api service = retrofit.create(Api.class);
        Integer offset = Integer.parseInt(pageChantier) * 20;
        Call<Reports> request = service.GetReports(token, offset.toString());

        request.enqueue(new Callback<Reports>() {
            @Override
            public void onResponse(Call<Reports> call, Response<Reports> response) {
                if(response.code() != 200){
                    findViewById(R.id.loadingPanelChantier).setVisibility(View.GONE);
                    try {
                        Toast.makeText(getApplicationContext(), response.errorBody().string(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    reports = response.body().getReports();
                    if(reports.length == 0){
                        Toast.makeText(ChantierActivity.this, "Il n'y a aucun rapport pour le moment", Toast.LENGTH_SHORT).show();
                    }else if(reports.length == 21){
                        nextButton.setVisibility(View.VISIBLE);
                    }else{
                        nextButton.setVisibility(View.INVISIBLE);
                    }
                    findViewById(R.id.loadingPanelChantier).setVisibility(View.GONE);
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

    @Override
    public void onClick(View v) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        if(v.getId() == nextButton.getId()){
            Integer offset = Integer.parseInt(pageChantier) + 1;
            editor.putString("pageChantier",offset.toString());
        }else if(v.getId() == previousButton.getId()){
            Integer offset = Integer.parseInt(pageChantier) - 1;
            editor.putString("pageChantier",offset.toString());
        }
        editor.commit();

        Intent myIntent = new Intent(getApplicationContext(), GalaxyActivity.class);
        startActivity(myIntent);
    }
}
