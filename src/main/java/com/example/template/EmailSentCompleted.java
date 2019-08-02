package com.example.template;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailSentCompleted {

    private String type ;
    private String stateMessage = "이메일 전송 완료";
    private String customerName;
    private String Message;
	
}
