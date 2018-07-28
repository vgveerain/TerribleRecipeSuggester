package com.bumos.vgvee.terriblerecipesuggester;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.internal.bind.DateTypeAdapter;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Main2Activity extends AppCompatActivity {

    RecyclerView recyclerView;
    APIResponse apiResponse;
    int page = 1;
    char sort;
    String url1,url;
    adapter adaptera;
    private Toolbar mTopToolbar;
    ArrayList<recipes> recipes;
    ImageView bookmarkTag;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference rootReference,childReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        rootReference = firebaseDatabase.getReference();
        childReference = rootReference.child(firebaseAuth.getCurrentUser().getUid());

        mTopToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mTopToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        apiResponse = getIntent().getExtras().getParcelable("APIResponse");
        recipes = apiResponse.recipes;
        url1 = getIntent().getExtras().getString("url");
        sort = getIntent().getCharExtra("sort",'r');
        recyclerView = findViewById(R.id.recyclerView);
        adaptera = new adapter(this,recipes,childReference);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adaptera);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.sortRating) {
            sort = 'r';
            url = url1+"&page="+page+"&sort="+sort;
            Log.e("TAG","rating : before Function ->"+url);
            makeNetworkCall(url);
            Log.e("TAG","rating : after Function");
            return true;
        }
        if (id == R.id.sortTrending) {
            sort = 't';
            url = url1+"&page="+page+"&sort="+sort;
            Log.e("TAG","trending : before Function ->"+url);
            makeNetworkCall(url);
            Log.e("TAG","trending : after Function");
            return true;
        }
        if (id == R.id.loadMoreButton) {
            page++;
            url = url1+"&page="+page+"&sort="+sort;
            Log.e("TAG","load more : before Function ->"+url);
            makeNetworkCall(url);
            Log.e("TAG","load more : after Function");
        }

        return super.onOptionsItemSelected(item);
    }

    private void makeNetworkCall(String url){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(Main2Activity.this, "Please retry after some time", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                Gson gson = new Gson();
                apiResponse = gson.fromJson(result,APIResponse.class);
                recipes = apiResponse.recipes;
                if(!recipes.isEmpty()){
                    Main2Activity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("Tag","runnable");
                            Log.e("Tag",result);
                            Log.e("tag",""+apiResponse.recipes.get(0).title);
                            adapter adapter = new adapter(Main2Activity.this,recipes,childReference);
                            recyclerView.setAdapter(adapter);
                        }
                    });
                }else{
                    Main2Activity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Main2Activity.this, "Option Unavailable!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
//                Log.e("Tag","runnable");
//                Log.e("Tag",result);
//                adaptera.notifyDataSetChanged();
            }
        });
    }

    public static void savePost(recipes recipe, DatabaseReference childReference){
        childReference.push().setValue(recipe);
    }
}