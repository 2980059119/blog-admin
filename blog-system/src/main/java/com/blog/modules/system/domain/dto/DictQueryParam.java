package com.blog.modules.system.domain.dto;

import lombok.Data;
import com.blog.annotation.Query;

/**
* @author ty
*/
@Data
public class DictQueryParam{

    @Query(blurry = "name,description")
    private String blurry;

    /** 精确 */
    @Query
    private Long dictId;

    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    private String name;
}
