package com.blog.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.modules.system.domain.dto.RoleDto;
import com.blog.modules.system.domain.dto.RoleQueryParam;
import com.blog.base.PageInfo;
import com.blog.modules.system.domain.Role;
import com.blog.modules.system.domain.dto.RoleSmallDto;
import com.blog.modules.system.domain.dto.UserDto;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
* @author ty
*/
public interface RoleService  extends IService<Role> {

    /**
    * 查询数据分页
    * @param query 条件
    * @param pageable 分页参数
    * @return PageInfo<RoleDto>
    */
    PageInfo<RoleDto> queryAll(RoleQueryParam query, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param query 条件参数
    * @return List<RoleDto>
    */
    List<RoleDto> queryAll(RoleQueryParam query);
    List<RoleDto> queryAll();

    Role getById(Long id);
    RoleDto findById(Long id);

    /**
     * 插入一条新数据。
     */
    boolean save(RoleDto resources);
    boolean updateById(RoleDto resources);
    boolean removeById(Long id);
    boolean removeByIds(Set<Long> ids);

    /**
     * 根据用户ID查询
     * @param id 用户ID
     * @return /
     */
    List<RoleSmallDto> findByUsersId(Long id);

    /**
     * 根据角色查询角色级别
     * @param roles /
     * @return /
     */
    Integer findByRoles(Set<Long> roleIds);

    /**
     * 修改绑定的菜单
     * @param resources /
     */
    void updateMenu(RoleDto resources);

    /**
     * 获取用户权限信息
     * @param user 用户信息
     * @return 权限信息
     */
    List<GrantedAuthority> mapToGrantedAuthorities(UserDto user);

    /**
     * 验证是否被用户关联
     * @param ids /
     */
    void verification(Set<Long> ids);

    /**
     * 根据菜单Id查询
     * @param menuIds /
     * @return /
     */
    List<Role> findInMenuId(List<Long> menuIds);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<RoleDto> all, HttpServletResponse response) throws IOException;
}
