package com.blog.modules.mnt.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import com.blog.base.CommonDto;

import java.io.Serial;
import java.io.Serializable;

/**
* @author ty
*/
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AppDto extends CommonDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;


    private Long id;

    private String name;

    private String uploadPath;

    private String deployPath;

    private String backupPath;

    private Integer port;

    private String startScript;

    private String deployScript;

}
