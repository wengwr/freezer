package com.lttd.freezer.bean;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Created by  on 2017/3/15.
 */


@Table(name = "FoodType")
public class FoodType {
    @Id
    public int id;

    private String type_name;
    private String type_code;

    public int getId() {
        return id;
    }

    public String getType_code() {
        return type_code;
    }

    public void setType_code(String type_code) {
        this.type_code = type_code;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }
}
