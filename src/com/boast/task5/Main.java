/*
 * Main.java 15/08/2017
 *
 * Created by Bondarenko Oleh
 */


package com.boast.task5;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class Main {

    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool(8);

        int[] array = getRandomArray(1_000_000, 0, 100);
        //System.out.println(Arrays.toString(array));

        System.out.println("With RecursiveTask:    " + forkJoinPool.invoke(new ArraySum(array)));
        System.out.println("Without RecursiveTask: " + ArraySum.simpleSum(array));
    }

    static int[] getRandomArray(int size, int valueFrom, int valueTo){
        int[] array = new int[size];

        for (int i = 0; i < array.length; i++){
            array[i] =  (int) (Math.random() * (valueTo - valueFrom) + valueFrom);
        }

        return array;
    }
}

class ArraySum extends RecursiveTask<Integer> {
    private final int[] array;

    ArraySum(int[] array) {
        this.array = array;
    }

    protected Integer compute() {
        if (array.length < 20) {
            return simpleSum(array);
        }
        ArraySum f1 = new ArraySum(Arrays.copyOf(array,array.length / 2));
        f1.fork();
        ArraySum f2 = new ArraySum(Arrays.copyOfRange(array, array.length / 2, array.length));
        return f2.compute() + f1.join();
    }

    static int simpleSum(int[] array){
        int res = 0;
        for (int value : array) {
            res += value;
        }
        return res;
    }
}
