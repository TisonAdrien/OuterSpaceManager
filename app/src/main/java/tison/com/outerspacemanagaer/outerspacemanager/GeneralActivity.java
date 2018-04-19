package tison.com.outerspacemanagaer.outerspacemanager;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);

        LinearLayout rl = (LinearLayout) findViewById(R.id.bg_general);
        FlowingGradientClass grad = new FlowingGradientClass();
        grad.setBackgroundResource(R.drawable.translate)
                .onLinearLayout(rl)
                .setTransitionDuration(4000)
                .start();

        txtInfos = (TextView) findViewById(R.id.txtInfos);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        token = settings.getString("token","");
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                            Toast.makeText(getApplicationContext(), "Une erreur est survenue !", Toast.LENGTH_LONG).show();
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
        timer.purge();
    }
}
