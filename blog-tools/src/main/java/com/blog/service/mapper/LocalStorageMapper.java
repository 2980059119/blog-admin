package com.blog.service.mapper;

import com.blog.domain.LocalStorage;
import com.blog.base.CommonMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* @author jinjin
* @date 2020-09-27
*/
@Mapper
public interface LocalStorageMapper extends CommonMapper<LocalStorage> {

}
