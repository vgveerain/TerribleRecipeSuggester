package com.bumos.vgvee.terriblerecipesuggester;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener{

    Button goBtn;
    EditText queryBox;
    String key = "bc16dc74f4ee9aac045c71e18a334fd2";
    APIResponse apiResponse;
    public String url;
    char sort = 'r';
    ImageView imageView;
    TextView viewSaved;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference rootReference;

    View.OnClickListener onClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewSaved = findViewById(R.id.viewSaved);
        viewSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Main3Activity.class);
                startActivity(intent);
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        rootReference = firebaseDatabase.getReference();

        imageView = findViewById(R.id.textView2);
        Picasso.get().load("https://i5.walmartimages.com/dfw/4ff9c6c9-7cfc/k2-_8a9d4b25-0e37-45f2-826f-c6bc794581b4.v1.jpg?odnWidth=1360&odnHeight=600&odnBg=ffffff").into(imageView);

        queryBox = findViewById(R.id.queryBox);
        goBtn = findViewById(R.id.goBtn);
//        goBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String q = queryBox.getText().toString();
//                try {
//                    q = URLEncoder.encode(q,"UTF-8");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                url = "http://food2fork.com/api/search?key="+key+"&q="+q;
//                makeNetworkCall(url);
//            }
//        });
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String q = queryBox.getText().toString();
                try {
                    q = URLEncoder.encode(q,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                url = "http://food2fork.com/api/search?key="+key+"&q="+q;
                makeNetworkCall(url);
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(this);
    }

    private void makeNetworkCall(final String url){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(MainActivity.this, "Please retry after some time", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                Gson gson = new Gson();
                apiResponse = gson.fromJson(result,APIResponse.class);
//                Log.e("TAG",String.valueOf(apiResponse.height));
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                        intent.putExtra("APIResponse",apiResponse);
                        intent.putExtra("url",url);
                        intent.putExtra("sort",sort);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if(firebaseAuth.getCurrentUser() == null){
            Intent loginIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(Arrays.asList(
                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                            new AuthUI.IdpConfig.EmailBuilder().build()
                    ))
                    .build();
                    startActivity(loginIntent);
        }else{
            goBtn.setOnClickListener(onClickListener);
            rootReference = rootReference.child(firebaseAuth.getCurrentUser().getUid());
        }
    }
}
