package com.lingdaoyi.cloud.entity;

import javax.persistence.*;

@Table(name = "sys_dictionary_class")
public class SysDictionaryClass {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类国际化编码
     */
    private String code;

    /**
     * 可编辑标志 : 1、可编辑
0、不可编辑
     */
    private Integer editable;

    /**
     * 获取ID
     *
     * @return id - ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置ID
     *
     * @param id ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取分类名称
     *
     * @return name - 分类名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置分类名称
     *
     * @param name 分类名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取分类国际化编码
     *
     * @return code - 分类国际化编码
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置分类国际化编码
     *
     * @param code 分类国际化编码
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取可编辑标志 : 1、可编辑
0、不可编辑
     *
     * @return editable - 可编辑标志 : 1、可编辑
0、不可编辑
     */
    public Integer getEditable() {
        return editable;
    }

    /**
     * 设置可编辑标志 : 1、可编辑
0、不可编辑
     *
     * @param editable 可编辑标志 : 1、可编辑
0、不可编辑
     */
    public void setEditable(Integer editable) {
        this.editable = editable;
    }
}