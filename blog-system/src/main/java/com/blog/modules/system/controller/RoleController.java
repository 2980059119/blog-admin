package com.blog.modules.system.controller;

import cn.hutool.core.lang.Dict;
import com.blog.base.CommonEntity;
import com.blog.exception.BadRequestException;
import com.blog.modules.logging.annotation.Log;
import com.blog.modules.system.domain.dto.RoleDto;
import com.blog.modules.system.domain.dto.RoleQueryParam;
import com.blog.modules.system.domain.dto.RoleSmallDto;
import com.blog.modules.system.service.RoleService;
import com.blog.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 系统：角色管理
 *
 * @author IKUN
 * @since 2023-05-31 21:25:43
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    private static final String ENTITY_NAME = "role";

    /**
     * 获取单个role
     */
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('roles:list')")
    public ResponseEntity<Object> query(@PathVariable Long id) {
        return new ResponseEntity<>(roleService.findById(id), HttpStatus.OK);
    }

    /**
     * 导出角色数据
     */
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('role:list')")
    public void download(HttpServletResponse response, RoleQueryParam criteria) throws IOException {
        roleService.download(roleService.queryAll(criteria), response);
    }

    /**
     * 返回全部的角色
     */
    @GetMapping(value = "/all")
    @PreAuthorize("@el.check('roles:list','user:add','user:edit')")
    public ResponseEntity<Object> query() {
        return new ResponseEntity<>(roleService.queryAll(), HttpStatus.OK);
    }

    /**
     * 查询角色
     */
    @GetMapping
    @PreAuthorize("@el.check('roles:list')")
    public ResponseEntity<Object> query(RoleQueryParam criteria, Pageable pageable) {
        return new ResponseEntity<>(roleService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    /**
     * 获取用户级别
     */
    @GetMapping(value = "/level")
    public ResponseEntity<Object> getLevel() {
        return new ResponseEntity<>(Dict.create().set("level", getLevels(null)), HttpStatus.OK);
    }

    /**
     * 新增角色
     */
    @Log("新增角色")
    @PostMapping
    @PreAuthorize("@el.check('roles:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody RoleDto resources) {
        if (resources.getId() != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        getLevels(resources.getLevel());
        roleService.save(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 修改角色
     */
    @Log("修改角色")
    @PutMapping
    @PreAuthorize("@el.check('roles:edit')")
    public ResponseEntity<Object> update(@Validated(CommonEntity.Update.class) @RequestBody RoleDto resources) {
        getLevels(resources.getLevel());
        roleService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 修改角色菜单
     */
    @Log("修改角色菜单")
    @PutMapping(value = "/menu")
    @PreAuthorize("@el.check('roles:edit')")
    public ResponseEntity<Object> updateMenu(@RequestBody RoleDto resources) {
        RoleDto role = roleService.findById(resources.getId());
        getLevels(role.getLevel());
        roleService.updateMenu(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 删除角色
     */
    @Log("删除角色")
    @DeleteMapping
    @PreAuthorize("@el.check('roles:del')")
    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids) {
        for (Long id : ids) {
            RoleDto role = roleService.findById(id);
            getLevels(role.getLevel());
        }
        // 验证是否被用户关联
        roleService.verification(ids);
        roleService.removeByIds(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 获取用户的角色级别
     * @return /
     */
    private int getLevels(Integer level){
        List<Integer> levels = roleService.findByUsersId(SecurityUtils.getCurrentUserId()).stream().map(RoleSmallDto::getLevel).collect(Collectors.toList());
        int min = Collections.min(levels);
        if(level != null){
            if(level < min){
                throw new BadRequestException("权限不足，你的角色级别：" + min + "，低于操作的角色级别：" + level);
            }
        }
        return min;
    }
}
