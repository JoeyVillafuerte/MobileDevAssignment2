package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedLocationViewModel extends ViewModel {
    // MutableLiveData to hold the Location data
    private final MutableLiveData<Location> data = new MutableLiveData<>();


    // Updates the value of the Location data
    public void setData(Location value) {
        data.setValue(value);
    }

    public LiveData<Location> getData() {
        return data;
    }
}
