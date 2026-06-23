package androidx.constraintlayout.motion.utils;

/* JADX INFO: loaded from: classes.dex */
public class LinearCurveFit extends CurveFit {
    private static final String TAG = "LinearCurveFit";
    private double[] mT;
    private double mTotalLength;
    private double[][] mY;

    public LinearCurveFit(double[] time, double[][] y) {
        int dim;
        double px;
        this.mTotalLength = Double.NaN;
        int length = time.length;
        char c = 0;
        int dim2 = y[0].length;
        this.mT = time;
        this.mY = y;
        if (dim2 > 2) {
            double sum = 0.0d;
            double lastx = 0.0d;
            double lasty = 0.0d;
            int i = 0;
            while (i < time.length) {
                double px2 = y[i][c];
                double py = y[i][c];
                if (i <= 0) {
                    dim = dim2;
                    px = px2;
                } else {
                    dim = dim2;
                    px = px2;
                    sum += Math.hypot(px2 - lastx, py - lasty);
                }
                lastx = px;
                lasty = py;
                i++;
                dim2 = dim;
                c = 0;
            }
            this.mTotalLength = 0.0d;
        }
    }

    private double getLength2D(double t) {
        if (Double.isNaN(this.mTotalLength)) {
            return 0.0d;
        }
        double[] dArr = this.mT;
        int n = dArr.length;
        if (t <= dArr[0]) {
            return 0.0d;
        }
        if (t >= dArr[n - 1]) {
            return this.mTotalLength;
        }
        double sum = 0.0d;
        double last_x = 0.0d;
        double last_y = 0.0d;
        for (int i = 0; i < n - 1; i++) {
            double[] dArr2 = this.mY[i];
            double px = dArr2[0];
            double py = dArr2[1];
            if (i > 0) {
                double d = px - last_x;
                double last_x2 = py - last_y;
                sum += Math.hypot(d, last_x2);
            }
            last_x = px;
            last_y = py;
            double[] dArr3 = this.mT;
            double d2 = dArr3[i];
            if (t == d2) {
                return sum;
            }
            if (t < dArr3[i + 1]) {
                double h = dArr3[i + 1] - d2;
                double x = (t - d2) / h;
                double[][] dArr4 = this.mY;
                double[] dArr5 = dArr4[i];
                double x1 = dArr5[0];
                double x2 = dArr4[i + 1][0];
                double y1 = dArr5[1];
                double y2 = dArr4[i + 1][1];
                return sum + Math.hypot(py - (((1.0d - x) * y1) + (y2 * x)), px - (((1.0d - x) * x1) + (x2 * x)));
            }
        }
        return 0.0d;
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
                    v[j4] = ((1.0d - x) * y1) + (y2 * x);
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
                    v[j4] = (float) (((1.0d - x) * y1) + (y2 * x));
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
                return ((1.0d - x) * y1) + (y2 * x);
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
                double d3 = (t2 - d2) / h;
                for (int j = 0; j < dim; j++) {
                    double[][] dArr3 = this.mY;
                    double y1 = dArr3[i][j];
                    double y2 = dArr3[i + 1][j];
                    v[j] = (y2 - y1) / h;
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
                double d3 = (t2 - d2) / h;
                double[][] dArr3 = this.mY;
                double y1 = dArr3[i][j];
                double y2 = dArr3[i + 1][j];
                return (y2 - y1) / h;
            }
        }
        return 0.0d;
    }

    @Override // androidx.constraintlayout.motion.utils.CurveFit
    public double[] getTimePoints() {
        return this.mT;
    }
}
