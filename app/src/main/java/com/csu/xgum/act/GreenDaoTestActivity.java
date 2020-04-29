package com.csu.xgum.act;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import com.csu.xgum.R;
import com.csu.xgum.base.BaseActivity;
import com.csu.xgum.entity.Student;
import com.csu.xgum.greendaod.StudentDao;
import com.csu.xgum.greendaof.DBManager;
import com.csu.xgum.greendaof.server.StuDaoSer;
import com.example.mypublib.utils.MyLog;
import com.example.mypublib.widge.PublicObserver;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.greendao.database.Database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.OnClick;
import dolphin.tools.util.ToastUtil;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class GreenDaoTestActivity extends BaseActivity {

    private StudentDao studentDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_green_dao_test);
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_green_dao_test;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        studentDao = DBManager.getDBManagerInstance(this).getStudentDao();
    }

    @OnClick(R.id.bt_ins)
    public void insert(View view) {
        Date date = new Date();
        SimpleDateFormat simUtil = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date parse = null;
        try {
            parse = simUtil.parse("2020-04-01 10:10:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Student student1 = new Student("苏程", Long.valueOf(1), Long.valueOf(001), "173", "26", parse);
        Student student2 = new Student("苏慧", Long.valueOf(2), Long.valueOf(002), "163", "23", date);
        Student student3 = new Student("苏翠", Long.valueOf(3), Long.valueOf(003), "166", "29", date);
        Student student4 = new Student("孙倩", Long.valueOf(4), Long.valueOf(004), "164", "28", date);
        List<Student> students = new ArrayList<>();
        students.add(student1);
        students.add(student2);
        students.add(student3);
        students.add(student4);
        new StuDaoSer(this).insertAll(students);
    }

    @OnClick(R.id.bt_update)
    public void uptatedata(View view) {
        StuDaoSer stuDaoSer = new StuDaoSer(this);
        Student stu = stuDaoSer.getStuByName("苏翠");
        stu.setDate(new Date());
        stuDaoSer.update(stu);
    }


    @OnClick(R.id.bt_see)
    public void seedata(View view) {
        List<Student> list = studentDao.queryBuilder().list();
        MyLog.i("data数据1", list.toString());
//        test2ForSql();
        test4Time();
        test6Time();
        test5();
    }

    private void test2ForSql() {
        Database database = studentDao.getSession().getDatabase();
        String sql = "select * from " + studentDao.getTablename();
        Cursor cursor = database.rawQuery(sql, null);
//        database.execSQL();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String columnName = cursor.getColumnName(0);
                int columnIndex = cursor.getColumnIndex(columnName);
                String string = cursor.getString(columnIndex);
                MyLog.i("data数据2", columnName + "----" + string);

            }
        }
    }

    private void test4Time() {
        //按住时间范围取出数据
        List<Student> students4 = studentDao.queryRaw("where datetimen between '2020-04-01 10:20:20' and '2020-04-01 15:48:20' order by stuId asc");
        MyLog.i("data数据4", students4.toString());

    }

    private void test6Time() {
        //按住时间范围取出数据
        List<Student> students6 = studentDao.queryRaw("where datetimen >= '2020-04-01 10:20:20' and datetimen <='2020-04-01 15:48:20' order by stuId asc");
        MyLog.i("data数据6", students6.toString());
    }

    private void test5() {
        Observable.just(true)
                .subscribeOn(AndroidSchedulers.mainThread())
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .concatMap(a -> new StuDaoSer(this).getDateIf(List.class))
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(result -> {
                    MyLog.i("data数据5", result.toString());
                })
                .doOnError(erro -> {
                    ToastUtil.shortShow(GreenDaoTestActivity.this, erro.getMessage());
                })

                .subscribe(new PublicObserver());
    }
}
