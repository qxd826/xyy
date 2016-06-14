package com.example.xyy.xyyapplication.source.pojo.goods;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by QXD on 2016/6/15.
 */
@Data
@EqualsAndHashCode
public class GoodsLogVO implements Serializable {
    private Long id;
    private String isDeleted;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer createId;
    private String goodsName;
    private Integer goodsNum;
    private String goodsCode;
    private Integer customerId;
    private String customerName;
    private Integer supplyId;
    private String supplyName;
    private String actionType;
}
