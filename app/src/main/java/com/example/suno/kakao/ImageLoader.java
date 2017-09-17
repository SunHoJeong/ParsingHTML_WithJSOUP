package com.example.suno.kakao;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by suno on 2017. 9. 17..
 */

public class ImageLoader {
    private String baseUrl = "http://www.gettyimagesgallery.com/";

    private Retrofit retrofit;
    private HttpService service;
    private RecyclerViewAdapter adapter;

    public ImageLoader(RecyclerViewAdapter adapter) {
        this.adapter = adapter;

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(HttpService.class);
    }

    public void getBitmap(String url, final ImageView imgv) {
        Call<ResponseBody> call = service.getBitmapFromUrl(url);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        // display the image data in a ImageView or save it
                        Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                        adapter.addBitmap(bitmap);
                        Log.d("onResponse", bitmap.toString());
                        imgv.setImageBitmap(bitmap);

                    } else {
                        // TODO
                    }
                } else {
                    // TODO
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
