package com.tusuapp.coreapi.utils;


import java.util.Arrays;
import java.util.Random;

public class OTPUtil {

    public static String generateOTP(int len)
    {
        String numbers = "0123456789";
        Random rndm_method = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < len; i++)
        {
            builder.append(numbers.charAt(rndm_method.nextInt(numbers.length())));
        }
        return builder.toString();
    }


}
