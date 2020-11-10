package com.company;

public class Sorter extends Thread {

    private int[] array;

    public Sorter(int[] array){
        this.array = array;
    }

    public void bubbleSort() {
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = i; j < array.length; j++) {
                if (array[i] > array[j]) {
                    int t = array[i];
                    array[i] = array[j];
                    array[j] = t;
                }
            }
        }
    }

    @Override
    public void run() {
        bubbleSort();
    }

}
