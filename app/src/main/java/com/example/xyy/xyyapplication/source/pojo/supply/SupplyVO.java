package com.example.xyy.xyyapplication.source.pojo.supply;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created by QXD on 2016/6/15.
 */
@Data
@ToString
@EqualsAndHashCode
public class SupplyVO implements Serializable {
    private Long id;
    private String isDeleted;
    private Long gmtCreate;
    private Long gmtModified;
    private String supplyName;
    private String supplyMobile;
}
