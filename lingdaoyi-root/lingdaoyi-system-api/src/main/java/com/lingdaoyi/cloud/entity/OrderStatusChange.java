package com.lingdaoyi.cloud.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "order_status_change")
public class OrderStatusChange {
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 订单id
     */
    @Column(name = "order_id")
    private Long orderId;

    /**
     * 修改之前状态
     */
    @Column(name = "beforeStatus")
    private Integer beforestatus;

    /**
     * 修改之后状态
     */
    @Column(name = "afterStatus")
    private Integer afterstatus;

    /**
     * 修改原因
     */
    @Column(name = "changeReason")
    private String changereason;

    /**
     * 订单状态类型
     */
    @Column(name = "operatorType")
    private Integer operatortype;

    /**
     * 操作人
     */
    @Column(name = "operatorId")
    private Long operatorid;

    @Column(name = "createTime")
    private Date createtime;

    @Column(name = "updateTime")
    private Date updatetime;

    /**
     * 获取id
     *
     * @return id - id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置id
     *
     * @param id id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取订单id
     *
     * @return order_id - 订单id
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * 设置订单id
     *
     * @param orderId 订单id
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    /**
     * 获取修改之前状态
     *
     * @return beforeStatus - 修改之前状态
     */
    public Integer getBeforestatus() {
        return beforestatus;
    }

    /**
     * 设置修改之前状态
     *
     * @param beforestatus 修改之前状态
     */
    public void setBeforestatus(Integer beforestatus) {
        this.beforestatus = beforestatus;
    }

    /**
     * 获取修改之后状态
     *
     * @return afterStatus - 修改之后状态
     */
    public Integer getAfterstatus() {
        return afterstatus;
    }

    /**
     * 设置修改之后状态
     *
     * @param afterstatus 修改之后状态
     */
    public void setAfterstatus(Integer afterstatus) {
        this.afterstatus = afterstatus;
    }

    /**
     * 获取修改原因
     *
     * @return changeReason - 修改原因
     */
    public String getChangereason() {
        return changereason;
    }

    /**
     * 设置修改原因
     *
     * @param changereason 修改原因
     */
    public void setChangereason(String changereason) {
        this.changereason = changereason;
    }

    /**
     * 获取订单状态类型
     *
     * @return operatorType - 订单状态类型
     */
    public Integer getOperatortype() {
        return operatortype;
    }

    /**
     * 设置订单状态类型
     *
     * @param operatortype 订单状态类型
     */
    public void setOperatortype(Integer operatortype) {
        this.operatortype = operatortype;
    }

    /**
     * 获取操作人
     *
     * @return operatorId - 操作人
     */
    public Long getOperatorid() {
        return operatorid;
    }

    /**
     * 设置操作人
     *
     * @param operatorid 操作人
     */
    public void setOperatorid(Long operatorid) {
        this.operatorid = operatorid;
    }

    /**
     * @return createTime
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * @param createtime
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * @return updateTime
     */
    public Date getUpdatetime() {
        return updatetime;
    }

    /**
     * @param updatetime
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}