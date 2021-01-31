package com.example.developerslife;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Logic extends ViewModel implements Callback {

    MutableLiveData<ArrayList<Result>> liveData = new MutableLiveData();

    public LiveData<ArrayList<Result>> getLiveData(String category, int page){
        if (liveData == null) {
            liveData = new MutableLiveData<>();
        }

        return liveData;
    }


    @Override
    public void response(ArrayList<Result> item) {
        liveData.postValue(item);
    }
}
