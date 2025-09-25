package Features;

import java.util.Scanner;


public class MergeSort {
    private static final int CUTOFF = 10;

    public static void mergeSort(int[] arr) {
        int[] buffer = new int[arr.length];
        mergeSort(arr, buffer, 0, arr.length - 1);
    }

    private static void mergeSort(int[] arr, int[] buffer, int left, int right) {
        if (right - left + 1 <= CUTOFF) {
            insertionSort(arr, left, right);
            return;
        }

        if (left < right) {
            int mid = (left + right) / 2;

            mergeSort(arr, buffer, left, mid);
            mergeSort(arr, buffer, mid + 1, right);

            merge(arr, buffer, left, mid, right);
        }
    }


    private static void merge(int[] arr, int[] buffer, int left, int mid, int right) {
        for (int i = left; i <= right; i++) {
            buffer[i] = arr[i];
        }

        int start_left = left;
        int start_right = mid + 1;
        int main_index = left;

        while (start_left <= mid && start_right <= right) {
            if (buffer[start_left] <= buffer[start_right]) {
                arr[main_index] = buffer[start_left];
                main_index++;
                start_left++;
            } else {
                arr[main_index] = buffer[start_right];
                main_index++;
                start_right++;
            }
        }
        while (start_left <= mid) {
            arr[main_index] = buffer[start_left];
            main_index++;
            start_left++;
        }
    }

    private static void insertionSort(int[] arr, int left, int right) {
        for (int i = left + 1; i <= right; i++) {
            int key = arr[i];
            int j = i - 1;

            while (j >= left && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    public static void printArray(int[] arr) {
        for (int num : arr) {
            System.out.print(num + " ");
        }
        System.out.println();
    }


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter the size of the array: ");
        int size = sc.nextInt();
        int[] arr = new int[size];

        System.out.print("Enter " + size + " elements:");
        for (int i = 0; i < size; i++) {
            arr[i] = sc.nextInt();
        }

        System.out.println("Original array:");
        printArray(arr);

        mergeSort(arr);

        System.out.println("\nSorted array:");
        printArray(arr);
    }
}
