package com.lingdaoyi.cloud.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "authentication_record")
public class AuthenticationRecord {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 客户id
     */
    @Column(name = "account_id")
    private Long accountId;

    /**
     * 鉴权名
     */
    @Column(name = "account_realname")
    private String accountRealname;

    /**
     * 鉴权手机
     */
    @Column(name = "mobile")
    private String mobile;

    /**
     * 鉴权身份证
     */
    @Column(name = "idcard")
    private String idcard;

    /**
     * 鉴权银行卡号
     */
    @Column(name = "bankcard")
    private String bankcard;

    /**
     * 鉴权银行名
     */
    @Column(name = "bank_name")
    private String bankName;

    /**
     * 银行编码
     */
    @Column(name = "bank_code")
    private String bankCode;

    /**
     * 银行身份证
     */
    @Column(name = "bank_idcard")
    private String bankIdcard;

    /**
     * 开户名
     */
    @Column(name = "bank_realname")
    private String bankRealname;

    /**
     * 银行预留手机号
     */
    @Column(name = "bank_mobile")
    private String bankMobile;

    /**
     * 创建时间
     */
    @Column(name = "gmt_create")
    private Date gmtCreate;
    
    @Column(name = "enkey")
    private String enkey;
    
    @Column(name = "is_ok")
    private Integer isOk;
    @Column(name = "ordersign")
    private String ordersign;
    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取客户id
     *
     * @return account_id - 客户id
     */
    public Long getAccountId() {
        return accountId;
    }

    /**
     * 设置客户id
     *
     * @param accountId 客户id
     */
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    /**
     * 获取鉴权名
     *
     * @return account_realname - 鉴权名
     */
    public String getAccountRealname() {
        return accountRealname;
    }

    /**
     * 设置鉴权名
     *
     * @param accountRealname 鉴权名
     */
    public void setAccountRealname(String accountRealname) {
        this.accountRealname = accountRealname;
    }

    /**
     * 获取鉴权手机
     *
     * @return mobile - 鉴权手机
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置鉴权手机
     *
     * @param mobile 鉴权手机
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * 获取鉴权身份证
     *
     * @return idcard - 鉴权身份证
     */
    public String getIdcard() {
        return idcard;
    }

    /**
     * 设置鉴权身份证
     *
     * @param idcard 鉴权身份证
     */
    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    /**
     * 获取鉴权银行卡号
     *
     * @return bankcard - 鉴权银行卡号
     */
    public String getBankcard() {
        return bankcard;
    }

    /**
     * 设置鉴权银行卡号
     *
     * @param bankcard 鉴权银行卡号
     */
    public void setBankcard(String bankcard) {
        this.bankcard = bankcard;
    }

    /**
     * 获取鉴权银行名
     *
     * @return bank_name - 鉴权银行名
     */
    public String getBankName() {
        return bankName;
    }

    /**
     * 设置鉴权银行名
     *
     * @param bankName 鉴权银行名
     */
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    /**
     * 获取银行编码
     *
     * @return bank_code - 银行编码
     */
    public String getBankCode() {
        return bankCode;
    }

    /**
     * 设置银行编码
     *
     * @param bankCode 银行编码
     */
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    /**
     * 获取银行身份证
     *
     * @return bank_idcard - 银行身份证
     */
    public String getBankIdcard() {
        return bankIdcard;
    }

    /**
     * 设置银行身份证
     *
     * @param bankIdcard 银行身份证
     */
    public void setBankIdcard(String bankIdcard) {
        this.bankIdcard = bankIdcard;
    }

    /**
     * 获取开户名
     *
     * @return bank_realname - 开户名
     */
    public String getBankRealname() {
        return bankRealname;
    }

    /**
     * 设置开户名
     *
     * @param bankRealname 开户名
     */
    public void setBankRealname(String bankRealname) {
        this.bankRealname = bankRealname;
    }

    /**
     * 获取银行预留手机号
     *
     * @return bank_mobile - 银行预留手机号
     */
    public String getBankMobile() {
        return bankMobile;
    }

    /**
     * 设置银行预留手机号
     *
     * @param bankMobile 银行预留手机号
     */
    public void setBankMobile(String bankMobile) {
        this.bankMobile = bankMobile;
    }

    /**
     * 获取创建时间
     *
     * @return gmt_create - 创建时间
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * 设置创建时间
     *
     * @param gmtCreate 创建时间
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }



	public String getEnkey() {
		return enkey;
	}

	public void setEnkey(String enkey) {
		this.enkey = enkey;
	}

	public Integer getIsOk() {
		return isOk;
	}

	public void setIsOk(Integer isOk) {
		this.isOk = isOk;
	}

	public String getOrdersign() {
		return ordersign;
	}

	public void setOrdersign(String ordersign) {
		this.ordersign = ordersign;
	}
    
    
}