package com.lingdaoyi.cloud.entity.sys;

import java.util.Date;
import javax.persistence.*;

@Table(name = "sys_sms")
public class SysSms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 发送时间
     */
    @Column(name = "send_time")
    private Date sendTime;

    /**
     * 接收手机号
     */
    @Column(name = "send_phone")
    private String sendPhone;

    /**
     * 短信内容
     */
    @Column(name = "sms_content")
    private String smsContent;

    /**
     * 短信类型
     */
    @Column(name = "sms_type")
    private Integer smsType;

    /**
     * 发送是否成功
     */
    @Column(name = "send_success")
    private Integer sendSuccess;

    /**
     * 发送响应信息
     */
    @Column(name = "send_response")
    private String sendResponse;

    /**
     * 创建时间
     */
    @Column(name = "gmt_create")
    private Date gmtCreate;

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

    /**
     * 获取发送时间
     *
     * @return send_time - 发送时间
     */
    public Date getSendTime() {
        return sendTime;
    }

    /**
     * 设置发送时间
     *
     * @param sendTime 发送时间
     */
    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    /**
     * 获取接收手机号
     *
     * @return send_phone - 接收手机号
     */
    public String getSendPhone() {
        return sendPhone;
    }

    /**
     * 设置接收手机号
     *
     * @param sendPhone 接收手机号
     */
    public void setSendPhone(String sendPhone) {
        this.sendPhone = sendPhone;
    }

    /**
     * 获取短信内容
     *
     * @return sms_content - 短信内容
     */
    public String getSmsContent() {
        return smsContent;
    }

    /**
     * 设置短信内容
     *
     * @param smsContent 短信内容
     */
    public void setSmsContent(String smsContent) {
        this.smsContent = smsContent;
    }

    /**
     * 获取短信类型
     *
     * @return sms_type - 短信类型
     */
    public Integer getSmsType() {
        return smsType;
    }

    /**
     * 设置短信类型
     *
     * @param smsType 短信类型
     */
    public void setSmsType(Integer smsType) {
        this.smsType = smsType;
    }

    /**
     * 获取发送是否成功
     *
     * @return send_success - 发送是否成功
     */
    public Integer getSendSuccess() {
        return sendSuccess;
    }

    /**
     * 设置发送是否成功
     *
     * @param sendSuccess 发送是否成功
     */
    public void setSendSuccess(Integer sendSuccess) {
        this.sendSuccess = sendSuccess;
    }

    /**
     * 获取发送响应信息
     *
     * @return send_response - 发送响应信息
     */
    public String getSendResponse() {
        return sendResponse;
    }

    /**
     * 设置发送响应信息
     *
     * @param sendResponse 发送响应信息
     */
    public void setSendResponse(String sendResponse) {
        this.sendResponse = sendResponse;
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
}