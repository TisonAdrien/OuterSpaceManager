package tison.com.outerspacemanagaer.outerspacemanager;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by atison on 23/04/2018.
 */

public class BuildDetailsFragment extends Fragment implements View.OnClickListener {
    private TextView tvNameBuilding;
    private TextView tvLevelBuilding;
    private TextView tvMineralCost;
    private TextView tvGasCost;
    private TextView tvTimeToBuild;
    private TextView tvAmountOfEffect;
    private TextView tvEffect;
    private TextView tvCost;
    private ImageView ivBuildingDetails;
    private ImageButton btnBuild;
    private ProgressBar pbPercentBuild;
    public static final String PREFS_NAME = "TOKEN_FILE";
    private String token;
    private Building building;
    private Long timeStartBuilding;
    private SharedPreferences settings;
    private Timer time;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_build_details,container);
        tvNameBuilding = v.findViewById(R.id.tvNameBuilding);
        tvLevelBuilding = v.findViewById(R.id.tvLevelBuilding);
        tvMineralCost = v.findViewById(R.id.tvMineralCost);
        tvGasCost = v.findViewById(R.id.tvGasCost);
        tvTimeToBuild = v.findViewById(R.id.tvTimeToBuild);
        tvAmountOfEffect = v.findViewById(R.id.tvAmountOfEffect);
        tvEffect = v.findViewById(R.id.tvEffect);
        tvCost = v.findViewById(R.id.tvCost);
        ivBuildingDetails = v.findViewById(R.id.ivBuildingDetails);
        btnBuild = v.findViewById(R.id.btnBuild);
        pbPercentBuild = v.findViewById(R.id.pbPercentBuild);
        btnBuild.setOnClickListener(this);
        settings = this.getActivity().getSharedPreferences(PREFS_NAME, 0);
        token = settings.getString("token","");
        building = new Building();
        timeStartBuilding = Long.getLong("0");
        return v;
    }

    public void fillTextView(String text)
    {
        tvNameBuilding.setText(text);
    }

    public void fillContent(Building building)
    {
        this.building = building;
        timeStartBuilding = settings.getLong("timeStartConstruction"+building.getName(), 0);

        DownLoadImageTask dl = new DownLoadImageTask(ivBuildingDetails, getActivity().getApplicationContext(), "");
        dl.execute(building.getImageUrl());
        tvNameBuilding.setText(building.getName());
        tvLevelBuilding.setText("Level : " + building.getLevel());

        Integer level0 = Integer.parseInt(building.getTimeToBuildLevel0());
        Integer byLevel = Integer.parseInt(building.getTimeToBuildByLevel());
        Integer level = Integer.parseInt(building.getLevel());
        Integer times =  level0 + (level * byLevel);
        Integer minutes = (int) Math.floor(times / 60);
        Integer seconds = (int) Math.floor(times % 60);
        Integer costMineral = (Integer.parseInt(building.getMineralCostLevel0()) + ( Integer.parseInt(building.getMineralCostByLevel()) * Integer.parseInt(building.getLevel())));
        Integer costGas = Integer.parseInt(building.getGasCostLevel0()) + ( Integer.parseInt(building.getGasCostByLevel()) * Integer.parseInt(building.getLevel()));
        Integer amountEffect = Integer.parseInt(building.getAmountOfEffectLevel0()) + (Integer.parseInt(building.getAmountOfEffectByLevel()) * Integer.parseInt(building.getLevel()));

        tvTimeToBuild.setText("Temps de build : " + minutes.toString() + ":" + seconds.toString() + " minutes");
        tvMineralCost.setText(costMineral.toString() + " minéraux");
        tvGasCost.setText(costGas.toString() + " gaz");
        tvAmountOfEffect.setText("Bonus : +" + amountEffect.toString() + " ");
        tvEffect.setText(building.getEffect());
        if (building.getBuilding().equals("true"))
        {
            tvTimeToBuild.append(" (en construction)");
            btnBuild.setColorFilter(Color.argb(150, 0, 0, 0));
            btnBuild.setEnabled(false);
            if (timeStartBuilding != 0) {
                this.updateProgressBar();
            }
        }
        else
        {
            btnBuild.setColorFilter(0);
            btnBuild.setEnabled(true);
            pbPercentBuild.setVisibility(View.INVISIBLE);
        }
        btnBuild.setVisibility(View.VISIBLE);
        tvCost.setVisibility(View.VISIBLE);

        setTimer();
        this.updateProgressBar();
    }

    public void updateProgressBar()
    {
        if (this.building.getBuilding().equals("true") && timeStartBuilding != 0)
        {
            setTimer();
            time.scheduleAtFixedRate(new TimerTask(){
                @Override
                public void run() {
                    if (getActivity()!=null){
                        Integer level0 = Integer.parseInt(BuildDetailsFragment.this.building.getTimeToBuildLevel0());
                        Integer byLevel = Integer.parseInt(BuildDetailsFragment.this.building.getTimeToBuildByLevel());
                        Integer level = Integer.parseInt(BuildDetailsFragment.this.building.getLevel());
                        final Integer times =  level0 + (level * byLevel);
                        pbPercentBuild.setMax(times);
                        Long timeNow = new Date().getTime() / 1000;
                        final Long difference = timeNow - timeStartBuilding;
                        Double percent = Double.parseDouble(difference + "") / Double.parseDouble(times+ "");
                        if (percent < 1) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pbPercentBuild.setProgress(Integer.parseInt(difference + ""));
                                    tvTimeToBuild.setText("Temps restant : " + (times - difference)/60 + ":" + (times - difference)%60 + " minutes");
                                    pbPercentBuild.setVisibility(View.VISIBLE);
                                }
                            });

                        } else {
                            time.cancel();
                            time.purge();
                            BuildDetailsFragment.this.refreshBuild();
                            settings.edit().remove("timeStartConstruction"+building.getName()).apply();
                            timeStartBuilding = Long.parseLong("0");
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    BuildDetailsFragment.this.fillContent(building);
                                    pbPercentBuild.setVisibility(View.INVISIBLE);
                                    btnBuild.setColorFilter(0);
                                    btnBuild.setEnabled(true);
                                }
                            });
                        }
                    }
                }
            },0,1000);
        }else{
            pbPercentBuild.setVisibility(View.INVISIBLE);
        }
    }

    public void setTimer(){
        // Reset the timer
        try{
            time.cancel();
            time.purge();
        }catch (Exception e){
            // Do nothing
        }
        time = new Timer();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(time != null){
            time.cancel();
            time.purge();
        }
    }

    private void refreshBuild()
    {
        Integer lvl = Integer.parseInt(this.building.getLevel())+1;
        this.building.setLevel(lvl+"");
        this.building.setBuilding(false+"");
    }

    @Override
    public void onClick(View v) {
        Retrofit retrofit= new Retrofit.Builder().baseUrl("https://outer-space-manager-staging.herokuapp.com").addConverterFactory(GsonConverterFactory.create()).build();
        Api service = retrofit.create(Api.class);
        Call<CodeResponse> request = service.CreateBuilding(token, building.getBuildingId());
        final BuildDetailsFragment buildDetailsFragment = BuildDetailsFragment.this;
        request.enqueue(new Callback<CodeResponse>() {
            @Override
            public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                switch (response.code())
                {
                    case 200:
                        Toast.makeText(getContext(), "La construction à commencé !", Toast.LENGTH_LONG).show();
                        Date dateCreation = new Date();
                        Long timeConstruct = dateCreation.getTime()/1000;
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putLong("timeStartConstruction"+building.getName(), timeConstruct);
                        editor.commit();
                        building.setBuilding("true");
                        buildDetailsFragment.fillContent(building);
                        break;
                    case 401 :
                        String res = "";
                        try {
                            Toast.makeText(getContext(), response.errorBody().string(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        break;
                    case 403 :
                        Toast.makeText(getContext(), "Veuillez vous réauthentifier s'il vous plait", Toast.LENGTH_LONG).show();
                        settings.edit().remove("token").apply();
                        Intent myIntent = new Intent(getContext(), SignUpActivity.class);
                        startActivity(myIntent);
                        break;
                    case 404 :
                        Toast.makeText(getContext(), "Comment as tu fait pour changer mes valeurs ? Oo", Toast.LENGTH_LONG).show();
                        break;
                    case 500 :
                        Toast.makeText(getContext(), "Problème interne de l'API, réessayez plus tard...", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<CodeResponse> call, Throwable t) {
                Toast.makeText(getContext(), call.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}