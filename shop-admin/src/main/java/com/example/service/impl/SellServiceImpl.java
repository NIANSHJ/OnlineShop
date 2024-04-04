package com.example.service.impl;

import com.example.mapper.SellMapper;
import com.example.pojo.Prediction;
import com.example.pojo.Sell;
import com.example.service.SellService;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class SellServiceImpl implements SellService {

    @Autowired
    private SellMapper sellMapper;

    //获取各分类销售额服务
    @Override
    public List<Sell> statistic() {
        return sellMapper.statistic();
    }

    //获取热销商品服务
    @Override
    public List<Prediction> prediction() {
        List<Prediction> predictions = sellMapper.prediction();
        if (predictions.isEmpty()) {
            return null;
        }
        WeightedObservedPoints points = new WeightedObservedPoints();
        for (Prediction prediction : predictions) {
            LocalDate date = LocalDate.parse(prediction.getDate());
            long timeStamp = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
            points.add(timeStamp, prediction.getSales());
        }

        // 使用三次多项式拟合
        PolynomialCurveFitter fitter = PolynomialCurveFitter.create(3);
        double[] coeffs = fitter.fit(points.toList());
        LocalDate tomorrowDate = LocalDate.now().plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = tomorrowDate.format(formatter);
        long tomorrowTimestamp = tomorrowDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        double predictedSales = coeffs[0] + coeffs[1] * (tomorrowTimestamp) +
                coeffs[2] * Math.pow(tomorrowTimestamp, 2) + coeffs[3] * Math.pow(tomorrowTimestamp, 3);
        Prediction prediction = new Prediction();
        prediction.setDate(formattedDate);
        prediction.setSales(predictedSales);
        predictions.add(prediction);
        return predictions;
    }

}
