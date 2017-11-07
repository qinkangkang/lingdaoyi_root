package com.lingdaoyi.cloud.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "sys_appversion")
public class SysAppversion {
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * app类型 2 ios 3 安卓
     */
    @Column(name = "appType")
    private Integer apptype;

    /**
     * 发版时间
     */
    @Column(name = "releaseDate")
    private Date releasedate;

    /**
     * 版本号
     */
    @Column(name = "versionNum")
    private String versionnum;

    /**
     * 版本值
     */
    @Column(name = "versionValue")
    private Long versionvalue;

    /**
     * 是否强制更新 0 不强制更新  1强制更新
     */
    @Column(name = "forceUpgradeValue")
    private Long forceupgradevalue;

    /**
     * 描述
     */
    private String description;

    /**
     * 创建时间
     */
    @Column(name = "createTime")
    private Date createtime;

    /**
     * 获取id
     *
     * @return id - id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置id
     *
     * @param id id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取app类型 2 ios 3 安卓
     *
     * @return appType - app类型 2 ios 3 安卓
     */
    public Integer getApptype() {
        return apptype;
    }

    /**
     * 设置app类型 2 ios 3 安卓
     *
     * @param apptype app类型 2 ios 3 安卓
     */
    public void setApptype(Integer apptype) {
        this.apptype = apptype;
    }

    /**
     * 获取发版时间
     *
     * @return releaseDate - 发版时间
     */
    public Date getReleasedate() {
        return releasedate;
    }

    /**
     * 设置发版时间
     *
     * @param releasedate 发版时间
     */
    public void setReleasedate(Date releasedate) {
        this.releasedate = releasedate;
    }

    /**
     * 获取版本号
     *
     * @return versionNum - 版本号
     */
    public String getVersionnum() {
        return versionnum;
    }

    /**
     * 设置版本号
     *
     * @param versionnum 版本号
     */
    public void setVersionnum(String versionnum) {
        this.versionnum = versionnum;
    }

    /**
     * 获取版本值
     *
     * @return versionValue - 版本值
     */
    public Long getVersionvalue() {
        return versionvalue;
    }

    /**
     * 设置版本值
     *
     * @param versionvalue 版本值
     */
    public void setVersionvalue(Long versionvalue) {
        this.versionvalue = versionvalue;
    }

    /**
     * 获取是否强制更新 0 不强制更新  1强制更新
     *
     * @return forceUpgradeValue - 是否强制更新 0 不强制更新  1强制更新
     */
    public Long getForceupgradevalue() {
        return forceupgradevalue;
    }

    /**
     * 设置是否强制更新 0 不强制更新  1强制更新
     *
     * @param forceupgradevalue 是否强制更新 0 不强制更新  1强制更新
     */
    public void setForceupgradevalue(Long forceupgradevalue) {
        this.forceupgradevalue = forceupgradevalue;
    }

    /**
     * 获取描述
     *
     * @return description - 描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置描述
     *
     * @param description 描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取创建时间
     *
     * @return createTime - 创建时间
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * 设置创建时间
     *
     * @param createtime 创建时间
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}