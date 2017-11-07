package com.lingdaoyi.cloud.entity;

import javax.persistence.*;

@Table(name = "sys_configuration")
public class SysConfiguration {
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 地区id
     */
    @Column(name = "country_id")
    private Long countryId;

    /**
     * 字典名称
     */
    private String name;

    /**
     * key
     */
    private String key;

    /**
     * key值
     */
    private String value;

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
     * 获取地区id
     *
     * @return country_id - 地区id
     */
    public Long getCountryId() {
        return countryId;
    }

    /**
     * 设置地区id
     *
     * @param countryId 地区id
     */
    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    /**
     * 获取字典名称
     *
     * @return name - 字典名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置字典名称
     *
     * @param name 字典名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取key
     *
     * @return key - key
     */
    public String getKey() {
        return key;
    }

    /**
     * 设置key
     *
     * @param key key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * 获取key值
     *
     * @return value - key值
     */
    public String getValue() {
        return value;
    }

    /**
     * 设置key值
     *
     * @param value key值
     */
    public void setValue(String value) {
        this.value = value;
    }
}