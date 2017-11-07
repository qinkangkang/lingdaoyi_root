package com.lingdaoyi.cloud.entity;

import javax.persistence.*;

@Table(name = "goods_category")
public class GoodsCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 类型名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 类型代码
     */
    @Column(name = "code")
    private String code;

    /**
     * 类型级别
     */
    @Column(name = "level")
    private Integer level;

    /**
     * 排序
     */
    @Column(name = "order")
    private Integer order;

    /**
     * 上级类型ID
     */
    @Column(name = "parentId")
    private Long parentid;

    /**
     * 图片URL
     */
    @Column(name = "imageA")
    private String imagea;

    /**
     * 图片URL
     */
    @Column(name = "imageB")
    private String imageb;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Long getParentid() {
        return parentid;
    }

    public void setParentid(Long parentid) {
        this.parentid = parentid;
    }

    public String getImagea() {
        return imagea;
    }

    public void setImagea(String imagea) {
        this.imagea = imagea;
    }

    public String getImageb() {
        return imageb;
    }

    public void setImageb(String imageb) {
        this.imageb = imageb;
    }
}