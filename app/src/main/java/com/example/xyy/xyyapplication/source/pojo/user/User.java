package com.example.xyy.xyyapplication.source.pojo.user;

import lombok.Data;
import lombok.ToString;

/**
 * Created by admin on 16/4/26.
 */
@Data
@ToString
public class User {
    private Integer id;
    private String isDeleted;
    private Long gmtCreate;
    private Long gmtModified;
    private String userName;
    private String account;
    private String password;
    private String mobile;
    private String isAdmin;  //是否是管理员 1:管理员 0:非管理员
}
