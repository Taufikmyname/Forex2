package com.taufik.forex2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout _swipeRefreshLayout1;
    private RecyclerView _recyclerView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initSwipeRefreshLayout();
        _recyclerView1 = findViewById(R.id.recyclerView1);

        bindRecyclerView();
//        initCurrencies();
    }

//    private void initCurrencies() {
//        String url = "https://openexchangerates.org/api/currencies.json";
//        AsyncHttpClient ahc = new AsyncHttpClient();
//
//        ahc.get(url, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                Log.d("*tw*", new String(responseBody));
//
//                Gson g = new Gson();
//                String responseString = new String(responseBody);
//
//                Map<String, String> responseMap = g.fromJson(responseString, new TypeToken<Map<String,String>>(){}.getType());
//                String curenncies = responseMap.get("status");
//
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });
//
//    }

    private void bindRecyclerView() {
        String url = "https://openexchangerates.org/api/latest.json?app_id=1a6ee471eb614d2a9000906ab1e0e159";
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

        asyncHttpClient.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String jsonString = new String(responseBody);
                JSONObject root;

                try {
                    root = new JSONObject(jsonString);
                } catch (JSONException e){
                    Toast.makeText(MainActivity.this, e.getMessage(),Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject rates;

                try {
                    rates = root.getJSONObject("rates");
                } catch (JSONException e){
                    Toast.makeText(MainActivity.this, e.getMessage(),Toast.LENGTH_SHORT).show();
                    return;
                }

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                ForexAdapter adapter = new ForexAdapter(rates);
                _recyclerView1.setLayoutManager(layoutManager);
                _recyclerView1.setAdapter(adapter);

                _swipeRefreshLayout1.setRefreshing(false);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(MainActivity.this, new String(responseBody), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initSwipeRefreshLayout(){
        _swipeRefreshLayout1 =findViewById(R.id.swipeRefreshLayout1);

        _swipeRefreshLayout1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() { bindRecyclerView();}
        });
    }
}