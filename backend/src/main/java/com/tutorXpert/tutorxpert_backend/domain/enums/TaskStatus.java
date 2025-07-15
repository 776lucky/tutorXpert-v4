package com.tutorXpert.tutorxpert_backend.domain.enums;

public enum TaskStatus {
    OPEN("待接受"),
    ASSIGNED("已指派"),
    IN_PROGRESS("进行中"),
    COMPLETED("已完成"),
    CANCELLED("已取消"),
    EXPIRED("已过期");

    private final String label;
    TaskStatus(String label) { this.label = label; }
    public String getLabel() { return label; }
}
