package com.example.xyy.xyyapplication.source.pojo.supply;

import lombok.Data;
import lombok.ToString;

/**
 * Created by admin on 16/5/1.
 */
@Data
@ToString
public class Supply {
    private Integer id;
    private String isDeleted;
    private Long gmtCreate;
    private Long gmtModified;
    private String supplyName;
    private String supplyMobile;
    private String supplyType;  //供应上类别
}
