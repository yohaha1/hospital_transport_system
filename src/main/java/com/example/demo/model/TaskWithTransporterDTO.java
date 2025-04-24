package com.example.demo.model;

import lombok.Data;

@Data
public class TaskWithTransporterDTO {
    private TransportTask task;
    private String transporterName;
}
