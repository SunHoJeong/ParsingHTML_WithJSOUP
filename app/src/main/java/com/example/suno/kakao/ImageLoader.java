package com.example.suno.kakao;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ImageLoader {
    private String baseUrl = "http://www.gettyimagesgallery.com/";

    private Context context;
    private Retrofit retrofit;
    private HttpService service;

    private int maxMemory;
    private int cacheSize;
    private LruCache<String, Bitmap> memoryCache;

    public ImageLoader(Context context) {
        this.context = context;

        maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        cacheSize = maxMemory / 8;

        memoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(HttpService.class);
    }

    public void loadBitmap(String url, final ImageView imgv) {
        final String imageKey = url;

        final Bitmap bitm = getBitmapFromMemCache(imageKey);

        if (bitm != null) {
            //adapter.addBitmap(bitm);
            imgv.setImageBitmap(bitm);
        } else {
            imgv.setImageResource(R.drawable.img_loading);

            Call<ResponseBody> call = service.getBitmapFromUrl(url);


            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
//                        BitmapFactory.Options options = new BitmapFactory.Options();
//                        options.inJustDecodeBounds = true;
//                        BitmapFactory.decodeStream(response.body().byteStream(), null, options);
//
//                        options.inSampleSize = calculateInSampleSize(options, 100, 100);
//
//                        // display the image data in a ImageView or save it
//                        options.inJustDecodeBounds = false;
                            Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());

                            addBitmapToMemoryCache(imageKey, bitmap);
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

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            memoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return memoryCache.get(key);
    }

}
