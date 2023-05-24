package com.blog.modules.system.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.blog.base.CommonEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
* @author ty
*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("sys_dept")
public class Dept extends CommonEntity<Dept> implements Serializable {
    private static final long serialVersionUID = 1L;

    // @ApiModelProperty(value = "ID")
    @TableId(value="dept_id", type= IdType.AUTO)
    private Long id;

    // @ApiModelProperty(value = "上级部门")
    private Long pid;

    // @ApiModelProperty(value = "子部门数目")
    private Integer subCount;

    // @ApiModelProperty(value = "名称")
    @NotBlank
    private String name;

    // @ApiModelProperty(value = "排序")
    private Integer deptSort;

    // @ApiModelProperty(value = "状态")
    @NotNull
    private Boolean enabled;

    public void copyFrom(Dept source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
