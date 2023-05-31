package com.blog.modules.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.base.PageInfo;
import com.blog.base.QueryHelpMybatisPlus;
import com.blog.commom.redis.service.RedisService;
import com.blog.modules.system.domain.Dict;
import com.blog.modules.system.domain.DictDetail;
import com.blog.modules.system.domain.dto.DictDto;
import com.blog.modules.system.domain.dto.DictQueryParam;
import com.blog.modules.system.mapper.DictDetailMapper;
import com.blog.modules.system.mapper.DictMapper;
import com.blog.modules.system.service.DictService;
import com.blog.utils.CacheKey;
import com.blog.utils.ConvertUtil;
import com.blog.utils.FileUtil;
import com.blog.utils.PageUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

/**
 * @author IKUN
 * @since 2023-05-31 21:25:43
 */
@Service
@AllArgsConstructor
@CacheConfig(cacheNames = DictServiceImpl.CACHE_KEY)
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    public static final String CACHE_KEY = "${changeClassName}";
    private final RedisService redisService;
    private final DictMapper dictMapper;
    private final DictDetailMapper detailMapper;

    @Override
    public PageInfo<DictDto> queryAll(DictQueryParam query, Pageable pageable) {
        IPage<Dict> page = PageUtil.toMybatisPage(pageable);
        IPage<Dict> pageList = dictMapper.selectPage(page, QueryHelpMybatisPlus.getPredicate(query));
        return ConvertUtil.convertPage(pageList, DictDto.class);
    }

    @Override
    public List<DictDto> queryAll(DictQueryParam query){
        return ConvertUtil.convertList(dictMapper.selectList(QueryHelpMybatisPlus.getPredicate(query)), DictDto.class);
    }

    @Override
    public Dict getById(Long id) {
        return dictMapper.selectById(id);
    }

    @Override
    public DictDto findById(Long id) {
        return ConvertUtil.convert(getById(id), DictDto.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(Dict resources) {
        return dictMapper.insert(resources) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(Dict resources){
        return dictMapper.updateById(resources) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(Set<Long> ids){
        List<Dict> dicts = dictMapper.selectBatchIds(ids);
        boolean ret = dictMapper.deleteBatchIds(ids) > 0;
        for (Dict dict : dicts) {
            detailMapper.lambdaUpdate().eq(DictDetail::getDictId, dict.getId()).remove();
            delCaches(dict);
        }
        return ret;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Long id){
        Set<Long> ids = new HashSet<>(1);
        ids.add(id);
        return removeByIds(ids);
    }
    @Override
    public void download(List<DictDto> all, HttpServletResponse response) throws IOException {
      List<Map<String, Object>> list = new ArrayList<>();
      for (DictDto dict : all) {
        Map<String,Object> map = new LinkedHashMap<>();
              map.put("字典名称", dict.getName());
              map.put("描述", dict.getDescription());
              map.put("创建者", dict.getCreateBy());
              map.put("更新者", dict.getUpdateBy());
              map.put("创建日期", dict.getCreateTime());
              map.put("更新时间", dict.getUpdateTime());
        list.add(map);
      }
      FileUtil.downloadExcel(list, response);
    }

    private void delCaches(Dict dict){
        redisService.del(CacheKey.DICTDEAIL_DICTNAME + dict.getName());
        redisService.del(CacheKey.DICTDEAIL_DICTID + dict.getId());
        redisService.del(CacheKey.DICT_ID + dict.getId());
    }
}
