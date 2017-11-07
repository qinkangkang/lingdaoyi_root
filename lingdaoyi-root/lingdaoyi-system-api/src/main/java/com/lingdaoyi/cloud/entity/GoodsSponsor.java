package com.lingdaoyi.cloud.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "goods_sponsor")
public class GoodsSponsor {
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户ID
     */
    @Column(name = "account_id")
    private Long accountId;

    /**
     * 达人简介
     */
    private String brief;

    /**
     * 图片id
     */
    private String image;

    /**
     * 图片
     */
    private String images;

    /**
     * 客服电话
     */
    private String phone;

    /**
     * 商户状态 0 未启用 1 启用
     */
    private Integer status;
    
    /**
     * 地区
     */
    private String reign;
    
    /**
     * 工作时间
     */
    @Column(name = "workTime")
    private String worktime;
    
    /**
     * 商户类型：1不可线下提现 2 可线下提现 
     */
    @Column(name = "sponsorType")
    private Integer sponsortype;
    
    /**
     * 地区id
     */
    @Column(name = "country_id")
    private Long countryId;

    /**
     * 商户名称
     */
    private String name;

    /**
     * 商户全称
     */
    @Column(name = "fullName")
    private String fullname;

    /**
     * 商户编号
     */
    private String number;

    /**
     * 商户详情URL
     */
    @Column(name = "detailHtmlUrl")
    private String detailhtmlurl;

    /**
     * 网址
     */
    @Column(name = "webSite")
    private String website;

    /**
     * 地址
     */
    private String address;

    /**
     * GPS
     */
    private String gps;

    /**
     * BDID
     */
    @Column(name = "bdID")
    private Long bdid;

    /**
     * 创建人ID
     */
    @Column(name = "createrId")
    private Long createrid;

    /**
     * 人均价格
     */
    @Column(name = "perPrice")
    private String perprice;

    /**
     * 温馨提示
     */
    private String reminder;

    /**
     * 创建时间
     */
    @Column(name = "createTime")
    private Date createtime;

    /**
     * 修改时间
     */
    @Column(name = "updateTime")
    private Date updatetime;

    /**
     * 商户详情
     */
    private String detail;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getDetailhtmlurl() {
		return detailhtmlurl;
	}

	public void setDetailhtmlurl(String detailhtmlurl) {
		this.detailhtmlurl = detailhtmlurl;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getGps() {
		return gps;
	}

	public void setGps(String gps) {
		this.gps = gps;
	}

	public Long getBdid() {
		return bdid;
	}

	public void setBdid(Long bdid) {
		this.bdid = bdid;
	}

	public Long getCreaterid() {
		return createrid;
	}

	public void setCreaterid(Long createrid) {
		this.createrid = createrid;
	}

	public String getPerprice() {
		return perprice;
	}

	public void setPerprice(String perprice) {
		this.perprice = perprice;
	}

	public String getReminder() {
		return reminder;
	}

	public void setReminder(String reminder) {
		this.reminder = reminder;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public Integer getSponsortype() {
		return sponsortype;
	}

	public void setSponsortype(Integer sponsortype) {
		this.sponsortype = sponsortype;
	}

	public String getReign() {
		return reign;
	}

	public void setReign(String reign) {
		this.reign = reign;
	}

	public String getWorktime() {
		return worktime;
	}

	public void setWorktime(String worktime) {
		this.worktime = worktime;
	}

}