package com.lingdaoyi.cloud.entity;

import javax.persistence.*;

@Table(name = "sys_dictionary")
public class SysDictionary {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 字典分类ID : 分类值
     */
    @Column(name = "class_id")
    private Long classId;

    /**
     * 代码名称
     */
    private String name;

    /**
     * 代码国际化编码
     */
    private String code;

    /**
     * 编码值
     */
    private Integer value;

    /**
     * 状态
     */
    private Integer status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
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

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}