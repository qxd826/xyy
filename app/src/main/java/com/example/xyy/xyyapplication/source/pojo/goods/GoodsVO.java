package com.example.xyy.xyyapplication.source.pojo.goods;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by QXD on 2016/6/14.
 */
@Data
@EqualsAndHashCode
public class GoodsVO implements Serializable{
    private Long id;
    private String isDeleted;
    private Long gmtCreate;
    private Long gmtModified;
    private String goodsName;
    private Integer goodsNum;
    private String goodsCode;
}
