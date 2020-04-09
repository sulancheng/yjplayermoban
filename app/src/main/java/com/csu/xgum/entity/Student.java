package com.csu.xgum.entity;

import com.csu.xgum.greendaof.converter.StringDateConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * * // 对一，实体属性 joinProperty 对应外联实体ID
 *
 * @ToOne(joinProperty = "fk_dogId")
 * private Dog dog;
 * <p>
 * // 对多。实体ID对应外联实体属性 referencedJoinProperty
 * @ToMany(referencedJoinProperty = "fk_userId")
 * private List<Cat> cats;
 * <p>
 * // 对多。@JoinProperty：name 实体属性对应外联实体属性 referencedName
 * @ToMany(joinProperties = {
 * @JoinProperty(name = "horseName", referencedName = "name")
 * })
 * @Entity( schema = "myschema",
 * <p>
 * active = true,
 * <p>
 * nameInDb = "AWESOME_USERS",
 * <p>
 * indexes = {
 * @Index(value = "name DESC", unique = true)
 * },
 * <p>
 * createInDb = false
 * )
 */
@Entity(nameInDb = "student")
public class Student {
    // 非空
    @NotNull
    String name;
    @Id(autoincrement = true)
    @Property(nameInDb = "stuId")
    Long stuId;
    //设置唯一性
    @Index(unique = true)
    Long stuNo;
    // 非空
    @NotNull
    String tall;
    @NotNull
    String age;
    @Property(nameInDb = "datetimen")
    //用到了这个Convert注解，表明它们的转换类，这样就可以转换成String保存到数据库中了
    @Convert(converter = StringDateConverter.class, columnType = String.class)
    Date date;
    @Generated(hash = 229792034)
    public Student(@NotNull String name, Long stuId, Long stuNo,
            @NotNull String tall, @NotNull String age, Date date) {
        this.name = name;
        this.stuId = stuId;
        this.stuNo = stuNo;
        this.tall = tall;
        this.age = age;
        this.date = date;
    }

    public Student(String name, Long stuNo, String tall, String age, Date date) {
        this.name = name;
        this.stuNo = stuNo;
        this.tall = tall;
        this.age = age;
        this.date = date;
    }

    @Generated(hash = 1556870573)
    public Student() {
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Long getStuId() {
        return this.stuId;
    }
    public void setStuId(Long stuId) {
        this.stuId = stuId;
    }
    public Long getStuNo() {
        return this.stuNo;
    }
    public void setStuNo(Long stuNo) {
        this.stuNo = stuNo;
    }
    public String getTall() {
        return this.tall;
    }
    public void setTall(String tall) {
        this.tall = tall;
    }
    public String getAge() {
        return this.age;
    }
    public void setAge(String age) {
        this.age = age;
    }
    public Date getDate() {
        return this.date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", stuId=" + stuId +
                ", stuNo=" + stuNo +
                ", tall='" + tall + '\'' +
                ", age='" + age + '\'' +
                ", date=" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date) +
                '}';
    }
}
