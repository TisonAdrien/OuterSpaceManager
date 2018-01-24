package tison.com.outerspacemanagaer.outerspacemanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dynamitechetan.flowinggradient.FlowingGradientClass;

import java.util.List;

public class ChantierActivity extends AppCompatActivity {

    private TextView txtChantier;

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
    }
}
