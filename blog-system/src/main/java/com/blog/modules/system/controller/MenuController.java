package com.blog.modules.system.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.blog.exception.BadRequestException;
import com.blog.modules.logging.annotation.Log;
import com.blog.modules.system.domain.Menu;
import com.blog.modules.system.domain.dto.MenuDto;
import com.blog.modules.system.domain.dto.MenuQueryParam;
import com.blog.modules.system.mapper.MenuMapper;
import com.blog.modules.system.service.MenuService;
import com.blog.utils.ConvertUtil;
import com.blog.utils.PageUtil;
import com.blog.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 系统：菜单管理
 *
 * @author IKUN
 * @since 2023-05-31 21:25:43
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/menus")
public class MenuController {

    private static final String ENTITY_NAME = "menu";
    private final MenuService menuService;
    private final MenuMapper menuMapper;

    /**
     * 查询菜单
     */
    @GetMapping
    @PreAuthorize("@el.check('menu:list')")
    public ResponseEntity<Object> query(MenuQueryParam criteria) throws Exception {
        List<MenuDto> menuDtoList = menuService.queryAll(criteria, true);
        return new ResponseEntity<>(PageUtil.toPage(menuDtoList, menuDtoList.size()), HttpStatus.OK);
    }

    /**
     * 返回全部的菜单
     */
    @GetMapping(value = "/lazy")
    @PreAuthorize("@el.check('menu:list','roles:list')")
    public ResponseEntity<Object> query(@RequestParam Long pid) {
        return new ResponseEntity<>(menuService.getMenus(pid), HttpStatus.OK);
    }

    @GetMapping(value = "/build")
    /**获取前端所需菜单*/ public ResponseEntity<Object> buildMenus() {
        List<MenuDto> menuDtoList = menuService.findByUser(SecurityUtils.getCurrentUserId());
        List<MenuDto> menuDtos = menuService.buildTree(menuDtoList);
        return new ResponseEntity<>(menuService.buildMenus(menuDtos), HttpStatus.OK);
    }

    /**
     * 根据菜单ID返回所有子节点ID，包含自身ID
     */
    @GetMapping(value = "/child")
    @PreAuthorize("@el.check('menu:list','roles:list')")
    public ResponseEntity<Object> child(@RequestParam Long id) {
        Set<Menu> menuSet = new HashSet<>();
        List<MenuDto> menuList = menuService.getMenus(id);
        menuSet.add(menuService.getById(id));
        menuSet = menuService.getChildMenus(ConvertUtil.convertList(menuList, Menu.class), menuSet);
        Set<Long> ids = menuSet.stream().map(Menu::getId).collect(Collectors.toSet());
        return new ResponseEntity<>(ids, HttpStatus.OK);
    }

    /**
     * 查询菜单:根据ID获取同级与上级数据
     */
    @PostMapping("/superior")
    @PreAuthorize("@el.check('menu:list')")
    public ResponseEntity<Object> getSuperior(@RequestBody List<Long> ids) {
        Set<MenuDto> menuDtos = new LinkedHashSet<>();
        if (CollectionUtil.isNotEmpty(ids)) {
            for (Long id : ids) {
                MenuDto menuDto = menuService.findById(id);
                menuDtos.addAll(menuService.getSuperior(menuDto, new ArrayList<>()));
            }
            return new ResponseEntity<>(menuService.buildTree(new ArrayList<>(menuDtos)), HttpStatus.OK);
        }
        return new ResponseEntity<>(menuService.getMenus(null), HttpStatus.OK);
    }

    /**
     * 新增菜单
     */
    @Log("新增菜单")
    @PostMapping
    @PreAuthorize("@el.check('menu:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Menu resources) {
        if (resources.getId() != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        menuService.save(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 修改菜单
     */
    @Log("修改菜单")
    @PutMapping
    @PreAuthorize("@el.check('menu:edit')")
    public ResponseEntity<Object> update(@Validated(Menu.Update.class) @RequestBody Menu resources) {
        menuService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 删除菜单
     */
    @Log("删除菜单")
    @DeleteMapping
    @PreAuthorize("@el.check('menu:del')")
    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids) {
        Set<Menu> menuSet = new HashSet<>();
        for (Long id : ids) {
            List<MenuDto> menuList = menuService.getMenus(id);
            menuSet.add(menuService.getById(id));
            menuSet = menuService.getDeleteMenus(ConvertUtil.convertList(menuList, Menu.class), menuSet);
        }
        menuService.removeByIds(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 导出菜单数据
     */
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('menu:list')")
    public void download(HttpServletResponse response, MenuQueryParam criteria) throws Exception {
        menuService.download(menuService.queryAll(criteria, false), response);
    }

}
