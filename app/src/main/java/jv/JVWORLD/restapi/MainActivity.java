package jv.JVWORLD.restapi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressLint("SetTextI18n")
public class MainActivity extends AppCompatActivity {

   private TextView textView,textViewKey,textViewR,textViewKeyR;
   private Button button,buttonKey,buttonR,buttonKeyR;
   private TextInputEditText editText,editTextKey,editTextR,editTextKeyR,editTextRP;
   private String data1,urlR,urlRP;
   private final int a = 0;
   private boolean keyActivate = false,keyActivateR = false;
   private LinearLayout ll1,ll2;
   private JsonRetroApi jsonRetroApi;

   private void initUI(){
       // JSON volley

       textView = findViewById(R.id.text_view_json);
       button = findViewById(R.id.button);
       editText = findViewById(R.id.ed_query);
       textViewKey = findViewById(R.id.text_view_json_data);
       buttonKey = findViewById(R.id.get_data);
       editTextKey = findViewById(R.id.ed_query_data);
       ll1 = findViewById(R.id.ll1);

       //JSON Retrofit

       textViewR = findViewById(R.id.text_view_json_r);
       buttonR = findViewById(R.id.button_r);
       editTextR = findViewById(R.id.ed_query_r);
       editTextRP = findViewById(R.id.ed_query_rp);
       textViewKeyR = findViewById(R.id.text_view_json_data_r);
       buttonKeyR = findViewById(R.id.get_data_r);
       editTextKeyR = findViewById(R.id.ed_query_data_r);
       ll2 = findViewById(R.id.ll2);

   }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        BtnHandler();
    }

    private void BtnHandler() {

        button.setOnClickListener(view -> JSONDATAFetched());

        buttonKey.setOnClickListener(view -> {
            keyActivate = true;
            JSONDATAFetched();
        });

        buttonR.setOnClickListener(view -> {
            urlR = Objects.requireNonNull(editTextR.getText()).toString();
            urlRP = Objects.requireNonNull(editTextRP.getText()).toString();
            if (textViewR != null){
                textViewR.setText("");
            }
            if (urlR.equals("")){
                textViewR.setText("Please enter Url/path");
                editTextR.setText("");
                ll2.setVisibility(View.INVISIBLE);
            }else if (urlR.equals("c")){
                textViewR.setText("");
                editTextR.setText("");
                editTextRP.setText("");
                ll2.setVisibility(View.INVISIBLE);
            }else{
                Retrofit retrofit = new Retrofit.Builder().baseUrl(urlR).
                        addConverterFactory(GsonConverterFactory.create()).build();
                jsonRetroApi = retrofit.create(JsonRetroApi.class);
                JSONDATARetrofit();
            }
        });

        buttonKeyR.setOnClickListener(view -> keyActivateR = true);
    }

    @SuppressLint("SetTextI18n")
    private void JSONDATAFetched() {

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String url = Objects.requireNonNull(editText.getText()).toString();
        String Key = Objects.requireNonNull(editTextKey.getText()).toString();
        if (url.equals("c")){
            textView.setText("");
            editText.setText("");
            ll1.setVisibility(View.INVISIBLE);
        }else if (url.equals("")){
            textView.setText("Please enter something");
            editText.setText("");
            ll1.setVisibility(View.INVISIBLE);
        }else{
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
                textView.setText(response.toString());
                ll1.setVisibility(View.VISIBLE);
                if (keyActivate) {
                    try {
                        JSONObject apiInfo = response.getJSONObject(a);
                        data1 = apiInfo.getString(Key);
                        textViewKey.setText(data1);
                        keyActivate = false;
                    } catch (JSONException e) {
                        textViewKey.setText(e.getMessage());
                        e.printStackTrace();
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    textViewKey.setText(error.getMessage());
                }
            }

            );

            queue.add(jsonArrayRequest);
        }
    }

    @SuppressLint("SetTextI18n")
    private void JSONDATARetrofit(){
       if (urlRP.equals("")){
           Call<List<PostModel>> call = jsonRetroApi.getPost(urlR);
           call.enqueue(new Callback<List<PostModel>>() {
               @Override
               public void onResponse(@NonNull Call<List<PostModel>> call, @NonNull retrofit2.Response<List<PostModel>> response) {
                   if (!response.isSuccessful()) {
                       textViewR.setText(response.message());
                       Toast.makeText(MainActivity.this, urlRP, Toast.LENGTH_SHORT).show();
                       return;
                   }
                   List<PostModel> postModels = response.body();
                   int b =1;
                   assert postModels != null;
                   for (PostModel postModel: postModels){
                       String a = "";
                       a += "ID: "+ b +" "+ postModel.getName()+"\n";
                       b++;
                       textViewR.append(a);
                   }
               }

               @Override
               public void onFailure(@NonNull Call<List<PostModel>> call, @NonNull Throwable t) {
                   textViewR.setText(t.getMessage());
               }
           });
       }
       else {
           Map<String, String> stringMap = new HashMap<>();
           stringMap.put(urlRP, "2");
           stringMap.put("_sort", "id");
           stringMap.put("_order", "asc");
           Call<List<PostModel>> call = jsonRetroApi.getPost(urlR, stringMap);
           call.enqueue(new Callback<List<PostModel>>() {
               @Override
               public void onResponse(@NonNull Call<List<PostModel>> call, @NonNull retrofit2.Response<List<PostModel>> response) {
                   if (!response.isSuccessful()) {
                       textViewR.setText(response.message());
                       Toast.makeText(MainActivity.this, urlRP, Toast.LENGTH_SHORT).show();
                       return;
                   }
                   List<PostModel> postModels = response.body();
                   int b =1;
                   assert postModels != null;
                   for (PostModel postModel: postModels){
                       String a = "";
                       a += "ID: "+ b +" "+ postModel.getName()+"\n";
                       b++;
                       textViewR.append(a);
                   }
               }

               @Override
               public void onFailure(@NonNull Call<List<PostModel>> call, @NonNull Throwable t) {
                   textViewR.setText(t.getMessage());
               }
           });
       }
    }
}

class PostModel{

    @SerializedName("body")
    String name;

    public PostModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}