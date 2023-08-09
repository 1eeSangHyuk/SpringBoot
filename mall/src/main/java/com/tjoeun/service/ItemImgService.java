package com.tjoeun.service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.tjoeun.entity.ItemImg;
import com.tjoeun.repository.ItemImgRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemImgService {
	
	@Value("${itemImgLocation}")
	private String itemImgLocation;
	
	private final ItemImgRepository itemImgRepository;
	
	private final FileService fileService;
	
	public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
		
		String oriImgName = itemImgFile.getOriginalFilename();
		String imgName = "";
		String imgUrl = "";
		
		if(!StringUtils.isEmpty(oriImgName)) {
			imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
			imgUrl = "/images/item/" + imgName;
		}
		
		// 이미지를 변경하는 경우, 상품 이미지 정보 저장
		itemImg.updateItemImg(imgName, oriImgName, imgUrl);
		
		// 실제 DB 에 저장함
		itemImgRepository.save(itemImg);
	}
	
	public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception{
		// 이미지 있는지 확인
		if(!itemImgFile.isEmpty()) {
			// DB 에서 itemImgId 에 헤당하는 이미지 가져오기
			ItemImg savedItemImg = itemImgRepository.findById(itemImgId)
																				 			.orElseThrow(EntityNotFoundException::new);
			
			// 기존 이미지 파일 삭제 - imgName 이 있는 것들만 삭제함
			if(!StringUtils.isEmpty(savedItemImg.getImgName())) {
				fileService.deleteFile(itemImgLocation + "/" + savedItemImg.getImgName());
			}
			
			// 수정된 이미지 저장
			String oriImgName = itemImgFile.getOriginalFilename();
			
			// project(server) 에 저장된 이름에는 UUID 가 적용되어 있음
			// 	ㄴ 외부에서 접근할 때 경로로 사용할 논리적 경로로 지정함
			String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
			
			// 현재 project 안에서의 logical path(논리적 경로)
			String imgUrl = "/images/item/" + imgName;
			
			savedItemImg.updateItemImg(imgName, oriImgName, imgUrl);
		}
	}
}
