package com.blog.modules.logging.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ty
 */
@Data
public class LogSmallDTO implements Serializable {

    private String description;

    private String requestIp;

    private Long time;

    private String address;

    private String browser;

    private Date createTime;
}