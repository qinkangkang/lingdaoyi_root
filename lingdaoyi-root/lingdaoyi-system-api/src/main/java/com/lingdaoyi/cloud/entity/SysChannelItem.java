package com.lingdaoyi.cloud.entity;

import javax.persistence.*;

@Table(name = "sys_channel_item")
public class SysChannelItem {
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 栏目设置表id
     */
    @Column(name = "setting_id")
    private Long settingId;

    /**
     * 栏目设置表名称
     */
    @Column(name = "setting_name")
    private String settingName;

    /**
     * 图片id
     */
    @Column(name = "image_id")
    private Long imageId;

    /**
     * 跳转类型 
     */
    @Column(name = "skipType")
    private Integer skiptype;

    /**
     * 跳转实体id
     */
    @Column(name = "entity_id")
    private Long entityId;

    /**
     * 跳转实体name
     */
    @Column(name = "entity_name")
    private String entityName;

    /**
     * h5url
     */
    @Column(name = "webUrl")
    private String weburl;

    /**
     * 排序 从小打到大
     */
    private Integer sort;

    /**
     * 是否显示 0不显示 1显示
     */
    @Column(name = "isVisible")
    private Integer isvisible;

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

    public Long getSettingId() {
        return settingId;
    }

    public void setSettingId(Long settingId) {
        this.settingId = settingId;
    }

    public String getSettingName() {
        return settingName;
    }

    public void setSettingName(String settingName) {
        this.settingName = settingName;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public Integer getSkiptype() {
        return skiptype;
    }

    public void setSkiptype(Integer skiptype) {
        this.skiptype = skiptype;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getWeburl() {
        return weburl;
    }

    public void setWeburl(String weburl) {
        this.weburl = weburl;
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

	public Integer getSeetingtype() {
		return seetingtype;
	}

	public void setSeetingtype(Integer seetingtype) {
		this.seetingtype = seetingtype;
	}

    
}