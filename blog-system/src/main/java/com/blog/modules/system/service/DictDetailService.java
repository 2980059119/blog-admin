package com.blog.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.base.PageInfo;
import com.blog.modules.system.domain.DictDetail;
import com.blog.modules.system.domain.dto.DictDetailDto;
import com.blog.modules.system.domain.dto.DictDetailQueryParam;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author IKUN
 * @since 2023-05-31 21:25:43
 */
public interface DictDetailService  extends IService<DictDetail> {

    /**
    * 查询数据分页
    * @param query 条件
    * @param pageable 分页参数
    * @return map[totalElements, content]
    */
    PageInfo<DictDetailDto> queryAll(DictDetailQueryParam query, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param query 条件参数
    * @return List<DictDetailDto>
    */
    List<DictDetailDto> queryAll(DictDetailQueryParam query);

    /**
     * 根据字典名称获取字典详情
     * @param name 字典名称
     * @return /
     */
    List<DictDetailDto> getDictByName(String name);

    PageInfo<DictDetailDto> getDictByName(String dictName, Pageable pageable);

    boolean removeById(Long id);
    boolean removeByDictId(Long id);

    boolean updateById(DictDetailDto resources);

    boolean save(DictDetailDto resources);

}
