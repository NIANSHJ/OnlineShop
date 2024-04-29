package com.example.service.impl;

import com.example.mapper.SellMapper;
import com.example.pojo.Prediction;
import com.example.pojo.CategorySell;
import com.example.service.SellService;
import com.example.utils.MathUtil;
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
    public List<CategorySell> statistic() {
        return sellMapper.statistic();
    }

    //获取热销商品服务
    @Override
    public List<Prediction> prediction() {
        List<Prediction> predictions = sellMapper.prediction();
        if (predictions.isEmpty()) {
            return predictions;
        }
        WeightedObservedPoints points = new WeightedObservedPoints();
        for (Prediction prediction : predictions) {
            LocalDate date = LocalDate.parse(prediction.getDate());
            long timeStamp = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
            points.add(timeStamp, prediction.getSales());
        }
        LocalDate tomorrowDate = LocalDate.now().plusDays(1);
        String formattedDate = tomorrowDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        long tomorrowTimestamp = tomorrowDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        double predictedSales = MathUtil.getPrediction(points, tomorrowTimestamp);
        Prediction prediction = new Prediction();
        prediction.setDate(formattedDate);
        prediction.setSales(predictedSales);
        predictions.add(prediction);
        return predictions;
    }

}
