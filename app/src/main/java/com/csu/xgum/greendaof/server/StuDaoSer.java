package com.csu.xgum.greendaof.server;

import android.content.Context;

import com.google.gson.Gson;
import com.csu.xgum.bean.MyTrowable;
import com.csu.xgum.entity.Student;
import com.csu.xgum.greendaod.StudentDao;
import com.csu.xgum.greendaof.DBManager;

import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;

/**
 * 作者：sucheng on 2020/4/1 0001 10:58
 */
public class StuDaoSer {

    private StudentDao studentDao;
    private Gson gson = new Gson();


    public StuDaoSer(Context context) {
        studentDao = DBManager.getDBManagerInstance(context).getStudentDao();
    }

    public void insert(Student student) {
        studentDao.insertOrReplace(student);
    }


    public void insertAll(List<Student> students) {
        studentDao.insertOrReplaceInTx(students);
    }


    public void delbyId(Long id) {
        studentDao.deleteByKey(id);
    }
    public void delBystu(Student student) {
        studentDao.delete(student);
    }
    public void delAll() {
        studentDao.deleteAll();
    }
    public void update(Student student) {
        studentDao.update(student);
    }
    public Student getStuByName(String name) {
        return studentDao.queryBuilder()
                .where(StudentDao.Properties.Name.eq(name))
                .orderAsc(StudentDao.Properties.Tall)
                .list().get(0);
    }
    public <T> Observable<T> getDateIf(Class<T> tClass) {
        List<Student> student5 = studentDao.queryBuilder()
                .where(StudentDao.Properties.Tall.between("156", "165"))
                .orderAsc(StudentDao.Properties.Tall)
                .list();
        //   .and(NoteBookDao.Properties.UserID.between())
        return allchuli(student5, tClass);
    }

    public <T> Observable<T> getDataForTall(Class<T> tClass) {
        QueryBuilder<Student> studentQueryBuilder = studentDao.queryBuilder().orderDesc(StudentDao.Properties.Tall);
        Query<Student> build =
                studentQueryBuilder.build();
        List<Student> list = build.list();
        return allchuli(list, tClass);
    }


    public <T> Observable<T> allchuli(List<Student> datas, Class<T> clazz) {

        return Observable.create((ObservableEmitter<T> e) -> {

            try {
                if (datas == null) {
                    e.tryOnError(new MyTrowable("学生数据为空", -3));
                } else {
                    //数据发送已经完成
                    e.onNext((T) datas);
                }

            } catch (Exception ex) {
                e.tryOnError(new MyTrowable(ex.getMessage() + "json格式异常", -3));
            }

        });
    }

}
