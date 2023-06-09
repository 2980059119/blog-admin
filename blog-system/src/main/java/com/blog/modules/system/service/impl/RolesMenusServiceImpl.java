package com.blog.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.modules.system.domain.RolesMenus;
import com.blog.modules.system.mapper.RolesMenusMapper;
import com.blog.modules.system.service.RolesMenusService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author IKUN
 * @since 2023-05-31 21:25:43
 */
@AllArgsConstructor
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RolesMenusServiceImpl extends ServiceImpl<RolesMenusMapper, RolesMenus> implements RolesMenusService {

    private final RolesMenusMapper rolesMenusMapper;

    @Override
    public List<Long> queryMenuIdByRoleId(Long id) {
        return lambdaQuery().eq(RolesMenus::getRoleId, id).list().stream().map(RolesMenus::getMenuId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> queryRoleIdByMenuId(Long id) {
        return lambdaQuery().eq(RolesMenus::getMenuId, id).list().stream().map(RolesMenus::getRoleId)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByRoleId(Long id) {
        return lambdaUpdate().eq(RolesMenus::getRoleId, id).remove();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByMenuId(Long id) {
        return lambdaUpdate().eq(RolesMenus::getMenuId, id).remove();
    }

}
