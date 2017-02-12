package com.example.jannis.fahrtenapp.Entity;

public class Distance {
    private long id;
    private float m_distance;
    private long start_time;
    private long stop_time;

    public Distance() {
        m_distance = 0;
    }

    public Distance(float distance, long start_time, long stop_time, long id) {
        this.start_time = start_time;
        this.stop_time = stop_time;
        this.m_distance = distance;
        this.id = id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public void setStop_time(long stop_time) {
        this.stop_time = stop_time;
    }

    public void addDistance(float distance) {
        m_distance += distance;
    }

    public float getDistance() {
        return m_distance;
    }

    public long getStart_time() {
        return start_time;
    }

    public long getStop_time() {
        return stop_time;
    }
}
