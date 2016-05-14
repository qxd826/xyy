package com.example.xyy.xyyapplication.source.pojo.goods;

import lombok.Data;
import lombok.ToString;

/**
 * Created by QXD on 2016/5/14.
 */
@Data
@ToString
public class GoodsLog{
    private Integer id;
    private String isDeleted;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer createId;
    private String goodsName;
    private Integer num;
    private String goodsCode;
    private Integer customerId;
    private String customerName;
    private Integer supplyId;
    private String supplyName;
    private String actionType;
}
