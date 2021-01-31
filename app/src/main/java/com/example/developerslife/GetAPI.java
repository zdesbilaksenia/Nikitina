package com.example.developerslife;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetAPI {
    @GET("{category}/{page}?json=true")
    Call<Data> getData(@Path("category") String category, @Path("page") int page);
}
