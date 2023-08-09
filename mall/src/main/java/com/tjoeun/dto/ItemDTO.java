package com.tjoeun.dto;

import com.tjoeun.entity.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDTO extends BaseEntity{
	private Long id;
	private String itemName;
	private int price;
	private int stockNumber;
	private String itemDetail;
	private String itemSellStatus;
}
