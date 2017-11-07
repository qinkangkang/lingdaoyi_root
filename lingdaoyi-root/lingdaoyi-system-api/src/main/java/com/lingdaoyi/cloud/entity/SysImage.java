package com.lingdaoyi.cloud.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "sys_image")
public class SysImage {
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 文件名
     */
    @Column(name = "fileName")
    private String filename;

    /**
     * 文件名
     */
    @Column(name = "storeFileName")
    private String storefilename;

    /**
     * 图片类型
     */
    @Column(name = "storefileExt")
    private String storefileext;

    /**
     * 图片绝对路径
     */
    @Column(name = "relativePath")
    private String relativepath;

    /**
     * 图片大小
     */
    @Column(name = "fileSize")
    private Long filesize;

    /**
     * 图片宽度
     */
    @Column(name = "imageWidth")
    private Integer imagewidth;

    /**
     * 图片高度
     */
    @Column(name = "imageHeight")
    private Integer imageheight;

    /**
     * 上传时间
     */
    @Column(name = "uploadTime")
    private Date uploadtime;

    /**
     * 上传人
     */
    @Column(name = "uploaderId")
    private Integer uploaderid;

    /**
     * 图片状态 0 不可用  1可用
     */
    private Integer status;

    /**
     * 实体id
     */
    @Column(name = "entityId")
    private Long entityid;

    /**
     * 实体名
     */
    @Column(name = "entityType")
    private Integer entitytype;

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
     * 获取文件名
     *
     * @return fileName - 文件名
     */
    public String getFilename() {
        return filename;
    }

    /**
     * 设置文件名
     *
     * @param filename 文件名
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * 获取文件名
     *
     * @return storeFileName - 文件名
     */
    public String getStorefilename() {
        return storefilename;
    }

    /**
     * 设置文件名
     *
     * @param storefilename 文件名
     */
    public void setStorefilename(String storefilename) {
        this.storefilename = storefilename;
    }

    /**
     * 获取图片类型
     *
     * @return storefileExt - 图片类型
     */
    public String getStorefileext() {
        return storefileext;
    }

    /**
     * 设置图片类型
     *
     * @param storefileext 图片类型
     */
    public void setStorefileext(String storefileext) {
        this.storefileext = storefileext;
    }

    /**
     * 获取图片绝对路径
     *
     * @return relativePath - 图片绝对路径
     */
    public String getRelativepath() {
        return relativepath;
    }

    /**
     * 设置图片绝对路径
     *
     * @param relativepath 图片绝对路径
     */
    public void setRelativepath(String relativepath) {
        this.relativepath = relativepath;
    }

    /**
     * 获取图片大小
     *
     * @return fileSize - 图片大小
     */
    public Long getFilesize() {
        return filesize;
    }

    /**
     * 设置图片大小
     *
     * @param filesize 图片大小
     */
    public void setFilesize(Long filesize) {
        this.filesize = filesize;
    }

    /**
     * 获取图片宽度
     *
     * @return imageWidth - 图片宽度
     */
    public Integer getImagewidth() {
        return imagewidth;
    }

    /**
     * 设置图片宽度
     *
     * @param imagewidth 图片宽度
     */
    public void setImagewidth(Integer imagewidth) {
        this.imagewidth = imagewidth;
    }

    /**
     * 获取图片高度
     *
     * @return imageHeight - 图片高度
     */
    public Integer getImageheight() {
        return imageheight;
    }

    /**
     * 设置图片高度
     *
     * @param imageheight 图片高度
     */
    public void setImageheight(Integer imageheight) {
        this.imageheight = imageheight;
    }

    /**
     * 获取上传时间
     *
     * @return uploadTime - 上传时间
     */
    public Date getUploadtime() {
        return uploadtime;
    }

    /**
     * 设置上传时间
     *
     * @param uploadtime 上传时间
     */
    public void setUploadtime(Date uploadtime) {
        this.uploadtime = uploadtime;
    }

    /**
     * 获取上传人
     *
     * @return uploaderId - 上传人
     */
    public Integer getUploaderid() {
        return uploaderid;
    }

    /**
     * 设置上传人
     *
     * @param uploaderid 上传人
     */
    public void setUploaderid(Integer uploaderid) {
        this.uploaderid = uploaderid;
    }

    /**
     * 获取图片状态 0 不可用  1可用
     *
     * @return status - 图片状态 0 不可用  1可用
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置图片状态 0 不可用  1可用
     *
     * @param status 图片状态 0 不可用  1可用
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取实体id
     *
     * @return entityId - 实体id
     */
    public Long getEntityid() {
        return entityid;
    }

    /**
     * 设置实体id
     *
     * @param entityid 实体id
     */
    public void setEntityid(Long entityid) {
        this.entityid = entityid;
    }

    /**
     * 获取实体名
     *
     * @return entityType - 实体名
     */
    public Integer getEntitytype() {
        return entitytype;
    }

    /**
     * 设置实体名
     *
     * @param entitytype 实体名
     */
    public void setEntitytype(Integer entitytype) {
        this.entitytype = entitytype;
    }
}