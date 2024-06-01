package com.sid.manage.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class OptUtil {

    public String generateOpt(){
        Random r = new Random();
        int randomNum = r.nextInt(999999);
        String output = Integer.toString(randomNum);
        // au lieu d'avoir 1234 on trouve 001234 tant que output<6
        while (output.length() < 6) {
            output="0"+output;
        }
        return output;
    }
}
