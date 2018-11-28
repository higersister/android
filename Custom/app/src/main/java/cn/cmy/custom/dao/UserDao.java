package cn.cmy.custom.dao;

import android.content.Context;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.cmy.custom.db.DBHelper;
import cn.cmy.custom.model.UserModel;

public class UserDao implements Dao<UserModel> {

    private com.j256.ormlite.dao.Dao<UserModel, Integer> dao;
    private DBHelper dbHelper;

    public UserDao(Context context) {
        dbHelper = DBHelper.getInstance(context);
        try {
            dao = dbHelper.getDao(UserModel.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void insert(UserModel userModel) {
        try {
            dao.create(userModel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(UserModel userModel) {
        try {
            dao.delete(userModel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(UserModel userModel) {
        try {
            dao.update(userModel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @Override
    public List<UserModel> query() {

        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

}
