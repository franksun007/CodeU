package com.codeu.frank.mikey.apktransfer;

import android.util.Log;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by chenfs on 8/11/15.
 *
 * This class is created and funtioning as a security code generator.
 * The only funtion will return a random string with length 6.
 */

public class RandomStringGenerator {

    private List<Character> range;                  // Range of the randomness
    private Random r;                               // Random Obj for random number
    public static final int LENGTH = 6;             // Length
    public static final String TAG = "apktransfer";

    public RandomStringGenerator() {
        r = new Random();
        range = new ArrayList<>();
        for (int i = 'a'; i <= 'z'; i++) {
            range.add((char) i);
        }
        for (int i = 'A'; i <= 'Z'; i++) {
            range.add((char) i);
        }
        for (int i = '0'; i <= '9'; i++) {
            range.add((char) i);
        }
        Log.i(TAG, range.toString());
    }

    // Return a String with length 6 from A-Z a-z 0-9
    public String generateString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < LENGTH; i++) {
            builder.append(range.get(r.nextInt(range.size())));
        }
        return builder.toString();
    }
}
