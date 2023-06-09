package com.blog.modules.blog.entity;


import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * (Info)表实体类
 *
 * @author IKUN
 * @since 2023-06-05 22:47:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("blog_info")
public class InfoEntity {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 名字
     */
    private String name;
    /**
     * 标题
     */
    private String title;
    /**
     * 个性签名
     */
    private String sign;
    /**
     * 关于
     */
    private String concerning;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 背景
     */
    private String background;
    /**
     * 头像后的背景
     */
    private String siteBg;


}

