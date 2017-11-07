package com.lingdaoyi.cloud.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "order_payinfo")
public class OrderPayinfo {
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 订单号
     */
    @Column(name = "order_id")
    private Long orderId;

    /**
     * 订单ID
     */
    @Column(name = "orderNum")
    private String ordernum;

    /**
     * 订单类型
     */
    @Column(name = "orderType")
    private Integer ordertype;

    /**
     * 支付人ID
     */
    @Column(name = "account_id")
    private Long accountId;

    /**
     * 客户端类型
     */
    @Column(name = "clientType")
    private Integer clienttype;

    /**
     * 1、收款，客户支付给我方\r\n            2、退款，我方支付给客户
     */
    @Column(name = "payInOut")
    private Integer payinout;

    /**
     * 支付币种
     */
    @Column(name = "currencyType")
    private String currencytype;

    /**
     * 支付金额
     */
    @Column(name = "payAmount")
    private BigDecimal payamount;

    /**
     * 支付类型 20.微信 30.支付宝
     */
    @Column(name = "payType")
    private Integer paytype;

    /**
     * charge查询id
     */
    @Column(name = "payId")
    private Long payid;

    /**
     * 支付状态
     */
    private Integer status;

    /**
     * 支付渠道
     */
    private String channel;

    /**
     * 创建时间
     */
    @Column(name = "createTime")
    private Date createtime;

    /**
     * 确认支付时间
     */
    @Column(name = "confirmPayTime")
    private Date confirmpaytime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}

	public Integer getOrdertype() {
		return ordertype;
	}

	public void setOrdertype(Integer ordertype) {
		this.ordertype = ordertype;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Integer getClienttype() {
		return clienttype;
	}

	public void setClienttype(Integer clienttype) {
		this.clienttype = clienttype;
	}

	public Integer getPayinout() {
		return payinout;
	}

	public void setPayinout(Integer payinout) {
		this.payinout = payinout;
	}

	public String getCurrencytype() {
		return currencytype;
	}

	public void setCurrencytype(String currencytype) {
		this.currencytype = currencytype;
	}

	public BigDecimal getPayamount() {
		return payamount;
	}

	public void setPayamount(BigDecimal payamount) {
		this.payamount = payamount;
	}

	public Integer getPaytype() {
		return paytype;
	}

	public void setPaytype(Integer paytype) {
		this.paytype = paytype;
	}

	public Long getPayid() {
		return payid;
	}

	public void setPayid(Long payid) {
		this.payid = payid;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getConfirmpaytime() {
		return confirmpaytime;
	}

	public void setConfirmpaytime(Date confirmpaytime) {
		this.confirmpaytime = confirmpaytime;
	}

}