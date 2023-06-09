package com.blog.modules.system.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.blog.base.CommonEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author IKUN
 * @since 2023-05-31 21:25:43
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("sys_user")
public class User extends CommonEntity<User> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * 部门名称
     */
    private Long deptId;

    /**
     * 用户名
     */
    @NotBlank
    private String username;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 性别
     */
    private String gender;

    /**
     * 手机号码
     */
    @NotBlank
    private String phone;

    /**
     * 邮箱
     */
    @NotBlank
    private String email;

    /**
     * 头像地址
     */
    private String avatarName;

    /**
     * 头像真实路径
     */
    private String avatarPath;

    /**
     * 密码
     */
    private String password;

    /**
     * 是否为admin账号
     */
    private Boolean isAdmin;

    /**
     * 状态：1启用、0禁用
     */
    private Boolean enabled;

    /**
     * 修改密码的时间
     */
    private Date pwdResetTime;

    public <T> void copyFrom(T source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
