package com.example.meme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;


import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    ProgressBar progressBar;
    String local=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



       loadImage();

        Button button = findViewById(R.id.button);

        Button share = findViewById(R.id.button2);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent send = new Intent();

                send.setAction(Intent.ACTION_SEND);
                send.putExtra(Intent.EXTRA_TEXT,"Check out this meme : " + local);
                send.setType("text/plane");

                Intent shareIntent = Intent.createChooser(send,"Share with...");
                startActivity(shareIntent);

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              loadImage();

            }
        });

    }

    public void loadImage()
    {

        progressBar = findViewById(R.id.progressBar);
        imageView = findViewById(R.id.imageView);

        progressBar.setVisibility(View.VISIBLE);

        String memeUrl = "https://meme-api.herokuapp.com/gimme";

        OkHttpClient okHttpClient = new OkHttpClient();

        Request request= new Request.Builder()
                .url(memeUrl)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                if(response.isSuccessful())
                {
                    String myResponse = response.body().string();

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject object = new JSONObject(myResponse);

                                String meme = object.getString("url");
                                 local=meme;

                                progressBar.setVisibility(View.INVISIBLE);

                                Picasso.get().load(meme).into(imageView);



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

            }
        });
    }


}