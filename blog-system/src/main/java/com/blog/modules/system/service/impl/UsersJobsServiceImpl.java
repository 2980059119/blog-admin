package com.blog.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.modules.system.domain.UsersJobs;
import com.blog.modules.system.mapper.UsersJobsMapper;
import com.blog.modules.system.service.UsersJobsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jinjin on 2020-09-25.
 */
@AllArgsConstructor
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UsersJobsServiceImpl extends ServiceImpl<UsersJobsMapper, UsersJobs> implements UsersJobsService {

    private final UsersJobsMapper usersJobsMapper;

    @Override
    public List<Long> queryUserIdByJobId(Long id) {
        return lambdaQuery().eq(UsersJobs::getJobId, id).list().stream().map(UsersJobs::getUserId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> queryJobIdByUserId(Long id) {
        return lambdaQuery().eq(UsersJobs::getUserId, id).list().stream().map(UsersJobs::getJobId)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByUserId(Long id) {
        return lambdaUpdate().eq(UsersJobs::getUserId, id).remove();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByJobId(Long id) {
        return lambdaUpdate().eq(UsersJobs::getJobId, id).remove();
    }
}
