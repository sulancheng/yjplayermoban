package com.csu.xgum.greendaof;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.csu.xgum.greendaod.DaoMaster;
import com.csu.xgum.greendaod.StudentDao;
import com.csu.xgum.greendaof.upgraputils.MigrationHelper;

import org.greenrobot.greendao.database.Database;

/**
 * Created by su
 * on 2017/5/19.df
 * 如果只是改变build中的配置则会删除表中的所有历史数据
 */
public class MySQLiteOpenHelper extends DaoMaster.DevOpenHelper {

    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {

            @Override
            public void onCreateAllTables(Database db, boolean ifNotExists) {
                DaoMaster.createAllTables(db, ifNotExists);
            }

            @Override
            public void onDropAllTables(Database db, boolean ifExists) {
                DaoMaster.dropAllTables(db, ifExists);
            }
        }, StudentDao.class);
        /*升级的步骤：1.在buidl 修改版本    2.在实体类加变量字段   make项目  3。像这里这样写，只有notebookdao在升级时候不会丢失数据，写谁谁数据保存着*/
    }
}