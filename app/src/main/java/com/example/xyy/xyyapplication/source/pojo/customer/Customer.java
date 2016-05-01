package com.example.xyy.xyyapplication.source.pojo.customer;

import lombok.Data;
import lombok.ToString;

/**
 * Created by admin on 16/5/1.
 */
@Data
@ToString
public class Customer {
    private Integer id;
    private String isDeleted;
    private Long gmtCreate;
    private Long gmtModified;
    private String customerName;
    private String customerMobile;
    private String customerType;  //客户类别
}
