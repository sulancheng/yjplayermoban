package com.csu.xgum.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 这里的意思是 userId作为外键与User的主键（也就是id）相连。
 * on 2017/1/12.
 * // 对一，实体属性 joinProperty 对应外联实体ID
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
 */
@Entity
public class NoteBook {
    @Id(autoincrement = true)//设置自增长
    private Long id;
    @Index(unique = true)//设置唯一性
    private String perNo;
    private String time;
    private String name;
    private Long userID;
    private String zishu;
    @Generated(hash = 2027970357)
    public NoteBook(Long id, String perNo, String time, String name, Long userID,
            String zishu) {
        this.id = id;
        this.perNo = perNo;
        this.time = time;
        this.name = name;
        this.userID = userID;
        this.zishu = zishu;
    }
    @Generated(hash = 2066935268)
    public NoteBook() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getPerNo() {
        return this.perNo;
    }
    public void setPerNo(String perNo) {
        this.perNo = perNo;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Long getUserID() {
        return this.userID;
    }
    public void setUserID(Long userID) {
        this.userID = userID;
    }
    public String getZishu() {
        return this.zishu;
    }
    public void setZishu(String zishu) {
        this.zishu = zishu;
    }

}
