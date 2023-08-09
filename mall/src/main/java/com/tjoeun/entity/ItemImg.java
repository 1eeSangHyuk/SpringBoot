package com.tjoeun.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ItemImg {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "item_img_id")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;
	
	private String imgName;			// 이미지 파일명 : 프로젝트 내에서 UUID 로 저장되는 이름
	private String oriImgName;	// 원본 이미지 파일명
	private String imgUrl;			// 이미지 저장 경로
	private String repImgYn;		// 대표 이미지 여부
	
	// 이미지 변경할 경우
	public void updateItemImg(String imgName, String oriImgName, String imgUrl) {
		this.imgName = imgName;
		this.oriImgName = oriImgName;
		this.imgUrl = imgUrl;
	}
}