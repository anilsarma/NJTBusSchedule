package com.smartdeviceny.njtsbus.retrofit;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;


@Root(name = "buses", strict = false)
public class BusPredictions {
    @Attribute(name = "id", required = false)
    private String id="";

    @ElementList(inline = true, required = false)
    private List<BusPrediction> bus = new ArrayList<>();





    public List<BusPrediction> getBus() {
        return bus;
    }

    public void setBus(List<BusPrediction> bus) {
        this.bus = bus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "BusPredictions{" + "bus=" + bus + ", id='" + id + '\'' + '}';
    }
}