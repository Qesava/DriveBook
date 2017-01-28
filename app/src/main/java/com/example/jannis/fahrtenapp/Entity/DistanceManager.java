package com.example.jannis.fahrtenapp.Entity;

import android.util.Log;

/**
 * Created by Jannis on 24.01.2017.
 */

public class DistanceManager {
    private static final DistanceManager m_distanceMangaerInstance = new DistanceManager();
    private float m_distance;
    public static float testdistancemanager;

    public static DistanceManager getDistanceMangerInstance() {
        return m_distanceMangaerInstance;
    }

    private DistanceManager() {
        m_distance = 0;
        testdistancemanager = 0;
    }

    public void addDistance(float distance) {
        m_distance += distance;
        testdistancemanager += distance;
    }

    public void clearDistance() {
        m_distance = 0;
    }

    public float getDistance() {
        return m_distance;
    }
}
