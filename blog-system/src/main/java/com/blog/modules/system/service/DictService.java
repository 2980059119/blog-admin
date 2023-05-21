package com.blog.modules.system.service;

import com.blog.modules.system.service.dto.DictQueryParam;
import com.blog.base.CommonService;
import com.blog.base.PageInfo;
import com.blog.modules.system.domain.Dict;
import com.blog.modules.system.service.dto.DictDto;
import org.springframework.data.domain.Pageable;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.io.IOException;
import java.util.Set;

/**
* @author jinjin
* @date 2020-09-24
*/
public interface DictService  extends CommonService<Dict>{

    /**
    * 查询数据分页
    * @param query 条件
    * @param pageable 分页参数
    * @return map[totalElements, content]
    */
    PageInfo<DictDto> queryAll(DictQueryParam query, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param query 条件参数
    * @return List<DictDto>
    */
    List<DictDto> queryAll(DictQueryParam query);

    Dict getById(Long id);
    DictDto findById(Long id);
    @Override
    boolean save(Dict resources);
    @Override
    boolean updateById(Dict resources);
    boolean removeById(Long id);
    boolean removeByIds(Set<Long> ids);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<DictDto> all, HttpServletResponse response) throws IOException;
}
