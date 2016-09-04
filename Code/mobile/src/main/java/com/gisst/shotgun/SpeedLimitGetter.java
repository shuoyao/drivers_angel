package com.gisst.shotgun;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;


public class SpeedLimitGetter {

    private String before_coordinates = "http://www.overpass-api.de/api/interpreter?data=[out:json];(way[maxspeed~%22.%22](";
    private String after_coordinates = ");%3C;);out;";
    private double boundary = 0.00025;
    private double prev_lat;
    private double prev_lon;
    private double prev_time = -1;

    // Takes in the latitude and longitude of the current location.
    // Returns the speed limit of the closest road in Miles/Hour.
    // Returns -1 if no speed limit is found for the current location.
    public int get_speed_limit(double lat, double lon) {
        prev_lat = lat;
        prev_lon = lon;
        prev_time = System.currentTimeMillis() / 1000;
        String lat_left_bound = Double.toString(lat - boundary);
        String lat_right_bound = Double.toString(lat + boundary);
        String lon_left_bound = Double.toString(lon - boundary);
        String lon_right_bound = Double.toString(lon + boundary);
        String full_addr = before_coordinates + lat_left_bound + "," + lon_left_bound + "," + lat_right_bound + "," + lon_right_bound + after_coordinates;
        System.out.println(full_addr);
        try {
            URL url = new URL(full_addr);
            InputStream input = url.openConnection().getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            StringBuilder response = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            String result = response.toString();
            int limit_index = result.indexOf("\"maxspeed\"");
            if (limit_index == -1) {
                return -1;
            }
            int ret = Integer.parseInt(result.substring(limit_index, limit_index+30).split("[^\\w']+")[2]);
            return ret;

        } catch (IOException e) {
            System.out.println("Error!!!\n");
        }
        return -1;
    }


    // Takes in the lat and lon of the current position.
    // Returns the current speed in Miles/Hour.
    // Returns -1 if neither of the functions (get_curr_speed() or get_speed_limit()) were called before.
    public int get_curr_speed(double lat, double lon) {
        if (prev_time == -1) {
            return -1;
        }
        double curr_time = System.currentTimeMillis() / 1000;
        int ret = (int) (distFrom(prev_lat, prev_lon, lat, lon) / (curr_time - prev_time) * 3600);
        prev_lat = lat;
        prev_lon = lon;
        prev_time = curr_time;
        return ret;

    }


    // Takes in two pairs of lat, lon.
    // Returns the distance between the points in Miles.
    private double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 3958;
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;
        return dist;
    }

    public static void main(String[] args) {
        SpeedLimitGetter getter = new SpeedLimitGetter();
        int result = getter.get_speed_limit(37.887949, -122.298105);
        try {
            Thread.sleep(5000);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        System.out.println(getter.get_curr_speed(37.886678, -122.297792));
    }
}