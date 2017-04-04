package com.lttd.freezer.bean;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by  on 2017/3/15.
 */


@Table(name = "TypeTemp")
public class TypeTemp {
    @Id
    public int id;

    public String foodtype_code;

    public double start_tem;
    public double end_tem;

    private int food_quality_period = 7;//保质期（天）


    public String getFoodtype_code() {
        return foodtype_code;
    }

    public void setFoodtype_code(String foodtype_code) {
        this.foodtype_code = foodtype_code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getStart_tem() {
        return start_tem;
    }

    public void setStart_tem(double start_tem) {
        this.start_tem = start_tem;
    }

    public double getEnd_tem() {
        return end_tem;
    }

    public void setEnd_tem(double end_tem) {
        this.end_tem = end_tem;
    }

    public int getFood_quality_period() {
        return food_quality_period;
    }

    public void setFood_quality_period(int food_quality_period) {
        this.food_quality_period = food_quality_period;
    }
}
