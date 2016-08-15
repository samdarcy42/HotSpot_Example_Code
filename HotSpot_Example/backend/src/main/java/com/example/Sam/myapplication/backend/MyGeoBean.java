package com.example.Sam.myapplication.backend;

import com.google.appengine.api.utils.SystemProperty;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MyGeoBean {

    String url = null;
    boolean updatedRow = true;
    String message = "Backend Reached";

    public MyGeoBean(int var1, String location) {

        if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {

            try {

                Class.forName("com.mysql.jdbc.GoogleDriver");

            } catch (ClassNotFoundException e) {

                e.printStackTrace();

            }

            url = "jdbc:google:mysql://keen-answer-95700:hotspotdb?user=root";

        } else {

            // Connecting from an external network.

            try {

                Class.forName("com.mysql.jdbc.Driver");

            } catch (ClassNotFoundException e) {

                e.printStackTrace();

            }

            url = "jdbc:mysql://207.159.171.241:3306?user=root";

        }


        Connection conn = null;

        try {

            conn = DriverManager.getConnection(url);

            String statement = "Count(*) from hotspotdb.USER where geofenceID=" + location;

            PreparedStatement stmt = conn.prepareStatement(statement);

            stmt.setInt(1, var1);

            updatedRow = stmt.execute();


        } catch (SQLException e) {


            e.printStackTrace();
            message += e.getMessage();
        }

        try {


            conn.close();

        } catch (SQLException e) {

            e.printStackTrace();
            message += e.getMessage();
        }


    }

    public String getMessage() {
        return message;
        //return "row updated: "+updatedRow ;

    }
}


