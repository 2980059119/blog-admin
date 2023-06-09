package com.blog.modules.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.base.PageInfo;
import com.blog.base.QueryHelpMybatisPlus;
import com.blog.commom.redis.service.RedisService;
import com.blog.exception.BadRequestException;
import com.blog.exception.EntityExistException;
import com.blog.modules.system.domain.Job;
import com.blog.modules.system.domain.UsersJobs;
import com.blog.modules.system.domain.dto.JobDto;
import com.blog.modules.system.domain.dto.JobQueryParam;
import com.blog.modules.system.mapper.JobMapper;
import com.blog.modules.system.mapper.UsersJobsMapper;
import com.blog.modules.system.service.JobService;
import com.blog.modules.system.service.UsersJobsService;
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
@CacheConfig(cacheNames = "job")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements JobService {

    private final JobMapper jobMapper;
    private final UsersJobsMapper usersJobsMapper;
    private final UsersJobsService usersJobsService;
    private final RedisService redisService;

    @Override
    public PageInfo<JobDto> queryAll(JobQueryParam query, Pageable pageable) {
        IPage<Job> page = PageUtil.toMybatisPage(pageable);
        IPage<Job> pageList = jobMapper.selectPage(page, QueryHelpMybatisPlus.getPredicate(query));
        return ConvertUtil.convertPage(pageList, JobDto.class);
    }

    @Override
    public List<JobDto> queryAll(JobQueryParam query) {
        return ConvertUtil.convertList(jobMapper.selectList(QueryHelpMybatisPlus.getPredicate(query)), JobDto.class);
    }

    @Override
    public List<JobDto> queryAll() {
        List<JobDto> list = ConvertUtil.convertList(jobMapper.selectList(Wrappers.emptyWrapper()), JobDto.class);
        redisService.set("job::all", list);
        return list;
    }

    @Override
    public Job getById(Long id) {
        return jobMapper.selectById(id);
    }

    @Override
    @Transactional
    public boolean save(Job resources) {
        Job job = lambdaQuery().eq(Job::getName, resources.getName()).one();
        if (job != null && ObjectUtil.notEqual(resources.getId(), job.getId())) {
            throw new EntityExistException(Job.class, "name", resources.getName());
        }
        return jobMapper.insert(resources) > 0;
    }

    @Override
    @Transactional
    public boolean updateById(Job resources) {
        Job job = lambdaQuery().eq(Job::getName, resources.getName()).one();
        if (job != null && ObjectUtil.notEqual(resources.getId(), job.getId())) {
            throw new EntityExistException(Job.class, "name", resources.getName());
        }
        return jobMapper.updateById(resources) > 0;
    }

    @Override
    @Transactional
    public boolean removeByIds(Set<Long> ids) {
        int ret = jobMapper.deleteBatchIds(ids);
        for (Long id : ids) {
            usersJobsService.removeByJobId(id);
        }
        return ret > 0;
    }

    @Override
    @Transactional
    public boolean removeById(Long id) {
        Set<Long> ids = new HashSet<>(1);
        ids.add(id);
        return this.removeByIds(ids);
    }

    @Override
    public void verification(Set<Long> ids) {
        int count = Math.toIntExact(usersJobsMapper.lambdaQuery().in(UsersJobs::getUserId, ids).count());
        if (count > 0) {
            throw new BadRequestException("所选的岗位中存在用户关联，请解除关联再试！");
        }
    }

    @Override
    public void download(List<JobDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (JobDto job : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("岗位名称", job.getName());
            map.put("岗位状态", job.getEnabled());
            map.put("排序", job.getJobSort());
            map.put("创建者", job.getCreateBy());
            map.put("更新者", job.getUpdateBy());
            map.put("创建日期", job.getCreateTime());
            map.put("更新时间", job.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
