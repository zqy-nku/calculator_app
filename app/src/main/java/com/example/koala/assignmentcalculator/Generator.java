package com.example.koala.assignmentcalculator;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by qyzhu on 10/24/17.
 */

public class Generator {

    private Random random;

    public Generator() {
        random = new Random(System.currentTimeMillis());
    }

    private int getRandom() {
        return -99 + random.nextInt(198);
    }

    public int[] generateFirst() {
        int[] res = new int[2];

        for (int i = 0; i < res.length; ++i)
            res[i] = getRandom();
        while (res[0] == 0) res[0] = getRandom();

        return res;
    }

    public double solveFirst(int[] input) throws WrongException {
        double opt = 0.0;
        double opt_1 = 0.0;
        String opt_2;
        double opt_3;
        if (input.length != 2) throw new WrongException("Wrong parameter number");

        opt_3 = (double) (0 - input[1]) / input[0];
        //opt=(double)(0 - input[1]) / input[0];
        opt_2 = String.format("%.2f", opt_3);
        //opt_1=(Math.round(opt*100)/100.0);   /*round their answers to 2 decimal places in case their answers are not integers */
        opt_1 = Double.parseDouble(opt_2);
        return opt_1;

    }

    public int[] generateSecond() {
        int[] res = new int[3];

        for (int i = 0; i < res.length; ++i)
            res[i] = getRandom();

        while (res[0] == 0) res[0] = getRandom();
        while (res[1] == 0) res[1] = getRandom();

        while (res[1] * res[1] - 4 * res[0] * (res[2] - 0) < 0) res[2] = getRandom();

        return res;
    }

    public double[] solveSecond(int[] input) throws WrongException {
        if (input.length != 3) throw new WrongException("Wrong parameter number");

        int a = input[0];
        int b = input[1];
        int c = input[2] - 0;

        if (a == 0) {
            int[] newArray = Arrays.copyOfRange(input, 1, 4);
            double[] res = new double[2];
            String opt_1;
            res[0] = solveFirst(newArray);
            res[0] = (double) (res[0] * 100) / 100.00;
            opt_1 = String.format("%.2f", res[0]);
            res[0] = Double.parseDouble(opt_1);
            res[1] = res[0];
            return res;
        }

        if (b * b - 4 * a * c < 0) throw new WrongException("Not root");

        double tem = Math.sqrt(b * b - 4 * a * c);
        double[] res = new double[2];
        String opt_0;
        String opt_1;

        res[0] = (-b + tem) / (2 * a);
        res[0] = (double) (res[0] * 100) / 100.0;
        opt_0 = String.format("%.2f", res[0]);
        res[0] = Double.parseDouble(opt_0);

        res[1] = (-b - tem) / (2 * a);
        res[1] = (double) (res[1] * 100) / 100.0;
        opt_1 = String.format("%.2f", res[1]);
        res[1] = Double.parseDouble(opt_1);

        return res;
    }

    class WrongException extends Exception {
        public WrongException(String message) {
            super(message);
        }
    }
}
