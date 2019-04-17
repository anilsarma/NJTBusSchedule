package com.smartdeviceny.njtsbus.model;

import com.smartdeviceny.njtsbus.route.Stop;

import java.util.ArrayList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BusRoutesModelView extends ViewModel {
    MutableLiveData<String> startStation = new MutableLiveData<>();
    MutableLiveData<String> stopStation = new MutableLiveData<>();
    MutableLiveData<ArrayList<Stop>> stops = new MutableLiveData<>();

    public BusRoutesModelView() {
        startStation.setValue("WASHINGTON BLVD AT NEWPORT PKWAY");
        stopStation.setValue("RT-18 AT RACE TRACK RD");

    }

    public LiveData<String> getStartStation() {
        return startStation;
    }

    public void setStartStation(String startStation) {
        this.startStation.postValue(startStation);
    }
    public LiveData<String> getStopStation() {
        return stopStation;
    }

    public void setStopStation(String stopStation) {
        this.stopStation.postValue(stopStation);
    }

    public LiveData<ArrayList<Stop>> getStops() {
        return stops;
    }

    public void setStops(ArrayList<Stop> stops) {
        this.stops.postValue(stops);
    }

}
