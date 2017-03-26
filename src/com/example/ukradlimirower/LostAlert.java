package com.example.ukradlimirower;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shybovycha on 23.11.14.
 */
public class LostAlert extends BaseAlert {
    public List<FoundAlert> foundAlerts;

    public LostAlert(int id, String title, String description, String author, double lat, double lon) {
        super(id, title, description, author, lat, lon);

        foundAlerts = new ArrayList<FoundAlert>();
    }
}
