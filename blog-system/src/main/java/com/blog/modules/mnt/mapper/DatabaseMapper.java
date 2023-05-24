package com.blog.modules.mnt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.modules.mnt.domain.Database;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* @author ty
* 
*/
@Mapper
public interface DatabaseMapper extends BaseMapper<Database> {

}
