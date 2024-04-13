package com.example.utils;

import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;

public class MathUtil {

    //使用三次多项式拟合
    public static double getPrediction(WeightedObservedPoints points, long tomorrowTimestamp) {
        PolynomialCurveFitter fitter = PolynomialCurveFitter.create(3);
        double[] coeffs = fitter.fit(points.toList());
        return coeffs[0] + coeffs[1] * (tomorrowTimestamp) + coeffs[2] * Math.pow(tomorrowTimestamp, 2)
                + coeffs[3] * Math.pow(tomorrowTimestamp, 3);
    }

}
