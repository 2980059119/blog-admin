package com.blog.modules.tools.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.modules.tools.domain.EmailConfig;
import com.blog.modules.tools.domain.vo.EmailVo;

/**
 * @author IKUN
 * @since 2023-05-31 21:25:43
 */
public interface EmailService extends IService<EmailConfig> {

    /**
     * 更新邮件配置
     *
     * @param emailConfig 邮箱配置
     * @param old         /
     * @return /
     * @throws Exception /
     */
    EmailConfig config(EmailConfig emailConfig, EmailConfig old) throws Exception;

    /**
     * 查询配置
     *
     * @return EmailConfig 邮件配置
     */
    EmailConfig find();

    /**
     * 发送邮件
     *
     * @param emailVo     邮件发送的内容
     * @param emailConfig 邮件配置
     * @throws Exception /
     */
    void send(EmailVo emailVo, EmailConfig emailConfig);
}
