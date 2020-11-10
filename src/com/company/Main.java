package com.company;

import java.util.Random;

public class Main {

    private static final int MAX = 10_000_000;
    private static final int N = 100000;
    //static int k = 1000;

    public static void main(String[] args) {
        int[] array = generate(N);
        int k = 2;
        long prev_time = 1<<21, curr_time = 8000 ;
        int q = 15;
//        int[] left = new int[N/2];
//        int[] right = new int[N - N/2];
//        for (int i = 0; i < array.length; i++) {
//            if (i < array.length / 2) {
//                left[i] = array[i];
//            } else {
//                right[i - array.length / 2] = array[i];
//            }
//        }
        while (q!=0){
            long start = System.currentTimeMillis();
            int p_len = N/k;
            int t_len = N-(N/k)*(k-1);
            int[][] parts = new int[k-1][p_len];
            int[] trailing = new int[t_len];
            for (int i = 0; i < k-1; i++) {
                System.arraycopy(array, i*p_len, parts[i], 0, p_len);
            }
            System.arraycopy(array, p_len*(k-1), trailing, 0, t_len);

//Sorter sorterLeft = new Sorter(left);
//Sorter sorterRight = new Sorter(right);

//sorterLeft.start();
//sorterRight.start();
//
//// сюрприз
//try {
//    sorterLeft.join();
//    sorterRight.join();
//} catch (InterruptedException e) {
//    e.printStackTrace();
//}

            Sorter[] workers = new Sorter[k];
            try {
                for (int i = 0; i < k-1; i++) {
                    workers[i] = new Sorter(parts[i]);
                    workers[i].start();
                    workers[i].join();
                }
                workers[k-1] = new Sorter(trailing);
                workers[k-1].start();
                workers[k-1].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int[] prev_res = parts[0];
            for (int i = 1; i < k-1; i++) {
                int[] res = merge(prev_res, parts[i]);
                prev_res = res;
            }
            int[] res = merge(prev_res,trailing);

//        int[] array2 = merge(left, right);
            long finish = System.currentTimeMillis();
            System.out.println(isSorted(res));
            prev_time = curr_time;
            curr_time = finish-start;
            System.out.println(k+" " + curr_time + " ms");
            if(curr_time>=prev_time){
                q--;
            }
            k+=5;

//        start = System.currentTimeMillis();
//        int[] sortedArray = bubbleSort(array);
//        finish = System.currentTimeMillis();
//
//        System.out.println(isSorted(sortedArray));
//        System.out.println(finish - start + " ms");
        }

    }

    public static int[] generate(int n) {
        Random r = new Random();
        int[] array = new int[n];
        for (int i = 0; i < n; i++) {
            array[i] = r.nextInt(MAX);
        }
        return array;
    }

    public static int[] bubbleSort(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = i; j < array.length; j++) {
                if (array[i] > array[j]) {
                    int t = array[i];
                    array[i] = array[j];
                    array[j] = t;
                }
            }
        }
        return array;
    }

    public static boolean isSorted(int[] array) {
        boolean sorted = true;
        for (int i = 0; i < array.length - 1; i++) {
            if(array[i] > array[i+1]){
                sorted = false;
            }
        }
        return sorted;
    }

    public static int[] merge(int[] left, int[] right) {
        int leftInd = 0;
        int rightInd = 0;
        int[] newArray = new int[left.length + right.length];
        for (int i = 0; i < newArray.length; i++) {
            if (leftInd >= left.length || rightInd >= right.length) {
                break;
            }
            if (left[leftInd] < right[rightInd]) {
                newArray[i] = left[leftInd];
                leftInd++;
            } else {
                newArray[i] = right[rightInd];
                rightInd++;
            }
        }
        if (leftInd == left.length) {
            for (int i = rightInd; i < right.length; i++) {
                newArray[leftInd + i] = right[i];
            }
        } else {
            for (int i = leftInd; i < left.length; i++) {
                newArray[rightInd + i] = left[i];
            }
        }
        return newArray;
    }

}
