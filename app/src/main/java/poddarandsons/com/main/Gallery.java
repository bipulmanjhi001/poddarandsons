package poddarandsons.com.main;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import poddarandsons.com.R;
import poddarandsons.com.api.URL;
import poddarandsons.com.model.Category_GridView_ImageAdapter;
import poddarandsons.com.model.Category_Grid_Model;
import poddarandsons.com.model.ExpandableHeightGridView;
import poddarandsons.com.model.VolleySingleton;

public class Gallery extends AppCompatActivity {
    Dialog myDialog,myDialog2;
    String mode,url,ids,desc,price;
    ExpandableHeightGridView mGridView;
    private Category_GridView_ImageAdapter category_gridView_imageAdapter;
    private ArrayList<Category_Grid_Model> mGridData;
    ProgressBar progressBar;
    String names,Phones,Regardings,Emails,Websites,Additionals;
    EditText name,Phone,Regarding,Email,Website,Additional;
    LinearLayout CONFIRM_add2;
    private static final String SHARED_PREF_NAME = "Bubblespref";
    private static final String KEY_ID = "token";
    String token,phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mode = bundle.getString("mode");
        }
        SharedPreferences prefs = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        token = prefs.getString(KEY_ID, null);
        phone = prefs.getString("phone", "");

        myDialog=new Dialog(Gallery.this);
        myDialog2=new Dialog(Gallery.this);
        mGridView=(ExpandableHeightGridView)findViewById(R.id.grid_imagelist);
        mGridView.setExpanded(true);
        mGridData = new ArrayList<Category_Grid_Model>();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        UPDATE();
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Category_Grid_Model item = (Category_Grid_Model) adapterView.getItemAtPosition(i);
                ids= item.getId();
                url= item.getImage();
                desc= item.getDesc();
                price= item.getPrice();
                ShowPopup(view);
            }
        });
    }

    public void ShowPopup(View v) {
        TextView txtclose,staff_rate_name,staff_rate_phone;
        Button btnFollow;
        myDialog.setContentView(R.layout.custom_rateus);
        myDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);

        ImageView staff_img = (ImageView) myDialog.findViewById(R.id.enquiry_ph);
        try {
            Glide.with(getApplicationContext())
                    .load(url)
                    .into(staff_img);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        staff_rate_name =(TextView) myDialog.findViewById(R.id._name);
        staff_rate_name.setText(desc);
        staff_rate_phone =(TextView) myDialog.findViewById(R.id._address);
        staff_rate_phone.setText("Price :" + price);

        btnFollow = (Button) myDialog.findViewById(R.id.btnenquiry);
        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowEnquiry(v);
                myDialog.dismiss();
            }
        });
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    private void UPDATE() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.URL_completeimage,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject c = new JSONObject(response);
                            JSONArray array = c.getJSONArray("content");

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject object = array.getJSONObject(i);
                                String id=object.getString("id");
                                String description=object.getString("description");
                                String price=object.getString("price");
                                String pic_dir=object.getString("pic_dir");
                                String pic_name=object.getString("pic_name");

                                Category_Grid_Model _grid_model= new Category_Grid_Model();
                                _grid_model.setId(id);
                                _grid_model.setDesc(description);
                                _grid_model.setPrice(price);
                                _grid_model.setName(pic_name);
                                _grid_model.setImage(pic_dir);
                                mGridData.add(_grid_model);
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        category_gridView_imageAdapter = new Category_GridView_ImageAdapter(Gallery.this, R.layout.complete_lay, mGridData);
                        mGridView.setAdapter(category_gridView_imageAdapter);
                        category_gridView_imageAdapter.setGridData(mGridData);
                        progressBar.setVisibility(View.GONE);
                        mGridView.setVisibility(View.VISIBLE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Check connection again.", Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("mode", mode);
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void ShowEnquiry(View v) {

        myDialog2.setContentView(R.layout.custom_enquary);
        myDialog2.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        name=(EditText)myDialog2.findViewById(R.id.name);
        Phone=(EditText)myDialog2.findViewById(R.id.Phone);
        Regarding=(EditText)myDialog2.findViewById(R.id.Regarding);
        Email=(EditText)myDialog2.findViewById(R.id.Email);
        Website=(EditText)myDialog2.findViewById(R.id.Website);
        Additional=(EditText)myDialog2.findViewById(R.id.Additional);

        CONFIRM_add2= (LinearLayout) myDialog2.findViewById(R.id.CONFIRM_add);
        CONFIRM_add2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                names=name.getText().toString();
                Phones=Phone.getText().toString();
                Regardings=Regarding.getText().toString();
                Emails= Email.getText().toString();
                Websites=Website.getText().toString();
                Additionals=Additional.getText().toString();
                EnquiryUpdate();
                myDialog2.dismiss();

            }
        });
        myDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog2.show();
    }

    public void EnquiryUpdate() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.URL_saveenquiry,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                             Toast.makeText(getApplicationContext(), obj.getString("enquiry"), Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                params.put("regarding", Regardings);
                params.put("message", Additionals);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
