package com.tjoeun.dto;

import java.time.LocalDateTime;

import com.tjoeun.constant.ItemSellStatus;

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
public class ItemDTO {
	private Long id;
	private String itemNm;
	private int price;
	private int stockNumber;
	private String itemDetail;
	private String itemSellStatus;
  private LocalDateTime regTime;
  private LocalDateTime updateTime;
}
