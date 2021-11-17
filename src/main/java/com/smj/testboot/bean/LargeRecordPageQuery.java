package com.smj.testboot.bean;


import lombok.Data;



@Data
public class LargeRecordPageQuery {

    private Integer startRow;
    private Integer pageSize;
    private Boolean enabled;

}
