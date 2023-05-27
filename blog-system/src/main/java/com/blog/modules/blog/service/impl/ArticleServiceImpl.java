package com.blog.modules.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.blog.modules.blog.entity.ArticleEntity;
import com.blog.modules.blog.mapper.ArticleMapper;
import com.blog.modules.blog.service.ArticleService;

/**
 * (Article)表服务实现类
 *
 * @author IKUN
 * @since 2023-05-27 19:58:23
 */
@Service("articleService")
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, ArticleEntity> implements ArticleService {

}

