package com.example.xyy.xyyapplication.source.pojo.goods;

import lombok.Data;
import lombok.ToString;

/**
 * Created by admin on 16/5/1.
 */
@Data
@ToString
public class Goods {
    private Integer id;
    private String isDeleted;
    private Long gmtCreate;
    private Long gmtModified;
    private String goodsName;
    private Integer goodsNum;
    private String goodsCode;
    private String goodsType;
}
