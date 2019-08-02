package com.example.template;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailSentCompleted {

    private String type ;
    private String stateMessage = "이메일 전송 완료. 수량이 변경되었습니다.";
    private String customerName;
    private String Message;
	
}
