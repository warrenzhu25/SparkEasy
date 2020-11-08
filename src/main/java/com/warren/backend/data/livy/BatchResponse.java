package com.warren.backend.data.livy;

import lombok.Data;

@Data
public class BatchResponse {
    private long id;
    private String name;
    private String state;
    private String appId;
    private String[] log;
    private String[] exceptions;
}
