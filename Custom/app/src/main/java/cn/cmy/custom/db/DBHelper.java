package cn.cmy.custom.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

import cn.cmy.custom.model.UserModel;

public class DBHelper extends OrmLiteSqliteOpenHelper {

    private static final String DB_NAME = "test.db";
    private static final int DEFAULT_VERSION = 1;
    private static DBHelper mDBHelper;


    public static DBHelper getInstance(Context context) {
        return getInstance(context, DB_NAME);
    }

    public static DBHelper getInstance(Context context, String databaseName) {
        if (null == mDBHelper) {
            synchronized (DBHelper.class) {
                if (null == mDBHelper) {
                    mDBHelper = new DBHelper(context, databaseName);
                }
            }
        }
        return mDBHelper;

    }

    private DBHelper(Context context) {
        this(context, DB_NAME);
    }

    private DBHelper(Context context, String databaseName) {
        this(context, databaseName, null, DEFAULT_VERSION);
    }


    private DBHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion) {
        super(context, databaseName, factory, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {

        try {
            TableUtils.createTableIfNotExists(connectionSource, UserModel.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {

    }

}
