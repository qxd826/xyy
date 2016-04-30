package com.example.xyy.xyyapplication.source.pojo.userLogin;

import lombok.Data;
import lombok.ToString;

/**
 * Created by admin on 16/5/1.
 */
@Data
@ToString
public class UserLoginLog {
    private Integer id;
    private String isDeleted;
    private Long gmtCreate;
    private Long gmtModified;
    private String account;
    private String password;
}
