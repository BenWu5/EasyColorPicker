package com.ben.colorpicker.utils;

import android.graphics.Color;

/**
 * Created by Hui on 2015/10/25.
 */
public class ColorUtils {
    public static  String D2HEX(int d){
        int rmove = Color.red(d);
        int gmove = Color.green(d);
        int bmove = Color.blue(d);
        //System.out.println("color rgb");
        String r11 = Integer.toHexString(rmove);
        String g11 = Integer.toHexString(gmove);
        String b11 = Integer.toHexString(bmove);
        return  r11 + g11 + b11;    //十六进制的颜色字符串。
    }


    public static  int getBorW(int color){
        int avg = ((Color.red(color)+Color.green(color)+Color.blue(color))/3);
        if(avg>=100){
            return Color.BLACK;
        }else{
            return Color.WHITE;
        }


    }
}
