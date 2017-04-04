package com.lttd.freezer.bean;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by  on 2017/3/15.
 */


@Table(name = "Food")
public class Food {
    @Id
    public int id;

    private String food_name;

    private int foodType_id = -1;

    private String foodType_name;

    private String food_prod_time;//放入冰箱的日期 （2016-07-01）

    private int food_quality_period = 7;//保质期（天）

    private int status = 0;//当前的状态  0 正常； 1 24小时内过期； 2 已过期； 3 被消费掉了；

    private int ic_drawid = -1;

    public int getIc_drawid() {
        return ic_drawid;
    }

    public void setIc_drawid(int ic_drawid) {
        this.ic_drawid = ic_drawid;
    }

    public String getFoodType_name() {
        return foodType_name;
    }

    public void setFoodType_name(String foodType_name) {
        this.foodType_name = foodType_name;
    }

    public int getFoodType_id() {
        return foodType_id;
    }

    public void setFoodType_id(int foodType_id) {
        this.foodType_id = foodType_id;
    }

    public int getId() {
        return id;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public String getFood_prod_time() {
        return food_prod_time;
    }

    public void setFood_prod_time(String food_prod_time) {
        this.food_prod_time = food_prod_time;
    }

    public int getFood_quality_period() {
        return food_quality_period;
    }

    public void setFood_quality_period(int food_quality_period) {
        this.food_quality_period = food_quality_period;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
