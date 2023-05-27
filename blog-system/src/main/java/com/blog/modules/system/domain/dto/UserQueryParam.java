package com.blog.modules.system.domain.dto;

import lombok.Data;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.blog.annotation.Query;
import org.springframework.format.annotation.DateTimeFormat;

/**
* @author ty
*/
@Data
public class UserQueryParam{

    /** 精确 */
    @Query
    private Long userId;

    private Long deptId;

    @Query(propName = "dept_id", type = Query.Type.IN)
    private Set<Long> deptIds = new HashSet<>();

    @Query(blurry = "email,username,nickName")
    private String blurry;

    /** 精确 */
    @Query
    private Boolean enabled;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Query(type = Query.Type.BETWEEN)
    private List<Date> createTime;
}