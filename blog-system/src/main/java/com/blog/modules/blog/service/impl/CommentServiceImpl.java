package com.blog.modules.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.blog.modules.blog.entity.CommentEntity;
import com.blog.modules.blog.mapper.CommentMapper;
import com.blog.modules.blog.service.CommentService;

/**
 * (Comment)表服务实现类
 *
 * @author makejava
 * @since 2023-05-27 19:43:42
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, CommentEntity> implements CommentService {

}

