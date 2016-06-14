package com.example.xyy.xyyapplication.source.pojo.goods;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by QXD on 2016/6/14.
 */
@Data
@EqualsAndHashCode
public class GoodsInOrOut implements Serializable{
    private String goodsCode;
    private Integer goodsNum;
    private int type;  //入库0出库1
    private Long supplyId;//供应商id
    private Long customerId;//客户id
}
