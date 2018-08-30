package com.example.diego.chatapp.manager;

import java.util.Random;

public class RandomManager {

    private static final Random random = new Random(System.currentTimeMillis());

    public static int getInt(int min, int max){
        return random.nextInt(max-min+1) + min;
    }
}
