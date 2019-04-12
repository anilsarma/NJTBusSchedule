package com.smartdeviceny.njtsbus.retrofit;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "pr", strict = false)
public class BusPrediction {
    @Element(name ="pt", required = false)
    private String pt="";
    @Element(name ="st", required = false)
    private String st="";

    public String getPt() {
        return pt;
    }

    public void setPt(String pt) {
        this.pt = pt;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    @Override
    public String toString() {
        return "BusPrediction{" + "pt=" + pt + ", st=" + st  + "}";
    }
}