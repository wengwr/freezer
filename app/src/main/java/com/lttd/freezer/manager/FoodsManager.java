package com.lttd.freezer.manager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lttd.freezer.FreApplication;
import com.lttd.freezer.FrePreference;
import com.lttd.freezer.bean.Food;
import com.lttd.freezer.bean.FoodType;
import com.lttd.freezer.bean.TypeTemp;
import com.lttd.freezer.config.FreConfig;
import com.lttd.freezer.utils.DateUtils;

import java.util.List;


/**
 * Created by  on 2017/3/15.
 */

public class FoodsManager {
    //0 正常； 1 24小时内过期； 2 已过期； 3 被消费掉了 4 扔到了垃圾桶
    public static final int FOOD_STATU_NORMAL = 0;
    public static final int FOOD_STATU_IN24 = 1;
    public static final int FOOD_STATU_EXPIRE = 2;
    public static final int FOOD_STATU_CONSUME = 3;
    public static final int FOOD_STATU_DISCARD = 4;

    private static FoodsManager INSTANCE;
    private Context mContext;

    private DbUtils db;

    private double curTemp;
    private double curHumidity;
    private FoodsRefreshListner foodsRefreshListner;
    private final Handler handler;
    private final FreNotificationManager notificationManager;

    public void deleteFoodTypeTemp(TypeTemp typeTemp) {
        try {
            db.delete(typeTemp);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void cleanAll() {
        try {
            db.dropDb();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public interface FoodsRefreshListner {
        void refresh();
    }

    public void registRecive(FoodsRefreshListner foodsRefreshListner) {
        this.foodsRefreshListner = foodsRefreshListner;
    }


    public static FoodsManager getINSTANCE(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new FoodsManager(context);
        }
        return INSTANCE;
    }

    public void setCurTemp(String msg) {
        String[] split = msg.split(",");
        if (split.length != 4) {
            return;
        }
        try {
            curTemp = Double.parseDouble(split[2]);
            curHumidity = Double.parseDouble(split[2]);
            handler.sendEmptyMessage(0);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public boolean saveNewFood(Food food) {
        try {
            db.save(food);
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Food findFood(String s) {
        Food food = null;
        try {
            food = db.findFirst(Selector.from(Food.class).where("food_name", "=", s));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return food;

    }

    public FoodType getFoodTypeByID(int id) {
        FoodType mFoodType = null;
        try {
            mFoodType = db.findById(FoodType.class, id);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return mFoodType;

    }

    public List<Food> getFoodInDustbin() {
        List<Food> foods = null;
        try {
            foods = db.findAll(Selector.from(Food.class).where("status", "=", FOOD_STATU_DISCARD));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return foods;
    }

    public List<Food> getFoodInFridge() {
        List<Food> foods = null;
        try {
            foods = db.findAll(Selector.from(Food.class).where("status", "=", FOOD_STATU_NORMAL)
                    .or("status", "=", FOOD_STATU_IN24).or("status", "=", FOOD_STATU_EXPIRE));
        } catch (DbException e) {
            e.printStackTrace();
        }


        return foods;
    }

    public void updateFoodState(Food food) {
        try {
            db.saveOrUpdate(food);
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    public boolean saveNewFoodType(FoodType foodType, List<TypeTemp> typeTempList) {

        try {
            long count = db.count(FoodType.class);
            count = count + 1;
            foodType.setType_code(count + "");
            db.save(foodType);

            for (TypeTemp mTypeTemp : typeTempList) {
                mTypeTemp.setFoodtype_code(foodType.getType_code());
                db.save(mTypeTemp);
            }

        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }
        initFoodType();
        return true;
    }

    public boolean updateFoodType(FoodType foodType, List<TypeTemp> typeTempList) {

        try {
            db.saveOrUpdate(foodType);

            for (TypeTemp mTypeTemp : typeTempList) {
                mTypeTemp.setFoodtype_code(foodType.getType_code());
                db.saveOrUpdate(mTypeTemp);
            }

        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List<TypeTemp> findFoodTypeTemp(String type_code) {
        List<TypeTemp> typeTemps = null;
        try {
            typeTemps = db.findAll(Selector.from(TypeTemp.class).where("foodtype_code", "=", type_code));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return typeTemps;
    }

    public void deleteFoodType(FoodType foodType) {
        try {
            db.delete(foodType);
        } catch (DbException e) {
            e.printStackTrace();
        }
        initFoodType();
    }


    private FoodsManager(Context context) {
        mContext = context;
        notificationManager = FreApplication.getINSTANCE().getNotificationManager();

        DbUtils.DaoConfig config = new DbUtils.DaoConfig(context);

        config.setDbName("freezer_db"); // db名
        // 数据库存储的路径
        if(!TextUtils.isEmpty(FreConfig.DB_PATH)){
            config.setDbDir(FreConfig.DB_PATH);
        }
        config.setDbVersion(1); // db版本

        db = DbUtils.create(config);


        final HandlerThread handlerThread = new HandlerThread("handlerThread");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        refreshData();
                        sendEmptyMessageDelayed(0, FreConfig.REFRESH_TIME);
                        if (foodsRefreshListner != null) {
                            foodsRefreshListner.refresh();
                        }
                        break;
                }
            }
        };
        handler.sendEmptyMessage(0);
    }

    public void refreshData() {
        List<Food> data = getAllFood();
        if (data == null) {
            return;
        }

        for (Food food : data) {
            if (food.getStatus() == FOOD_STATU_EXPIRE || food.getStatus() == FOOD_STATU_CONSUME ||
                    food.getStatus() == FOOD_STATU_DISCARD) {
                continue;//如果食物已经消费了 或者过期了 就不再去计算保质期了
            }

            FoodType foodType = getFoodTypeByID(food.getFoodType_id());
            if (foodType != null) {
                List<TypeTemp> foodTypeTemp = findFoodTypeTemp(foodType.getType_code());
                for (TypeTemp typeTemp : foodTypeTemp) {
                    if (curTemp >= typeTemp.getStart_tem() && curTemp < typeTemp.getEnd_tem()) {
                        food.setFood_quality_period(typeTemp.getFood_quality_period());
                    }
                }
            }

            int betweenDaysFromNow = DateUtils.getBetweenDaysFromNow(food.getFood_prod_time());
            int period = food.getFood_quality_period() - betweenDaysFromNow;

            if (period == 0) {
                food.setStatus(FOOD_STATU_IN24);
            }
            if (period > 0) {
                food.setStatus(FOOD_STATU_NORMAL);

            }
            if (period < 0) {
                food.setStatus(FOOD_STATU_EXPIRE);
            }
            updateFoodState(food);

        }
    }


    private List<FoodType> foodTypes = null;

    public List<FoodType> getFoodTypes() {
        if (foodTypes == null) {
            initFoodType();
        }

        return foodTypes;
    }

    public void initFoodType() {
        try {
            foodTypes = db.findAll(FoodType.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    private List<Food> getAllFood() {
        List<Food> foods = null;
        try {
            foods = db.findAll(Selector.from(Food.class).where("status", "=", FOOD_STATU_NORMAL)
                    .or("status", "=", FOOD_STATU_IN24).or("status", "=", FOOD_STATU_EXPIRE));
        } catch (DbException e) {
            e.printStackTrace();
        }

        if (foods == null) {
            return null;
        }

        if (foods.size() == 0) {
            return null;
        }

        return foods;
    }

}
