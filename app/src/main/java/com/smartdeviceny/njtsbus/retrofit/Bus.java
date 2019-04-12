package com.smartdeviceny.njtsbus.retrofit;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 <bus>
     <id>7111</id>
     <consist/>
     <cars/>
     <rtpiFeedName/>
     <m>1</m>
     <rt>68</rt>
     <rtRtpiFeedName/>
     <rtdd>68</rtdd>
     <d>South Bound</d>
     <dd>Old Bridge</dd>
     <dn>S</dn>
     <lat>40.754215240478516</lat>
     <lon>-74.02864837646484</lon>
     <pid>728</pid>
     <pd>Old Bridge</pd>
     <pdRtpiFeedName/>
     <run>221</run>
     <fs>68 OLD BRIDGE VIA ROUTE 18</fs>
     <op>479210</op>
     <dip>3756</dip>
     <bid>74858802</bid>
     <wid1>221</wid1>
     <wid2>0068</wid2>
 </bus>
 */
@Root(name = "bus", strict = false)
public class Bus {
    @Element(name ="dd", required = false)
    private String dd="";
    @Element(name ="pdRtpiFeedName", required = false)
    private String pdRtpiFeedName="";
    @Element(name ="op", required = false)
    private String op="";
    @Element(name ="rt", required = false)
    private String rt="";
    @Element(name ="rtRtpiFeedName", required = false)
    private String rtRtpiFeedName;
    @Element(name ="d", required = false)
    private String d="";
    @Element(name ="wid1", required = false)
    private String wid1="";
    @Element(name ="dn", required = false)
    private String dn="";
    @Element(name ="lon", required = false)
    private String lon="";
    @Element(name ="pid", required = false)
    private String pid="";
    @Element(name ="run", required = false)
    private String run="";
    @Element(name ="dip", required = false)
    private String dip="";
    @Element(name ="m", required = false)
    private String m="";
    @Element(name ="fs", required = false)
    private String fs="";
    @Element(name ="consist", required = false)
    private String consist="";
    @Element(name ="cars", required = false)
    private String cars="";
    @Element(name ="rtpiFeedName", required = false)
    private String rtpiFeedName="";
    @Element(name ="pd", required = false)
    private String pd="";
    @Element(name ="rtdd", required = false)
    private String rtdd="";
    @Element(name ="wid2", required = false)
    private String wid2="";
    @Element(name ="id", required = false)
    private String id="";
    @Element(name ="bid", required = false)
    private String bid="";
    @Element(name ="lat", required = false)
    private String lat="";

    public String getDd() {
        return dd;
    }

    public void setDd(String dd) {
        this.dd = dd;
    }

    public String getPdRtpiFeedName() {
        return pdRtpiFeedName;
    }

    public void setPdRtpiFeedName(String pdRtpiFeedName) {
        this.pdRtpiFeedName = pdRtpiFeedName;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
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

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getWid1() {
        return wid1;
    }

    public void setWid1(String wid1) {
        this.wid1 = wid1;
    }

    public String getDn() {
        return dn;
    }

    public void setDn(String dn) {
        this.dn = dn;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getRun() {
        return run;
    }

    public void setRun(String run) {
        this.run = run;
    }

    public String getDip() {
        return dip;
    }

    public void setDip(String dip) {
        this.dip = dip;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public String getFs() {
        return fs;
    }

    public void setFs(String fs) {
        this.fs = fs;
    }

    public String getConsist() {
        return consist;
    }

    public void setConsist(String consist) {
        this.consist = consist;
    }

    public String getCars() {
        return cars;
    }

    public void setCars(String cars) {
        this.cars = cars;
    }

    public String getRtpiFeedName() {
        return rtpiFeedName;
    }

    public void setRtpiFeedName(String rtpiFeedName) {
        this.rtpiFeedName = rtpiFeedName;
    }

    public String getPd() {
        return pd;
    }

    public void setPd(String pd) {
        this.pd = pd;
    }

    public String getRtdd() {
        return rtdd;
    }

    public void setRtdd(String rtdd) {
        this.rtdd = rtdd;
    }

    public String getWid2() {
        return wid2;
    }

    public void setWid2(String wid2) {
        this.wid2 = wid2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return "Bus [dd = " + dd + ", pdRtpiFeedName = " + pdRtpiFeedName + ", op = " + op + ", rt = " + rt + ", rtRtpiFeedName = " + rtRtpiFeedName + ", d = " + d + ", wid1 = " + wid1 + ", dn = " + dn + ", lon = " + lon + ", pid = " + pid + ", run = " + run + ", dip = " + dip + ", m = " + m + ", fs = " + fs + ", consist = " + consist + ", cars = " + cars + ", rtpiFeedName = " + rtpiFeedName + ", pd = " + pd + ", rtdd = " + rtdd + ", wid2 = " + wid2 + ", id = " + id + ", bid = " + bid + ", lat = " + lat + "]";
    }
}