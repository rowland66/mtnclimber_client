package org.rowland.trecktraker;

/**
 * Created by smith on 7/23/2017.
 */
class Mountain {
    String name;
    int rank;
    int height;
    int position;
    Double Long;
    Double Lat;
    Mountain(String line) {
        String[] data = line.split(",");
        name = data[0];
        rank = Integer.parseInt(data[1]);
        height = Integer.parseInt(data[2]);
        Long = Double.parseDouble(data[3]);
        Lat = Double.parseDouble(data[4]);
    }
}
