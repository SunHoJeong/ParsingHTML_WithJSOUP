package com.example.suno.kakao;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;


public interface HttpService {
    @GET
    Call<ResponseBody> getBitmapFromUrl(@Url String url);
}
