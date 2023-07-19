package com.example.caltracker.Util;

import java.text.DecimalFormat;

public class Utils {
    //static method to format calorie number
    public static String formatNumber(int value){
        //set format with DecimalFormat class
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        //return formatted value in string
        return formatter.format(value);
    }

}
