package com.lingdaoyi.cloud.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "goods_spu")
public class GoodsSpu {
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 商品标题
     */
    private String name;

    /**
     * 商品副标题
     */
    @Column(name = "subName")
    private String subname;

    /**
     * 副标题图片
     */
    @Column(name = "subIcon")
    private Long subicon;

    /**
     * 所属城市
     */
    @Column(name = "country_id")
    private Long countryId;

    /**
     * 所属商户
     */
    @Column(name = "sponsor_id")
    private Long sponsorId;

    /**
     * 商品一级分类
     */
    @Column(name = "categoryA_id")
    private Long categoryaId;

    /**
     * 商品二级分类
     */
    @Column(name = "categoryB_id")
    private Long categorybId;

    /**
     * 活动简介
     */
    private String brief;

    /**
     * 活动详情URL
     */
    @Column(name = "detailHtmlUrl")
    private String detailhtmlurl;
    
    /**
     * 活动详情URL
     */
    @Column(name = "purchaseDesc")
    private String purchasedesc;

    /**
     * 主图片
     */
    private String image1;

    /**
     * 细节图
     */
    private String image2;

    /**
     * 活动状态
     */
    private Integer status;

    /**
     * 创建人id
     */
    @Column(name = "userId")
    private Long userid;

    /**
     * 上架时间
     */
    @Column(name = "onSaleTime")
    private Date onsaletime;

    /**
     * 下架时间/秒杀截至时间
     */
    @Column(name = "offSaleTime")
    private Date offsaletime;

    /**
     * 修改时间
     */
    @Column(name = "updateTime")
    private Date updatetime;

    /**
     * 创建时间
     */
    @Column(name = "createTime")
    private Date createtime;

    /**
     * 商品规格
     */
    private String spec;

    /**
     * 限购（-1表示无限购）
     */
    private Integer limitation;

    /**
     * 商品总库存量
     */
    private Integer total;

    /**
     * 商品剩余库存量
     */
    private Integer stock;

    /**
     * 售卖总量
     */
    @Column(name = "saleTotal")
    private Integer saletotal;

    /**
     * 商品售卖价格
     */
    @Column(name = "presentPrice")
    private BigDecimal presentprice;

    /**
     * 商品原价
     */
    @Column(name = "originalPrice")
    private BigDecimal originalprice;

    /**
     * 活动详情
     */
    private String detail;

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

	public String getSubname() {
		return subname;
	}

	public void setSubname(String subname) {
		this.subname = subname;
	}

	public Long getSubicon() {
		return subicon;
	}

	public void setSubicon(Long subicon) {
		this.subicon = subicon;
	}

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public Long getSponsorId() {
		return sponsorId;
	}

	public void setSponsorId(Long sponsorId) {
		this.sponsorId = sponsorId;
	}

	public Long getCategoryaId() {
		return categoryaId;
	}

	public void setCategoryaId(Long categoryaId) {
		this.categoryaId = categoryaId;
	}

	public Long getCategorybId() {
		return categorybId;
	}

	public void setCategorybId(Long categorybId) {
		this.categorybId = categorybId;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public String getDetailhtmlurl() {
		return detailhtmlurl;
	}

	public void setDetailhtmlurl(String detailhtmlurl) {
		this.detailhtmlurl = detailhtmlurl;
	}

	public String getPurchasedesc() {
		return purchasedesc;
	}

	public void setPurchasedesc(String purchasedesc) {
		this.purchasedesc = purchasedesc;
	}

	public String getImage1() {
		return image1;
	}

	public void setImage1(String image1) {
		this.image1 = image1;
	}

	public String getImage2() {
		return image2;
	}

	public void setImage2(String image2) {
		this.image2 = image2;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public Date getOnsaletime() {
		return onsaletime;
	}

	public void setOnsaletime(Date onsaletime) {
		this.onsaletime = onsaletime;
	}

	public Date getOffsaletime() {
		return offsaletime;
	}

	public void setOffsaletime(Date offsaletime) {
		this.offsaletime = offsaletime;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public Integer getLimitation() {
		return limitation;
	}

	public void setLimitation(Integer limitation) {
		this.limitation = limitation;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Integer getSaletotal() {
		return saletotal;
	}

	public void setSaletotal(Integer saletotal) {
		this.saletotal = saletotal;
	}

	public BigDecimal getPresentprice() {
		return presentprice;
	}

	public void setPresentprice(BigDecimal presentprice) {
		this.presentprice = presentprice;
	}

	public BigDecimal getOriginalprice() {
		return originalprice;
	}

	public void setOriginalprice(BigDecimal originalprice) {
		this.originalprice = originalprice;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

}