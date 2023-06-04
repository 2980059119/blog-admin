package com.blog.modules.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.modules.blog.entity.CommentEntity;
import com.blog.modules.blog.mapper.CommentMapper;
import com.blog.modules.blog.service.CommentService;
import org.springframework.stereotype.Service;

/**
 * (Comment)表服务实现类
 *
 * @author IKUN
 * @since 2023-05-27 19:58:24
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, CommentEntity> implements CommentService {

}

