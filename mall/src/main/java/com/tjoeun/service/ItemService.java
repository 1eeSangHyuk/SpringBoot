package com.tjoeun.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tjoeun.dto.ItemFormDTO;
import com.tjoeun.dto.ItemImgDTO;
import com.tjoeun.dto.ItemSearchDTO;
import com.tjoeun.entity.Item;
import com.tjoeun.entity.ItemImg;
import com.tjoeun.repository.ItemImgRepository;
import com.tjoeun.repository.ItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
	
	private final ItemRepository itemRepository;
	private final ItemImgRepository itemImgRepository;
	private final ItemImgService itemImgService;
	
	// 상품 등록
	public Long saveItem(ItemFormDTO itemFormDTO, List<MultipartFile> itemImgFileList) throws Exception {
		
		// ItemFormDTO 가 받은 값을 Item Entity 에 저장하기
		Item item = itemFormDTO.createItem();
		
		// DB 저장
		itemRepository.save(item);
		
		// 이미지 등록 : 저장한 이미지 개수만큼 등록
		for (int i=0; i<itemImgFileList.size(); i++) {
			ItemImg itemImg = new ItemImg();
			
			itemImg.setItem(item);
			
			if(i==0) {
				itemImg.setRepImgYn("Y");
			} else {
				itemImg.setRepImgYn("N");
			}
			
			// 이미지를 DB 에 저장함
			itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
		}
		
		return item.getId();
	}
	
	// 상품 수정 - 헤당 itemFormDTO 받아오기
	public ItemFormDTO getItemFormDTO(Long itemId) {
		// DB 에서 헤당 itemId 의 Item Entity 가져오기
		Item item = itemRepository.findById(itemId)
															.orElseThrow(EntityNotFoundException::new);
		
		// DB 에서 헤당 itemId 의 ItemImgList 가져오기
		List<ItemImg> itemImgList = itemImgRepository.findByItemId(itemId);
		// ItemImg(Entity) -> itemImgDTO
		List<ItemImgDTO> itemImgDTOList = new ArrayList<>();
		for(ItemImg itemImg : itemImgList) {
			ItemImgDTO itemImgDTO = ItemImgDTO.of(itemImg);
			itemImgDTOList.add(itemImgDTO);
		}
		
		// Item(Entity) -> ItemFormDTO
		ItemFormDTO itemFormDTO = ItemFormDTO.of(item);
		
		itemFormDTO.setItemImgDTOList(itemImgDTOList);
		
		return itemFormDTO;
	}
	
	// 상품 수정
	public Long updateItem(ItemFormDTO itemFormDTO, List<MultipartFile> itemImgFileList) throws Exception {
			// 수정 대상인 item(Entity) 를 DB에서 가져오기
			Item item = itemRepository.findById(itemFormDTO.getId())
																.orElseThrow(EntityNotFoundException::new);
			
			// item(Entity) 업데이트하기
			item.updateItem(itemFormDTO);
			
			// 상품 이미지 업데이트하기
			List<Long> itemImgIds = itemFormDTO.getItemImgIds();
			for(int i = 0; i < itemImgIds.size(); i++) {
				itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
			}
			
		return item.getId();
	}
	
	// 상품 가져오기
	public Page<Item> getAdminItemPage(ItemSearchDTO itemSearchDTO, Pageable pageable){
		return itemRepository.getAdminItemPage(itemSearchDTO, pageable);
	}
}
