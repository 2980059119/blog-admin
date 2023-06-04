package com.blog.modules.blog.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * (Notebook)表实体类
 *
 * @author IKUN
 * @since 2023-05-27 22:06:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotebookVo {
    /**
     * id
     */
    private Long id;

    /**
     * 笔记
     */
    private String notebook;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 更新者
     */
    @JsonIgnore
    private String updateBy;
    /**
     * 创建日期
     */
    private Date createTime;
    /**
     * 更新时间
     */
    @JsonIgnore
    private Date updateTime;
    /**
     * 1表示已删除，0表示未删除
     */
    @JsonIgnore
    private Integer deleteFlag;


}

