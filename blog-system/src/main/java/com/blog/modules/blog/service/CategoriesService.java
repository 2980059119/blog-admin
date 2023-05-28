package com.blog.modules.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.modules.blog.entity.ArticleEntity;
import com.blog.modules.blog.entity.CategoriesEntity;
import com.blog.modules.blog.entity.dto.CategoriesDto;
import com.blog.modules.blog.entity.vo.CategoriesVo;


/**
 * (Categories)表服务接口
 *
 * @author IKUN
 * @since 2023-05-27 19:58:23
 */
public interface CategoriesService extends IService<CategoriesEntity> {
    /**
     * 通过文章标题查找文章
     * @param categoriesName 标题
     * @return 文章
     */
    CategoriesEntity getByCategoriesName(String categoriesName);
    /**
     * 新增数据
     *
     * @param categories 实体对象
     * @return 新增结果
     */
    boolean save(CategoriesDto categories);
    /**
     * 修改数据
     *
     * @param categories 实体对象
     * @return 修改结果
     */
    boolean updateById(CategoriesDto categories);
}
