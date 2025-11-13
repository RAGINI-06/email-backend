package com.email.writer.app;



import  lombok.Data;
@Data   // getter settter and constructor

public class EmailRequest {
    private  String   emailContent;
    private String tone;
}
