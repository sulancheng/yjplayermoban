package com.csu.xgum.greendaof;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.csu.xgum.greendaod.DaoMaster;
import com.csu.xgum.greendaod.DaoSession;
import com.csu.xgum.greendaod.NoteBookDao;
import com.csu.xgum.greendaod.StudentDao;

import org.greenrobot.greendao.query.QueryBuilder;


/**
 * Created by Administrator
 * on 2017/1/12.
 */
public class DBManager {
    private final static String dbName = "su_student";
    private static DBManager mInstance;
    private DaoMaster.DevOpenHelper openHelper;
    private Context context;
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    private DBManager(Context context) {
        this.context = context;
        //openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        //MigrationHelper.DEBUG = true; //如果你想查看日志信息，请将DEBUG设置为true
        openHelper = new MySQLiteOpenHelper(context, dbName, null);
        daoSession = getDaoSession();//获取daoMaster  daoSession
        //展示日志
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
    }

    public static DBManager getDBManagerInstance(Context context) {
        if (mInstance == null) {
            synchronized (DBManager.class) {
                mInstance = new DBManager(context);
            }
        }
        return mInstance;
    }

    /**
     * 获取可读数据库
     */
    private SQLiteDatabase getReadableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }

    /**
     * 获取可写数据库
     */
    private SQLiteDatabase db;

    private SQLiteDatabase getWritableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        if (db == null) {
            db = openHelper.getWritableDatabase();
        }
        return db;
    }

    private DaoSession getDaoSession() {
        if (daoMaster == null) {
            daoMaster = new DaoMaster(getWritableDatabase());
        }
        if (daoSession == null) {
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

    public DaoSession returnDaoSession() {
        return daoSession;
    }

    //增删查改
    public NoteBookDao getNoteBookDao() {
        NoteBookDao noteBookDao = getDaoSession().getNoteBookDao();
        return noteBookDao;
    }

    public StudentDao getStudentDao() {
        StudentDao studentDao = getDaoSession().getStudentDao();
        return studentDao;
    }

}

