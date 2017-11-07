package com.lingdaoyi.cloud.entity;

import javax.persistence.*;

@Table(name = "sys_channel_setting")
public class SysChannelSetting {
    /**
     * 主键id 0 为全部国家通用
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 名称
     */
    private String title;

    /**
     * 编码
     */
    private String code;

    /**
     * 排序（按从小到大）
     */
    private Integer sort;

    /**
     * 是否显示（0不显示 1显示）
     */
    @Column(name = "isVisible")
    private Integer isvisible;

    /**
     * 地区id
     */
    @Column(name = "country_id")
    private Long countryId;

    /**
     * 地区名称
     */
    @Column(name = "countryName")
    private String countryname;
    
    /**
     * 栏目设置类型
     */
    @Column(name = "seetingType")
    private Integer seetingtype;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getIsvisible() {
        return isvisible;
    }

    public void setIsvisible(Integer isvisible) {
        this.isvisible = isvisible;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public String getCountryname() {
        return countryname;
    }

    public void setCountryname(String countryname) {
        this.countryname = countryname;
    }

	public Integer getSeetingtype() {
		return seetingtype;
	}

	public void setSeetingtype(Integer seetingtype) {
		this.seetingtype = seetingtype;
	}
}