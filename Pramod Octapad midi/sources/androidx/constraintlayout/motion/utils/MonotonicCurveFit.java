package androidx.constraintlayout.motion.utils;

import java.lang.reflect.Array;

/* JADX INFO: loaded from: classes.dex */
public class MonotonicCurveFit extends CurveFit {
    private static final String TAG = "MonotonicCurveFit";
    private double[] mT;
    private double[][] mTangent;
    private double[][] mY;

    public MonotonicCurveFit(double[] time, double[][] y) {
        int N = time.length;
        int dim = y[0].length;
        double[][] slope = (double[][]) Array.newInstance((Class<?>) Double.TYPE, N - 1, dim);
        double[][] tangent = (double[][]) Array.newInstance((Class<?>) Double.TYPE, N, dim);
        for (int j = 0; j < dim; j++) {
            for (int i = 0; i < N - 1; i++) {
                double dt = time[i + 1] - time[i];
                slope[i][j] = (y[i + 1][j] - y[i][j]) / dt;
                if (i == 0) {
                    tangent[i][j] = slope[i][j];
                } else {
                    tangent[i][j] = (slope[i - 1][j] + slope[i][j]) * 0.5d;
                }
            }
            int i2 = N - 1;
            tangent[i2][j] = slope[N - 2][j];
        }
        for (int i3 = 0; i3 < N - 1; i3++) {
            for (int j2 = 0; j2 < dim; j2++) {
                if (slope[i3][j2] == 0.0d) {
                    tangent[i3][j2] = 0.0d;
                    tangent[i3 + 1][j2] = 0.0d;
                } else {
                    double a = tangent[i3][j2] / slope[i3][j2];
                    double b = tangent[i3 + 1][j2] / slope[i3][j2];
                    double h = Math.hypot(a, b);
                    if (h > 9.0d) {
                        double t = 3.0d / h;
                        tangent[i3][j2] = t * a * slope[i3][j2];
                        tangent[i3 + 1][j2] = t * b * slope[i3][j2];
                    }
                }
            }
        }
        this.mT = time;
        this.mY = y;
        this.mTangent = tangent;
    }

    @Override // androidx.constraintlayout.motion.utils.CurveFit
    public void getPos(double t, double[] v) {
        double[] dArr = this.mT;
        int n = dArr.length;
        int dim = this.mY[0].length;
        if (t <= dArr[0]) {
            for (int j = 0; j < dim; j++) {
                v[j] = this.mY[0][j];
            }
            return;
        }
        if (t >= dArr[n - 1]) {
            for (int j2 = 0; j2 < dim; j2++) {
                v[j2] = this.mY[n - 1][j2];
            }
            return;
        }
        for (int i = 0; i < n - 1; i++) {
            if (t == this.mT[i]) {
                for (int j3 = 0; j3 < dim; j3++) {
                    v[j3] = this.mY[i][j3];
                }
            }
            double[] dArr2 = this.mT;
            if (t < dArr2[i + 1]) {
                double d = dArr2[i + 1];
                double d2 = dArr2[i];
                double h = d - d2;
                double x = (t - d2) / h;
                for (int j4 = 0; j4 < dim; j4++) {
                    double[][] dArr3 = this.mY;
                    double y1 = dArr3[i][j4];
                    double y2 = dArr3[i + 1][j4];
                    double[][] dArr4 = this.mTangent;
                    double t1 = dArr4[i][j4];
                    double t2 = dArr4[i + 1][j4];
                    v[j4] = interpolate(h, x, y1, y2, t1, t2);
                }
                return;
            }
        }
    }

    @Override // androidx.constraintlayout.motion.utils.CurveFit
    public void getPos(double t, float[] v) {
        double[] dArr = this.mT;
        int n = dArr.length;
        int dim = this.mY[0].length;
        if (t <= dArr[0]) {
            for (int j = 0; j < dim; j++) {
                v[j] = (float) this.mY[0][j];
            }
            return;
        }
        if (t >= dArr[n - 1]) {
            for (int j2 = 0; j2 < dim; j2++) {
                v[j2] = (float) this.mY[n - 1][j2];
            }
            return;
        }
        for (int i = 0; i < n - 1; i++) {
            if (t == this.mT[i]) {
                for (int j3 = 0; j3 < dim; j3++) {
                    v[j3] = (float) this.mY[i][j3];
                }
            }
            double[] dArr2 = this.mT;
            if (t < dArr2[i + 1]) {
                double d = dArr2[i + 1];
                double d2 = dArr2[i];
                double h = d - d2;
                double x = (t - d2) / h;
                for (int j4 = 0; j4 < dim; j4++) {
                    double[][] dArr3 = this.mY;
                    double y1 = dArr3[i][j4];
                    double y2 = dArr3[i + 1][j4];
                    double[][] dArr4 = this.mTangent;
                    double t1 = dArr4[i][j4];
                    double t2 = dArr4[i + 1][j4];
                    v[j4] = (float) interpolate(h, x, y1, y2, t1, t2);
                }
                return;
            }
        }
    }

    @Override // androidx.constraintlayout.motion.utils.CurveFit
    public double getPos(double t, int j) {
        double[] dArr = this.mT;
        int n = dArr.length;
        if (t <= dArr[0]) {
            return this.mY[0][j];
        }
        if (t >= dArr[n - 1]) {
            return this.mY[n - 1][j];
        }
        for (int i = 0; i < n - 1; i++) {
            double[] dArr2 = this.mT;
            double d = dArr2[i];
            if (t == d) {
                return this.mY[i][j];
            }
            if (t < dArr2[i + 1]) {
                double h = dArr2[i + 1] - d;
                double x = (t - d) / h;
                double[][] dArr3 = this.mY;
                double y1 = dArr3[i][j];
                double y2 = dArr3[i + 1][j];
                double[][] dArr4 = this.mTangent;
                double t1 = dArr4[i][j];
                double t2 = dArr4[i + 1][j];
                return interpolate(h, x, y1, y2, t1, t2);
            }
        }
        return 0.0d;
    }

    @Override // androidx.constraintlayout.motion.utils.CurveFit
    public void getSlope(double t, double[] v) {
        double t2;
        double[] dArr = this.mT;
        int n = dArr.length;
        int dim = this.mY[0].length;
        if (t <= dArr[0]) {
            t2 = dArr[0];
        } else if (t < dArr[n - 1]) {
            t2 = t;
        } else {
            t2 = dArr[n - 1];
        }
        for (int i = 0; i < n - 1; i++) {
            double[] dArr2 = this.mT;
            if (t2 <= dArr2[i + 1]) {
                double d = dArr2[i + 1];
                double d2 = dArr2[i];
                double h = d - d2;
                double x = (t2 - d2) / h;
                for (int j = 0; j < dim; j++) {
                    double[][] dArr3 = this.mY;
                    double y1 = dArr3[i][j];
                    double y2 = dArr3[i + 1][j];
                    double[][] dArr4 = this.mTangent;
                    double t1 = dArr4[i][j];
                    double t22 = dArr4[i + 1][j];
                    v[j] = diff(h, x, y1, y2, t1, t22) / h;
                }
                return;
            }
        }
    }

    @Override // androidx.constraintlayout.motion.utils.CurveFit
    public double getSlope(double t, int j) {
        double t2;
        double[] dArr = this.mT;
        int n = dArr.length;
        if (t < dArr[0]) {
            t2 = dArr[0];
        } else if (t < dArr[n - 1]) {
            t2 = t;
        } else {
            t2 = dArr[n - 1];
        }
        for (int i = 0; i < n - 1; i++) {
            double[] dArr2 = this.mT;
            if (t2 <= dArr2[i + 1]) {
                double d = dArr2[i + 1];
                double d2 = dArr2[i];
                double h = d - d2;
                double x = (t2 - d2) / h;
                double[][] dArr3 = this.mY;
                double y1 = dArr3[i][j];
                double y2 = dArr3[i + 1][j];
                double[][] dArr4 = this.mTangent;
                double t1 = dArr4[i][j];
                double t22 = dArr4[i + 1][j];
                return diff(h, x, y1, y2, t1, t22) / h;
            }
        }
        return 0.0d;
    }

    @Override // androidx.constraintlayout.motion.utils.CurveFit
    public double[] getTimePoints() {
        return this.mT;
    }

    private static double interpolate(double h, double x, double y1, double y2, double t1, double t2) {
        double x2 = x * x;
        double x3 = x2 * x;
        return (((((((((((-2.0d) * x3) * y2) + ((x2 * 3.0d) * y2)) + ((x3 * 2.0d) * y1)) - ((3.0d * x2) * y1)) + y1) + ((h * t2) * x3)) + ((h * t1) * x3)) - ((h * t2) * x2)) - (((h * 2.0d) * t1) * x2)) + (h * t1 * x);
    }

    private static double diff(double h, double x, double y1, double y2, double t1, double t2) {
        double x2 = x * x;
        return ((((((((((-6.0d) * x2) * y2) + ((x * 6.0d) * y2)) + ((x2 * 6.0d) * y1)) - ((6.0d * x) * y1)) + (((h * 3.0d) * t2) * x2)) + (((3.0d * h) * t1) * x2)) - (((2.0d * h) * t2) * x)) - (((4.0d * h) * t1) * x)) + (h * t1);
    }
}
