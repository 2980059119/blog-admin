package com.blog.modules.quartz.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.blog.base.CommonEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
@TableName("sys_quartz_job")
public class QuartzJob extends CommonEntity<QuartzJob> implements Serializable {
    public static final String JOB_KEY = "JOB_KEY";
    private static final long serialVersionUID = 1L;
    // @ApiModelProperty(value = "ID")
    @TableId(value = "job_id", type = IdType.AUTO)
    private Long id;


    @TableField(exist = false)
    // @ApiModelProperty(value = "用于子任务唯一标识", hidden = true)
    private String uuid;

    // @ApiModelProperty(value = "Spring Bean名称")
    private String beanName;

    // @ApiModelProperty(value = "cron 表达式")
    private String cronExpression;

    // @ApiModelProperty(value = "状态：1暂停、0启用")
    private Boolean isPause;

    // @ApiModelProperty(value = "任务名称")
    private String jobName;

    // @ApiModelProperty(value = "方法名称")
    private String methodName;

    // @ApiModelProperty(value = "参数")
    private String params;

    // @ApiModelProperty(value = "备注")
    private String description;

    // @ApiModelProperty(value = "负责人")
    private String personInCharge;

    // @ApiModelProperty(value = "报警邮箱")
    private String email;

    // @ApiModelProperty(value = "子任务ID")
    private String subTask;

    // @ApiModelProperty(value = "任务失败后是否暂停")
    private Boolean pauseAfterFailure;


    public void copyFrom(QuartzJob source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
