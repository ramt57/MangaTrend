package com.manga.ramt57.mangareader.trend.pojomodels;

import java.util.ArrayList;

/**
 * Created by user on 15-09-2017.
 */

public class Mangalist {
    String a;   //alias
    ArrayList<String> c;    //category
    String h;   //hits
    String i;   //id
    String im;  //img url
    String ld;  //last date
    String s;   //status
    String t;   // title
    public Mangalist(){

    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public ArrayList<String> getC() {
        return c;
    }

    public void setC(ArrayList<String> c) {
        this.c = c;
    }

    public String getH() {
        return h;
    }

    public void setH(String h) {
        this.h = h;
    }

    public String getI() {
        return i;
    }

    public void setI(String i) {
        this.i = i;
    }

    public String getIm() {
        return im;
    }

    public void setIm(String im) {
        this.im = im;
    }

    public String getLd() {
        return ld;
    }

    public void setLd(String ld) {
        this.ld = ld;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }
}
