package com.blog.modules.tools.controller;

import com.blog.exception.BadRequestException;
import com.blog.modules.tools.domain.LocalStorage;
import com.blog.modules.tools.service.LocalStorageService;
import com.blog.modules.tools.service.dto.LocalStorageQueryParam;
import com.blog.utils.FileUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
* @author ty
*/
@RestController
@RequiredArgsConstructor
// @Api(tags = "工具：本地存储管理")
@RequestMapping("/api/localStorage")
public class LocalStorageController {

    private final LocalStorageService localStorageService;

    @GetMapping
    @PreAuthorize("@el.check('storage:list')")
    public ResponseEntity<Object> query(LocalStorageQueryParam criteria, Pageable pageable){
        return new ResponseEntity<>(localStorageService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    // @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('storage:list')")
    public void download(HttpServletResponse response, LocalStorageQueryParam criteria) throws IOException {
        localStorageService.download(localStorageService.queryAll(criteria), response);
    }

    // @ApiOperation("上传文件")
    @PostMapping
    @PreAuthorize("@el.check('storage:add')")
    public ResponseEntity<Object> create(@RequestParam String name, @RequestParam("file") MultipartFile file){
        localStorageService.create(name, file);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/pictures")
    // @ApiOperation("上传图片")
    public ResponseEntity<Object> upload(@RequestParam MultipartFile file){
        // 判断文件是否为图片
        String suffix = FileUtil.getExtensionName(file.getOriginalFilename());
        if(!FileUtil.IMAGE.equals(FileUtil.getFileType(suffix))){
            throw new BadRequestException("只能上传图片");
        }
        LocalStorage localStorage = localStorageService.create(null, file);
        return new ResponseEntity<>(localStorage, HttpStatus.OK);
    }

    // @ApiOperation("修改文件")
    @PutMapping
    @PreAuthorize("@el.check('storage:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody LocalStorage resources){
        localStorageService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    // @ApiOperation("多选删除")
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        localStorageService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
