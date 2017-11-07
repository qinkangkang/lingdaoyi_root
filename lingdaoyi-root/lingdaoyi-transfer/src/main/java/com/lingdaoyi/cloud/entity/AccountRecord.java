package com.lingdaoyi.cloud.entity;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;
import javax.persistence.*;

@Table(name = "account_record")
public class AccountRecord {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 当前客户表关联id
	 */
	@Column(name = "account_id")
	private Long accountId;

	/**
	 * 关联订单id(用于购物扣款)
	 */
	@Column(name = "pay_no")
	private String payNo;

	/**
	 * 0:充值,1:转账到余额(他方);2:收钱;3:提现;4:购物扣款;5:二维码支付
	 */
	@Column
	private Integer type;

	/**
	 * 关联银行卡id:(充值时用)
	 */
	@Column(name = "blank_card_id")
	private Long blankCardId;

	/**
	 * 交易金额
	 */
	@Column(name = "transfer_money")
	private BigDecimal transferMoney;
	
	@Column(name = "currency_name")
	private  String currencyName; // 转账币种

	/**
	 * 转账说明
	 */
	@Column(name = "transfer_instructions")
	private String transferInstructions;

	/**
	 * 收账的账户id(转账/或扣款)
	 */
	@Column(name = "receive_account_id")
	private Long receiveAccountId;

	/**
	 * 收账账户的银行卡id(提现/购物消费/转账)
	 */
	@Column(name = "receive_card_id")
	private Long receiveCardId;

	/**
	 * 交易完成状态:0:未完成;1:付款成功 2:银行处理中,3:处理完成
	 */
	@Column(name = "is_success")
	private String isSuccess;

	
	@Column(name = "payAdvance_isSuccess")
	private String payAdvance_isSuccess;//预付卡订单扣款是否完成;1:完成;0:未完成
	
	@Column(name = "payAdvance_orderNO")
	private  Long payAdvanceOrder_NO;//预付卡订单编号;
	
	@Column
	private String remarks;//转账备注
	@Column(name = "transfer_way")
	private String  transferWay;

	/**
	 * 登录gps
	 */
	private String gps;

	/**
	 * 登录ip
	 */
	@Column(name = "address_ip")
	private String addressIp;

	/**
	 * 创建时间
	 */
	@Column(name = "gmt_create")
	private Date gmtCreate;

	/**
	 * 1 表示删除，0 表示未删除
	 */
	@Column(name = "is_deleted")
	private Byte isDeleted;

	/**
	 * 汇率id
	 */
	@Column(name = "exchange_id")
	private Long exchangeId;

	/**
	 * @return id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	
	
	public Long getPayAdvanceOrder_NO() {
		return payAdvanceOrder_NO;
	}

	public void setPayAdvanceOrder_NO(Long payAdvanceOrder_NO) {
		this.payAdvanceOrder_NO = payAdvanceOrder_NO;
	}

	/**
	 * 获取当前客户表关联id
	 *
	 * @return account_id - 当前客户表关联id
	 */
	public Long getAccountId() {
		return accountId;
	}

	/**
	 * 设置当前客户表关联id
	 *
	 * @param accountId
	 *            当前客户表关联id
	 */
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	/**
	 * 获取关联订单id(用于购物扣款)
	 *
	 * @return pay_no - 关联订单id(用于购物扣款)
	 */
	public String getPayNo() {
		return payNo;
	}

	
	public String getPayAdvance_isSuccess() {
		return payAdvance_isSuccess;
	}

	public void setPayAdvance_isSuccess(String payAdvance_isSuccess) {
		this.payAdvance_isSuccess = payAdvance_isSuccess;
	}

	/**
	 * 设置关联订单id(用于购物扣款)
	 *
	 * @param payNo
	 *            关联订单id(用于购物扣款)
	 */
	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}

	/**
	 * 获取0:充值,1:转账到余额(他方);2:转账到银行卡(他方);
	 *
	 * @return type - 0:充值,1:转账到余额(他方);2:转账到银行卡(他方);
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * 设置0:充值,1:转账到余额(他方);2:转账到银行卡(他方);
	 *
	 * @param type
	 *            0:充值,1:转账到余额(他方);2:转账到银行卡(他方);
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * 获取关联银行卡id:(充值时用)
	 *
	 * @return blank_card_id - 关联银行卡id:(充值时用)
	 */
	public Long getBlankCardId() {
		return blankCardId;
	}

	/**
	 * 设置关联银行卡id:(充值时用)
	 *
	 * @param blankCardId
	 *            关联银行卡id:(充值时用)
	 */
	public void setBlankCardId(Long blankCardId) {
		this.blankCardId = blankCardId;
	}

	/**
	 * 获取交易金额
	 *
	 * @return transfer_money - 交易金额
	 */
	public BigDecimal getTransferMoney() {
		return transferMoney;
	}

	/**
	 * 设置交易金额
	 *
	 * @param transferMoney
	 *            交易金额
	 */
	public void setTransferMoney(BigDecimal transferMoney) {
		this.transferMoney = transferMoney;
	}

	/**
	 * 获取转账说明
	 *
	 * @return transfer_instructions - 转账说明
	 */
	public String getTransferInstructions() {
		return transferInstructions;
	}

	/**
	 * 设置转账说明
	 *
	 * @param transferInstructions
	 *            转账说明
	 */
	public void setTransferInstructions(String transferInstructions) {
		this.transferInstructions = transferInstructions;
	}

	/**
	 * 获取收账的账户id(转账/或扣款)
	 *
	 * @return receive_account_id - 收账的账户id(转账/或扣款)
	 */
	public Long getReceiveAccountId() {
		return receiveAccountId;
	}

	/**
	 * 设置收账的账户id(转账/或扣款)
	 *
	 * @param receiveCustomerId
	 *            收账的账户id(转账/或扣款)
	 */
	public void setReceiveAccountId(Long receiveAccountId) {
		this.receiveAccountId = receiveAccountId;
	}

	/**
	 * 获取收账账户的银行卡id(提现/购物消费/转账)
	 *
	 * @return receive_card_id - 收账账户的银行卡id(提现/购物消费/转账)
	 */
	public Long getReceiveCardId() {
		return receiveCardId;
	}

	/**
	 * 设置收账账户的银行卡id(提现/购物消费/转账)
	 *
	 * @param receiveCardId
	 *            收账账户的银行卡id(提现/购物消费/转账)
	 */
	public void setReceiveCardId(Long receiveCardId) {
		this.receiveCardId = receiveCardId;
	}

	/**
	 * 获取交易完成状态:0成功；1：失败;
	 *
	 * @return is_success - 交易完成状态:0成功；1：失败;
	 */
	public String getIsSuccess() {
		return isSuccess;
	}


	/**
	 * 设置交易完成状态:0成功；1：失败;
	 *
	 * @param c
	 *            交易完成状态:0成功；1：失败;
	 */
	public void setIsSuccess(String isSuccess) {
		this.isSuccess = isSuccess;
	}

	/**
	 * 获取登录gps
	 *
	 * @return gps - 登录gps
	 */
	public String getGps() {
		return gps;
	}

	/**
	 * 设置登录gps
	 *
	 * @param gps
	 *            登录gps
	 */
	public void setGps(String gps) {
		this.gps = gps;
	}

	/**
	 * 获取登录ip
	 *
	 * @return address_ip - 登录ip
	 */
	public String getAddressIp() {
		return addressIp;
	}

	/**
	 * 设置登录ip
	 *
	 * @param addressIp
	 *            登录ip
	 */
	public void setAddressIp(String addressIp) {
		this.addressIp = addressIp;
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
	 * @param gmtCreate
	 *            创建时间
	 */
	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	/**
	 * 获取1 表示删除，0 表示未删除
	 *
	 * @return is_deleted - 1 表示删除，0 表示未删除
	 */
	public Byte getIsDeleted() {
		return isDeleted;
	}

	/**
	 * 设置1 表示删除，0 表示未删除
	 *
	 * @param isDeleted
	 *            1 表示删除，0 表示未删除
	 */
	public void setIsDeleted(Byte isDeleted) {
		this.isDeleted = isDeleted;
	}

	/**
	 * 获取汇率id
	 *
	 * @return exchange_id - 汇率id
	 */
	public Long getExchangeId() {
		return exchangeId;
	}

	/**
	 * 设置汇率id
	 *
	 * @param exchangeId
	 *            汇率id
	 */
	public void setExchangeId(Long exchangeId) {
		this.exchangeId = exchangeId;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getTransferWay() {
		return transferWay;
	}

	public void setTransferWay(String transferWay) {
		this.transferWay = transferWay;
	}
	
	

}