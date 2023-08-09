package com.tjoeun.dto;

import com.tjoeun.constant.ItemSellStatus;

import lombok.Getter;
import lombok.Setter;
/*
 1 ) 상품등록일
 2 ) 판매 상태
 3 ) 상품명
 4 ) 등록자 아이디
*/
@Getter @Setter
public class ItemSearchDTO {
	private String searchDateType;
	private ItemSellStatus searchSellStatus;
	private String searchBy;
	private String searchQuery = "";
}
