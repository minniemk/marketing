package com.example.template;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductModified {
	
    private String type;
    private String stateMessage = "상품의 수량이 변경되었습니다.";
    
    private Long id;
    private String imgUrl;
    private String name;
    private int price;
    private int Stock;
    

}
