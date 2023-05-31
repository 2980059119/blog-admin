package com.blog.modules.mnt.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.base.PageInfo;
import com.blog.base.QueryHelpMybatisPlus;
import com.blog.exception.BadRequestException;
import com.blog.modules.mnt.domain.App;
import com.blog.modules.mnt.domain.dto.AppDto;
import com.blog.modules.mnt.domain.dto.AppQueryParam;
import com.blog.modules.mnt.mapper.AppMapper;
import com.blog.modules.mnt.service.AppService;
import com.blog.utils.ConvertUtil;
import com.blog.utils.FileUtil;
import com.blog.utils.PageUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Service
@AllArgsConstructor
// @CacheConfig(cacheNames = AppService.CACHE_KEY)
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AppServiceImpl extends ServiceImpl<AppMapper, App> implements AppService {

    // private final RedisUtils redisUtils;
//    @Autowired
//    private AppMapper appMapper;

    @Override
    public PageInfo<AppDto> queryAll(AppQueryParam query, Pageable pageable) {
        IPage<App> page = PageUtil.toMybatisPage(pageable);
        IPage<App> pageList = getBaseMapper().selectPage(page, QueryHelpMybatisPlus.getPredicate(query));
        return ConvertUtil.convertPage(pageList, AppDto.class);
    }

    @Override
    public List<AppDto> queryAll(AppQueryParam query){
        return ConvertUtil.convertList(getBaseMapper().selectList(QueryHelpMybatisPlus.getPredicate(query)), AppDto.class);
    }
    
    @Override
    public AppDto findById(Long id) {
        return ConvertUtil.convert(getById(id), AppDto.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(App resources) {
        verification(resources);
        return getBaseMapper().insert(resources) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(App resources){
        verification(resources);
        int ret = getBaseMapper().updateById(resources);
        // delCaches(resources.id);
        return ret > 0;
    }
    private void verification(App resources){
        String opt = "/opt";
        String home = "/home";
        if (!(resources.getUploadPath().startsWith(opt) || resources.getUploadPath().startsWith(home))) {
            throw new BadRequestException("文件只能上传在opt目录或者home目录 ");
        }
        if (!(resources.getDeployPath().startsWith(opt) || resources.getDeployPath().startsWith(home))) {
            throw new BadRequestException("文件只能部署在opt目录或者home目录 ");
        }
        if (!(resources.getBackupPath().startsWith(opt) || resources.getBackupPath().startsWith(home))) {
            throw new BadRequestException("文件只能备份在opt目录或者home目录 ");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(Set<Long> ids){
        // delCaches(ids);
        return getBaseMapper().deleteBatchIds(ids) > 0;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Long id){
        Set<Long> set = new HashSet<>(1);
        set.add(id);
        return this.removeByIds(set);
    }

    /*
    private void delCaches(Long id) {
        redisUtils.delByKey(CACHE_KEY + "::id:", id);
    }

    private void delCaches(Set<Long> ids) {
        for (Long id: ids) {
            delCaches(id);
        }
    }*/

    @Override
    public void download(List<AppDto> all, HttpServletResponse response) throws IOException {
      List<Map<String, Object>> list = new ArrayList<>();
      for (AppDto app : all) {
        Map<String,Object> map = new LinkedHashMap<>();
              map.put("应用名称", app.getName());
              map.put("上传目录", app.getUploadPath());
              map.put("部署路径", app.getDeployPath());
              map.put("备份路径", app.getBackupPath());
              map.put("应用端口", app.getPort());
              map.put("启动脚本", app.getStartScript());
              map.put("部署脚本", app.getDeployScript());
              map.put("创建者", app.getCreateBy());
              map.put("更新者", app.getUpdateBy());
              map.put("创建日期", app.getCreateTime());
              map.put("更新时间", app.getUpdateTime());
        list.add(map);
      }
      FileUtil.downloadExcel(list, response);
    }
}
