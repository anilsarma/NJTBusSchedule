package com.smartdeviceny.njtsbus.retrofit;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;


@Root(name = "buses", strict = false)
public class Buses {
    @ElementList(inline = true, required = false)
    private List<Bus> bus = new ArrayList<>();

    @Element(name = "rt", required = false)
    private String rt="";
    @Element(name = "rtRtpiFeedName", required = false)
    private String rtRtpiFeedName;
    @Element(name = "rtdd", required = false)
    private String rtdd="";
    @Element(name = "time", required = false)
    private String time="";

    public List<Bus> getBus() {
        return bus;
    }

    public void setBus(List<Bus> bus) {
        this.bus = bus;
    }

    public String getRt() {
        return rt;
    }

    public void setRt(String rt) {
        this.rt = rt;
    }

    public String getRtRtpiFeedName() {
        return rtRtpiFeedName;
    }

    public void setRtRtpiFeedName(String rtRtpiFeedName) {
        this.rtRtpiFeedName = rtRtpiFeedName;
    }

    public String getRtdd() {
        return rtdd;
    }

    public void setRtdd(String rtdd) {
        this.rtdd = rtdd;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        StringBuffer str = new StringBuffer();
        str.append("Buses [rt = " + rt + ", rtRtpiFeedName = " + rtRtpiFeedName + ", rtdd = " + rtdd + ", time = " + time + "]");
        for(Bus b:bus) {
            str.append("\n" + b.toString());
        }
        return str.toString();
    }
}