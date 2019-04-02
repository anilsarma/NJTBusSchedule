package com.smartdeviceny.njtsbus.route;

import android.location.Location;

public class StopDistance {
    public Stop stop;
    public double miles;
    public Location from;
    public StopDistance(Location from, double miles, Stop stop) {
        this.stop = stop;
        this.from = from;
        this.miles = miles;

    }
}
