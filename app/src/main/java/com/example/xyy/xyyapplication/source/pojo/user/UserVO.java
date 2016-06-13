package com.example.xyy.xyyapplication.source.pojo.user;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by admin on 16/6/13.
 */
@Data
@EqualsAndHashCode
public class UserVO implements Serializable{
    private Integer id;
    private String isDeleted;
    private Long gmtCreate;
    private Long gmtModified;
    private String userName;//用户姓名
    private String account;//登录账户
    private String password;//登录密码
    private String isAdmin;//1是管理员,0是非管理员
    private String mobile;//用户电话
}
