package Features;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class TestAlgorithms {

    static class QuickSortInvoker {
        private final Class<?> qsClass;
        private final Method mNoBounds;
        private final Method mWithBounds;

        QuickSortInvoker() {
            try {
                qsClass = Class.forName("Features.QuickSort");
            } catch (Exception e) {
                throw new RuntimeException("Не найден класс Features.QuickSort", e);
            }
            Method noB = null, withB = null;
            for (String name : new String[]{"sort", "quickSort", "quicksort"}) {
                try {
                    Method m = qsClass.getDeclaredMethod(name, int[].class);
                    m.setAccessible(true);
                    noB = m;
                    break;
                } catch (Exception ignored) {}
            }
            for (String name : new String[]{"sort", "quickSort", "quicksort", "mergeSort"}) {
                try {
                    Method m = qsClass.getDeclaredMethod(name, int[].class, int.class, int.class);
                    m.setAccessible(true);
                    withB = m;
                    break;
                } catch (Exception ignored) {}
            }
            this.mNoBounds = noB;
            this.mWithBounds = withB;
            if (mNoBounds == null && mWithBounds == null)
                throw new RuntimeException("В QuickSort не найдены подходящие методы.");
        }

        void sort(int[] a) {
            try {
                if (mNoBounds != null) {
                    mNoBounds.invoke(null, (Object) a);
                } else {
                    mWithBounds.invoke(null, a, 0, a.length - 1);
                }
            } catch (Exception e) {
                throw new RuntimeException("Ошибка вызова QuickSort", e);
            }
        }
    }

    static class QSDepth {
        int maxDepth = 0;
        void sortMeasureDepth(int[] a) {
            rndQS(a, 0, a.length - 1, 0);
        }
        private void rndQS(int[] a, int l, int r, int depth) {
            if (l >= r) return;
            if (depth > maxDepth) maxDepth = depth;
            int p = partitionRandom(a, l, r);
            if (p - 1 - l < r - (p + 1)) {
                rndQS(a, l, p - 1, depth + 1);
                l = p + 1;
            } else {
                rndQS(a, p + 1, r, depth + 1);
                r = p - 1;
            }
            while (l < r) {
                if (depth > maxDepth) maxDepth = depth;
                int q = partitionRandom(a, l, r);
                if (q - 1 - l < r - (q + 1)) {
                    rndQS(a, l, q - 1, depth + 1);
                    l = q + 1;
                } else {
                    rndQS(a, q + 1, r, depth + 1);
                    r = q - 1;
                }
            }
        }
        private int partitionRandom(int[] a, int l, int r) {
            int pi = ThreadLocalRandom.current().nextInt(l, r + 1);
            swap(a, pi, r);
            int pivot = a[r];
            int i = l;
            for (int j = l; j < r; j++) {
                if (a[j] <= pivot) {
                    swap(a, i++, j);
                }
            }
            swap(a, i, r);
            return i;
        }
    }

    static int selectDeterministic(int[] a, int k) {
        try {
            Class<?> cls = Class.forName("Features.DeterministicSelect");
            Method m = cls.getDeclaredMethod("select", int[].class, int.class, int.class, int.class);
            m.setAccessible(true);
            int[] copy = Arrays.copyOf(a, a.length);
            return (int) m.invoke(null, copy, 0, copy.length - 1, k);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось вызвать DeterministicSelect.select", e);
        }
    }

    static double closestFast(double[][] pts) {
        try {
            Class<?> cls = Class.forName("Features.ClosestPairOfPoints");
            Class<?> pointCls = Class.forName("Features.ClosestPairOfPoints$Point");
            Object[] arr = (Object[]) java.lang.reflect.Array.newInstance(pointCls, pts.length);
            for (int i = 0; i < pts.length; i++) {
                arr[i] = pointCls.getConstructor(double.class, double.class).newInstance(pts[i][0], pts[i][1]);
            }
            Method m = cls.getDeclaredMethod("closestPair", java.lang.reflect.Array.newInstance(pointCls, 0).getClass());
            m.setAccessible(true);
            return (double) m.invoke(null, (Object) arr);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось вызвать ClosestPairOfPoints.closestPair", e);
        }
    }

    static double closestBruteforce(double[][] pts) {
        double best = Double.POSITIVE_INFINITY;
        for (int i = 0; i < pts.length; i++) {
            for (int j = i + 1; j < pts.length; j++) {
                double dx = pts[i][0] - pts[j][0];
                double dy = pts[i][1] - pts[j][1];
                double d = Math.hypot(dx, dy);
                if (d < best) best = d;
            }
        }
        return best;
    }

    static int[] randomArray(int n, int bound) {
        int[] a = new int[n];
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        for (int i = 0; i < n; i++) a[i] = rnd.nextInt(bound);
        return a;
    }

    static int[] adversarialSorted(int n) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = i;
        return a;
    }

    static int[] adversarialReversed(int n) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = n - 1 - i;
        return a;
    }

    static int[] adversarialManyDuplicates(int n, int kinds) {
        int[] a = new int[n];
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        for (int i = 0; i < n; i++) a[i] = rnd.nextInt(kinds);
        return a;
    }

    static int[] adversarialAlternating(int n) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = (i % 2 == 0) ? 0 : 1;
        return a;
    }

    static double[][] randomPoints(int n, double range) {
        double[][] p = new double[n][2];
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        for (int i = 0; i < n; i++) {
            p[i][0] = rnd.nextDouble(-range, range);
            p[i][1] = rnd.nextDouble(-range, range);
        }
        return p;
    }

    static void swap(int[] a, int i, int j) { int t = a[i]; a[i] = a[j]; a[j] = t; }

    static void testSortingCorrectnessAndDepth() {
        System.out.println("== Sorting: correctness + recursion depth (instrumented randomized QS) ==");
        QuickSortInvoker invoker = new QuickSortInvoker();
        List<int[]> tests = new ArrayList<>();
        tests.add(randomArray(10000, 1_000_000));
        tests.add(adversarialSorted(20000));
        tests.add(adversarialReversed(20000));
        tests.add(adversarialManyDuplicates(20000, 5));
        tests.add(adversarialAlternating(20000));
        int caseId = 1;
        for (int[] original : tests) {
            int[] a1 = Arrays.copyOf(original, original.length);
            int[] a2 = Arrays.copyOf(original, original.length);
            invoker.sort(a1);
            Arrays.sort(a2);
            boolean ok = Arrays.equals(a1, a2);
            int[] forDepth = Arrays.copyOf(original, original.length);
            QSDepth depthMeter = new QSDepth();
            depthMeter.sortMeasureDepth(forDepth);
            int n = forDepth.length;
            int bound = 2 * (int) Math.floor(Math.log(n) / Math.log(2)) + 20;
            System.out.printf(Locale.US,
                    "Case %d: n=%d, sorted=%s, depth=%d, bound≈%d%n",
                    caseId++, n, ok ? "OK" : "FAIL", depthMeter.maxDepth, bound);
            if (!ok) throw new AssertionError("Sorting FAILED on case " + (caseId - 1));
        }
    }

    static void testMergeSort() {
        System.out.println("\n== MergeSort: correctness test ==");
        try {
            Class<?> cls = Class.forName("Features.MergeSort");
            Method m = cls.getDeclaredMethod("mergeSort", int[].class);
            m.setAccessible(true);

            int caseId = 1;
            for (int t = 1; t <= 5; t++) { // 5 случаев как у QuickSort
                int n;
                int[] a;
                switch (t) {
                    case 1: n = 10000; a = randomArray(n, 1_000_000); break;
                    case 2: n = 20000; a = adversarialSorted(20000); break;
                    case 3: n = 20000; a = adversarialReversed(20000); break;
                    case 4: n = 20000; a = adversarialManyDuplicates(20000, 5); break;
                    default: n = 20000; a = adversarialAlternating(20000); break;
                }

                int[] b = Arrays.copyOf(a, a.length);
                Arrays.sort(b);

                int[] copy = Arrays.copyOf(a, a.length);
                m.invoke(null, (Object) copy);

                boolean ok = Arrays.equals(copy, b);
                System.out.printf("Case %d: n=%d, sorted=%s%n",
                        caseId++, n, ok ? "OK" : "FAIL");

                if (!ok) throw new AssertionError("MergeSort FAILED on case " + (caseId - 1));
            }
        } catch (Exception e) {
            throw new RuntimeException("Не удалось вызвать MergeSort.mergeSort(int[])", e);
        }
    }



    static void testDeterministicSelect() {
        System.out.println("\n== Select: compare with Arrays.sort(a)[k] over 100 random trials ==");
        int trials = 100;
        for (int t = 1; t <= trials; t++) {
            int n = ThreadLocalRandom.current().nextInt(1, 5000);
            int[] a = randomArray(n, 1_000_000);
            int k = ThreadLocalRandom.current().nextInt(0, n);
            int expected = Arrays.stream(Arrays.copyOf(a, n)).sorted().toArray()[k];
            int got = selectDeterministic(a, k);
            if (expected != got) {
                throw new AssertionError(String.format("Trial %d FAILED: expected %d, got %d (k=%d, n=%d)",
                        t, expected, got, k, n));
            }
            if (t % 20 == 0) System.out.println("  passed " + t + " / " + trials);
        }
        System.out.println("  All trials passed.");
    }

    static void testClosestPair() {
        System.out.println("\n== Closest Pair: validate vs O(n^2) for n ≤ 2000; fast-only for large n ==");
        for (int n : new int[]{50, 200, 500, 1000, 2000}) {
            double[][] pts = randomPoints(n, 1e6);
            double fast = closestFast(pts);
            double slow = closestBruteforce(pts);
            if (Math.abs(fast - slow) > 1e-9) {
                throw new AssertionError(String.format(Locale.US,
                        "ClosestPair mismatch at n=%d: fast=%.12f, slow=%.12f", n, fast, slow));
            }
            System.out.printf(Locale.US, "  n=%d check OK (fast=slow=%.6f)%n", n, fast);
        }
        for (int n : new int[]{50_000, 100_000}) {
            double[][] pts = randomPoints(n, 1e6);
            long t0 = System.currentTimeMillis();
            double fast = closestFast(pts);
            long t1 = System.currentTimeMillis();
            System.out.printf(Locale.US, "  n=%d fast-only: d=%.6f, time=%d ms%n", n, fast, (t1 - t0));
        }
    }

    public static void main(String[] args) {
        testSortingCorrectnessAndDepth();
        testMergeSort();
        testDeterministicSelect();
        testClosestPair();
        System.out.println("\nAll tests completed.");
    }
}
