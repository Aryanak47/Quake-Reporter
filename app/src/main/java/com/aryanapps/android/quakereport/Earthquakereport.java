package com.aryanapps.android.quakereport;

public class Earthquakereport {
    private double mag;
    private String place;
    private Long date;

    public Earthquakereport(double mag, String place, Long date) {
        this.mag = mag;
        this.place = place;
        this.date = date;
    }

    public Earthquakereport() {}

    public double getMag() {
        return mag;
    }

    public String getPlace() {
        return place;
    }

    public Long getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Earthquakereport{" +
                "mag=" + mag +
                ", place='" + place + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
