package Features;

import java.util.Arrays;

public class DeterministicSelect {

    public static int select(int[] arr, int left, int right, int k) {
        while (true) {
            if (left == right) return arr[left];
            int pivot = medianOfMedians(arr, left, right);
            int[] rg = partition3(arr, left, right, pivot);
            int lt = rg[0], gt = rg[1];
            int leftSize = lt - left;
            int midSize = gt - lt + 1;
            if (k < left + leftSize) {
                right = lt - 1;
            } else if (k < left + leftSize + midSize) {
                return pivot;
            } else {
                left = gt + 1;
            }
        }
    }

    private static int[] partition3(int[] a, int l, int r, int pivot) {
        int lt = l, i = l, gt = r;
        while (i <= gt) {
            if (a[i] < pivot) {
                swap(a, lt++, i++);
            } else if (a[i] > pivot) {
                swap(a, i, gt--);
            } else {
                i++;
            }
        }
        return new int[]{lt, gt};
    }

    private static int medianOfMedians(int[] a, int l, int r) {
        int n = r - l + 1;
        if (n <= 5) {
            Arrays.sort(a, l, r + 1);
            return a[l + n / 2];
        }
        int numGroups = (n + 4) / 5;
        int[] medians = new int[numGroups];
        int idx = 0;
        for (int i = l; i <= r; i += 5) {
            int sr = Math.min(i + 4, r);
            Arrays.sort(a, i, sr + 1);
            int size = sr - i + 1;
            medians[idx++] = a[i + size / 2];
        }
        return medianOfMedians(medians, 0, medians.length - 1);
    }

    private static void swap(int[] a, int i, int j) {
        int t = a[i]; a[i] = a[j]; a[j] = t;
    }

    public static void main(String[] args) {
        int[] arr = {12, 3, 5, 7, 4, 19, 26, 12, 12};
        for (int k = 0; k < arr.length; k++) {
            int res = select(Arrays.copyOf(arr, arr.length), 0, arr.length - 1, k);
            int expected = Arrays.stream(arr).sorted().toArray()[k];
            System.out.printf("k=%d -> %d (expected %d)%n", k, res, expected);
        }
    }
}
