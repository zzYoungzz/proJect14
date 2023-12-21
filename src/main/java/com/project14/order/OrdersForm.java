package com.project14.order;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrdersForm {
	@NotNull(message = "수량 및 가격을 확인해주세요.")
	private Integer count;
	

	@NotNull(message = "수량 및 가격을 확인해주세요.")
	private Integer price;
	

	@NotEmpty(message = "주소는 필수항목 입니다.")
	private String address;
	
	@NotEmpty(message = "우편번호는 필수항목 입니다.")
	private String postcode;
	
	@NotEmpty(message = "상세주소는 필수항목 입니다.")
	private String address_detail;
	
	@NotEmpty(message = "배송받으실 분의 성함을 꼭 적어주세요.")
	private String shipping_name;
	
}
