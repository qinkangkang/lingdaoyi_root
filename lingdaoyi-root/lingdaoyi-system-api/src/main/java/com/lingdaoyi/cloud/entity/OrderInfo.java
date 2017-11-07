package com.lingdaoyi.cloud.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "order_info")
public class OrderInfo {
    /**
     * 订单id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 订单编号
     */
    @Column(name = "orderNum")
    private String ordernum;

    /**
     * 客户ID
     */
    @Column(name = "account_id")
    private Long accountId;

    /**
     * 客户名称
     */
    @Column(name = "customerName")
    private String customername;

    /**
     * 客户手机号
     */
    @Column(name = "customerPhone")
    private String customerphone;

    /**
     * 商家ID
     */
    @Column(name = "sponsor_id")
    private Long sponsorId;

    /**
     * 商家名称
     */
    @Column(name = "sponsorName")
    private String sponsorname;

    /**
     * 商家全称
     */
    @Column(name = "sponsorFullName")
    private String sponsorfullname;

    /**
     * 商家联系电话
     */
    @Column(name = "sponsorPhone")
    private String sponsorphone;

    /**
     * 商家编码
     */
    @Column(name = "sponsorNumber")
    private String sponsornumber;

    /**
     * 商品ID
     */
    @Column(name = "goods_id")
    private Long goodsId;

    /**
     * 活动名称
     */
    @Column(name = "goodsName")
    private String goodsname;
    
    /**
     * 商品图片路径
     */
    @Column(name = "goodsUrl")
    private String goodsurl;

    /**
     * 数量
     */
    private Integer count;

    /**
     * 实收金额
     */
    @Column(name = "orderTotal")
    private BigDecimal ordertotal;

    /**
     * 应收金额
     */
    @Column(name = "receivableTotal")
    private BigDecimal receivabletotal;

    /**
     * 邮费
     */
    private BigDecimal postage;

    /**
     * 订单状态
     */
    private Integer status;

    /**
     * 客户备注
     */
    private String remark;

    /**
     * 客户信息
     */
    private String recipient;
    
    /**
     * 支付类型
     */
    @Column(name = "payType")
    private Integer paytype;

    /**
     * 支付时间
     */
    @Column(name = "payTime")
    private Date paytime;

    /**
     * 提货码
     */
    @Column(name = "verificationCode")
    private String verificationcode;

    /**
     * 未支付失效时间
     */
    @Column(name = "unPayFailureTime")
    private Date unpayfailuretime;

    /**
     * 退款原因
     */
    @Column(name = "refundReason")
    private String refundreason;

    /**
     * 退款时间
     */
    @Column(name = "refundTime")
    private Date refundtime;

    /**
     * 核销类型
     */
    @Column(name = "verificationType")
    private Integer verificationtype;

    /**
     * 核销时间
     */
    @Column(name = "verificationTime")
    private Date verificationtime;

    /**
     * 订单区域ID
     */
    @Column(name = "country_id")
    private long countryId;

    /**
     * 下单渠道
     */
    private Integer channel;

    /**
     * 下单坐标
     */
    private String gps;

    /**
     * 下单时间
     */
    @Column(name = "createTime")
    private Date createtime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getCustomername() {
		return customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public String getCustomerphone() {
		return customerphone;
	}

	public void setCustomerphone(String customerphone) {
		this.customerphone = customerphone;
	}

	public Long getSponsorId() {
		return sponsorId;
	}

	public void setSponsorId(Long sponsorId) {
		this.sponsorId = sponsorId;
	}

	public String getSponsorname() {
		return sponsorname;
	}

	public void setSponsorname(String sponsorname) {
		this.sponsorname = sponsorname;
	}

	public String getSponsorfullname() {
		return sponsorfullname;
	}

	public void setSponsorfullname(String sponsorfullname) {
		this.sponsorfullname = sponsorfullname;
	}

	public String getSponsorphone() {
		return sponsorphone;
	}

	public void setSponsorphone(String sponsorphone) {
		this.sponsorphone = sponsorphone;
	}

	public String getSponsornumber() {
		return sponsornumber;
	}

	public void setSponsornumber(String sponsornumber) {
		this.sponsornumber = sponsornumber;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsname() {
		return goodsname;
	}

	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
	}

	public String getGoodsurl() {
		return goodsurl;
	}

	public void setGoodsurl(String goodsurl) {
		this.goodsurl = goodsurl;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public BigDecimal getOrdertotal() {
		return ordertotal;
	}

	public void setOrdertotal(BigDecimal ordertotal) {
		this.ordertotal = ordertotal;
	}

	public BigDecimal getReceivabletotal() {
		return receivabletotal;
	}

	public void setReceivabletotal(BigDecimal receivabletotal) {
		this.receivabletotal = receivabletotal;
	}

	public BigDecimal getPostage() {
		return postage;
	}

	public void setPostage(BigDecimal postage) {
		this.postage = postage;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public Date getPaytime() {
		return paytime;
	}

	public void setPaytime(Date paytime) {
		this.paytime = paytime;
	}

	public String getVerificationcode() {
		return verificationcode;
	}

	public void setVerificationcode(String verificationcode) {
		this.verificationcode = verificationcode;
	}

	public Date getUnpayfailuretime() {
		return unpayfailuretime;
	}

	public void setUnpayfailuretime(Date unpayfailuretime) {
		this.unpayfailuretime = unpayfailuretime;
	}

	public String getRefundreason() {
		return refundreason;
	}

	public void setRefundreason(String refundreason) {
		this.refundreason = refundreason;
	}

	public Date getRefundtime() {
		return refundtime;
	}

	public void setRefundtime(Date refundtime) {
		this.refundtime = refundtime;
	}

	public Integer getVerificationtype() {
		return verificationtype;
	}

	public void setVerificationtype(Integer verificationtype) {
		this.verificationtype = verificationtype;
	}

	public Date getVerificationtime() {
		return verificationtime;
	}

	public void setVerificationtime(Date verificationtime) {
		this.verificationtime = verificationtime;
	}

	public long getCountryId() {
		return countryId;
	}

	public void setCountryId(long countryId) {
		this.countryId = countryId;
	}

	public Integer getChannel() {
		return channel;
	}

	public void setChannel(Integer channel) {
		this.channel = channel;
	}

	public String getGps() {
		return gps;
	}

	public void setGps(String gps) {
		this.gps = gps;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Integer getPaytype() {
		return paytype;
	}

	public void setPaytype(Integer paytype) {
		this.paytype = paytype;
	}

}