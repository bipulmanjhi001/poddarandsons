package poddarandsons.com.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;
import poddarandsons.com.R;
import poddarandsons.com.api.URL;
import poddarandsons.com.model.VolleySingleton;

public class Dashboard extends AppCompatActivity {

     TextView logo_text;
     LinearLayout calc,goinglay,come;
     String token;
     TextView completeds,going,coming;
    private static final String SHARED_PREF_NAME = "Bubblespref";
    private static final String KEY_ID = "token";
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            token = bundle.getString("token");
        }
        SharedPreferences prefs = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        token = prefs.getString(KEY_ID, null);
        phone = prefs.getString("phone", "");

        UPDATE();

        logo_text=(TextView)findViewById(R.id.logo_text);
        coming=(TextView)findViewById(R.id.coming);
        completeds=(TextView)findViewById(R.id.completed);
        going=(TextView)findViewById(R.id.going);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "goodVibrationsscript.ttf");
        logo_text.setTypeface(typeface);

        calc=(LinearLayout)findViewById(R.id.calc);
        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openMain = new Intent(Dashboard.this, Gallery.class);
                openMain.putExtra("mode","1");
                startActivity(openMain);

            }
        });
        goinglay=(LinearLayout)findViewById(R.id.goinglay);
        goinglay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openMain = new Intent(Dashboard.this, Gallery.class);
                openMain.putExtra("mode","2");
                startActivity(openMain);

            }
        });
        come=(LinearLayout)findViewById(R.id.come);
        come.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openMain = new Intent(Dashboard.this, Gallery.class);
                openMain.putExtra("mode","3");
                startActivity(openMain);

            }
        });
    }

    private void UPDATE() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.URL_countproject,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getBoolean("status")) {

                                JSONObject gets=obj.getJSONObject("count");
                                String completed = String.valueOf(gets.getInt("completed"));
                                completeds.setText(completed);
                                String ongoing =String.valueOf(gets.getInt("ongoing"));
                                going.setText(ongoing);
                                String upcoming = String.valueOf(gets.getInt("upcoming"));
                                coming.setText(upcoming);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Check connection again.", Toast.LENGTH_SHORT).show();
                    }
                })
        {
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}