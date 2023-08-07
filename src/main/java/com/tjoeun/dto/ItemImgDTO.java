package com.tjoeun.dto;

import org.modelmapper.ModelMapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemImgDTO {
	
	private Long id;
	
	private String imgName;			// 이미지 파일명 : 프로젝트 내에서 UUID 로 저장되는 이름
	
	private String oriImgName;	// 원본 이미지 파일명
	
	private String imgUrl;			// 이미지 저장 경로
	
	private String repImgYn;		// 대표 사진 여부
	
	private static ModelMapper modelMapper = new ModelMapper();

	// ItemImgDTO 가 화면에서 받아온 data를 
	// Entity 클래스인 ItemImg 에 전달하는 메소드
	public static ItemImgDTO of(String ItemImg) {
		return modelMapper.map(ItemImg, ItemImgDTO.class); 
	}
}
