package Features;
import java.util.Scanner;

import java.util.concurrent.ThreadLocalRandom;

public final class QuickSort {

    public static void sort(int[] arr) {
        if (arr == null || arr.length < 2) return;
        quicksort(arr, 0, arr.length - 1);
    }

    private static void quicksort(int[] a, int left, int right) {
        while (left < right) {
            int mid = partitionWithRandomPivot(a, left, right);

            if (mid - left < right - (mid + 1)) {
                quicksort(a, left, mid);
                left = mid + 1;
            } else {
                quicksort(a, mid + 1, right);
                right = mid;
            }
        }
    }

    private static int partitionWithRandomPivot(int[] a, int left, int right) {
        int pivotIdx = ThreadLocalRandom.current().nextInt(left, right + 1);
        int pivot = a[pivotIdx];
        swap(a, left, pivotIdx);

        int i = left - 1;
        int j = right + 1;
        while (true) {
            do { i++; } while (a[i] < pivot);
            do { j--; } while (a[j] > pivot);
            if (i >= j) return j;
            swap(a, i, j);
        }
    }

        private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter the size of the array: ");
        int size = sc.nextInt();

        int[] arr = new int[size];

        System.out.println("Enter " + size + " elements: ");
        for (int i = 0; i < size; i++) {
            arr[i] = sc.nextInt();
        }

        System.out.println("Original array:");
        MergeSort.printArray(arr);

        sort(arr);

        System.out.println("Sorted array :");
        MergeSort.printArray(arr);
    }

}
