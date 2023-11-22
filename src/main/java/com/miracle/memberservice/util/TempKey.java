package com.miracle.memberservice.util;

import java.util.Random;

public class TempKey{
    public static String make(){
        StringBuffer sb = new StringBuffer(6);
        char[] alphabets = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        Random random = new Random();
        for(int i=0; i<3; i++){
            sb.append(alphabets[random.nextInt(26)]);
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}