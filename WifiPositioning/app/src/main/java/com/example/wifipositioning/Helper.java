package com.example.wifipositioning;

import java.util.Random;
import java.util.TimerTask;

public class Helper extends TimerTask {
    public static int i = 1;
    public static Random random = new Random();

    private RandomCallback randomCallback = null;

    public Helper(RandomCallback randomCallback) {
        this.randomCallback = randomCallback;
    }

    @Override
    public void run() {
        int randomNum = random.nextInt(-50 - (-60) + 1) + (-60);
        System.out.println("This is called " + i++ + " time");
        System.out.println("Number: " + randomNum );
        if (randomCallback != null) {
            randomCallback.onReceiveValue(randomNum);
        }
    }

    interface RandomCallback {
        void onReceiveValue(int value);
    }
}
