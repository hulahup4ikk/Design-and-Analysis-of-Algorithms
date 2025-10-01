package Features;

import java.util.*;

public class ClosestPairOfPoints {

    public static class Point {
        public final double x, y;
        public Point(double x, double y) { this.x = x; this.y = y; }
    }

    public static double closestPair(Point[] pts) {
        Point[] px = pts.clone();
        Arrays.sort(px, Comparator.comparingDouble(p -> p.x));
        Point[] py = px.clone();
        Arrays.sort(py, Comparator.comparingDouble(p -> p.y));
        return closest(px, py, 0, pts.length - 1);
    }

    private static double closest(Point[] px, Point[] py, int lo, int hi) {
        if (hi - lo <= 3) return brute(px, lo, hi);

        int mid = (lo + hi) >>> 1;
        double midx = px[mid].x;

        Point[] pyl = new Point[mid - lo + 1];
        Point[] pyr = new Point[hi - mid];
        int li = 0, ri = 0;
        for (Point p : py) {
            if (p.x <= midx && li < pyl.length) pyl[li++] = p;
            else pyr[ri++] = p;
        }

        double dl = closest(px, pyl, lo, mid);
        double dr = closest(px, pyr, mid + 1, hi);
        double d = Math.min(dl, dr);

        return Math.min(d, stripClosest(py, midx, d));
    }

    private static double stripClosest(Point[] py, double midx, double d) {
        List<Point> strip = new ArrayList<>();
        for (Point p : py) if (Math.abs(p.x - midx) < d) strip.add(p);

        double min = d;
        for (int i = 0; i < strip.size(); i++) {
            for (int j = i + 1; j < strip.size() && (strip.get(j).y - strip.get(i).y) < min; j++) {
                min = Math.min(min, dist(strip.get(i), strip.get(j)));
            }
        }
        return min;
    }

    private static double brute(Point[] pts, int lo, int hi) {
        double d = Double.POSITIVE_INFINITY;
        for (int i = lo; i <= hi; i++) {
            for (int j = i + 1; j <= hi; j++) {
                d = Math.min(d, dist(pts[i], pts[j]));
            }
        }
        return d;
    }

    private static double dist(Point a, Point b) {
        double dx = a.x - b.x, dy = a.y - b.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public static void main(String[] args) {
        Point[] pts = {
                new Point(2, 3), new Point(12, 30),
                new Point(40, 50), new Point(5, 1),
                new Point(12, 10), new Point(3, 4)
        };
        System.out.println("Closest distance = " + closestPair(pts));
    }
}
