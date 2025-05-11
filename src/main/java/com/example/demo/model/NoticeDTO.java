package com.example.demo.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class NoticeDTO {

    private int taskId;

    private String itemName;

    private Integer departmentId;

    private String transName;

    private String type;

}
