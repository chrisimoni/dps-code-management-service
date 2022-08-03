package com.interswitch.dps.codemanagement.utils;

import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

/**
 *
 */
@Slf4j
public class RandomUtil {

    public static final String UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final String LOWER_CASE = UPPER_CASE.toLowerCase(Locale.ROOT);

    public static final String NUMBER_DIGITS = "0123456789";

    public static final String ALPHA_NUMERIC = UPPER_CASE + NUMBER_DIGITS;

    private final SecureRandom random;

    private final char[] symbols;

    private final char[] buf;

    public RandomUtil(int length, SecureRandom random, String symbols) {
        if (length < 1) throw new IllegalArgumentException();
        if (symbols.length() < 2) throw new IllegalArgumentException();
        this.random = Objects.requireNonNull(random);
        this.symbols = symbols.toCharArray();
        this.buf = new char[length];
    }

    /**
     * Create an alphanumeric string generator.
     */
    public RandomUtil(int length, SecureRandom random) {
        this(length, random, ALPHA_NUMERIC);
    }

    /**
     * Create an alphanumeric strings from a secure generator.
     */
    public RandomUtil(int length) {
        this(length, new SecureRandom());
    }

    /**
     * Create session identifiers.
     */
    public RandomUtil() {
        this(21);
    }

    public static String getRandomNumber(int n) {
        String randomCode = "";
        int count = 0;
        do {
            if(count>=1){
                log.info("Number of times code-generation attempt not equal to length of code ===>"+count);
            }
            long randomNumber = generateRandomDigits(n);
            randomCode = String.format("%d", randomNumber);
            count++;
            if (count > 5) {
                break;
            }
        } while (randomCode.trim().length() < n && count <= 5);
        return randomCode;

    }

    /**
     * @param n the number of digits the code to be generated must comprise.
     * @return a random number to be used as OTP code
     */
    private static long generateRandomDigits(int n) {
//        SecureRandom rand = new SecureRandom();
//        long x = (long) (rand.nextDouble() * Math.pow(10, n));
        return new SecureRandom().nextInt((9 * (int) Math.pow(10, n - 1)) - 1)
                + (int) Math.pow(10, n - 1);
    }

    public String nextString() {
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        return new String(buf);
    }

}
