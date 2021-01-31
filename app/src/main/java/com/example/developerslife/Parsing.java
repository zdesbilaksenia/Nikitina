package com.example.developerslife;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Parsing {
    private Callback callback;

    public Parsing(Callback callback) {
        this.callback = callback;
    }

    public void ParseJSON(String category, int page){
        String BASE_URL = "https://developerslife.ru/";
        GetAPI api;
        api = Parsing.getAPI(BASE_URL);
        Call<Data> apiData = api.getData(category, page);
        apiData.enqueue(new retrofit2.Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                Data data = response.body();
                ArrayList<Result> temp = new ArrayList<>();
                for (int i =0;i<data.getResult().size();i++)
                    temp.add(data.getResult().get(i));
                callback.response(temp);
            }
            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                callback.response(null);
            }
        });
    }

    public static GetAPI getAPI(String url){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        GetAPI api = retrofit.create(GetAPI.class);
        return api;
    }
}
