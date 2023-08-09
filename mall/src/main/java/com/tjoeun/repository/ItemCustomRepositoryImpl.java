package com.tjoeun.repository;

import static com.tjoeun.entity.QItem.item;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tjoeun.constant.ItemSellStatus;
import com.tjoeun.dto.ItemSearchDTO;
import com.tjoeun.entity.Item;

public class ItemCustomRepositoryImpl implements ItemCustomRepository{

	private JPAQueryFactory jpaQueryFactory;
	
	public ItemCustomRepositoryImpl(EntityManager entityManager) {
		this.jpaQueryFactory = new JPAQueryFactory(entityManager);
	}
	
	@Override
	public Page<Item> getAdminItemPage(ItemSearchDTO itemSearchDTO, Pageable pageable) {
		List<Item> contentList = jpaQueryFactory.selectFrom(item)
																		 .where(regDtsAfter(itemSearchDTO.getSearchDateType()),
																				 		searchSellStatusEq(itemSearchDTO.getSearchSellStatus()),
																				 		searchByLike(itemSearchDTO.getSearchBy(), itemSearchDTO.getSearchQuery()))
																		 .orderBy(item.id.desc())
																		 .offset(pageable.getOffset())
																		 .limit(pageable.getPageSize())
																		 .fetch();
		Long total = jpaQueryFactory.select(Wildcard.count)
																.from(item)
																.where(regDtsAfter(itemSearchDTO.getSearchDateType()),
																	 		 searchSellStatusEq(itemSearchDTO.getSearchSellStatus()),
																	 		 searchByLike(itemSearchDTO.getSearchBy(), itemSearchDTO.getSearchQuery()))
																.fetchOne();
		
		return new PageImpl<Item>(contentList, pageable, total);
	}

	private BooleanExpression searchByLike(String searchBy, String searchQuery) {
		// itemName : 상품명
		// createdBy : 작성자
		if(StringUtils.equals("itemName", searchBy)) {
			return item.itemName.like("%"+ searchQuery +"%");
		} else if(StringUtils.equals("createdBy", searchBy)) {
			return item.createdBy.like("%"+ searchQuery +"%");
		}
		
		return null;
	}

	private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus) {
		return searchSellStatus == null ? null : item.itemSellStatus.eq(searchSellStatus);
	}

	private BooleanExpression regDtsAfter(String searchDateType) {
		LocalDateTime dateTime = LocalDateTime.now();
		if(StringUtils.equals("all", searchDateType) || searchDateType == null) {
			return null;
		} else if(StringUtils.equals("1d", searchDateType)) {
			dateTime = dateTime.minusDays(1);
		} else if(StringUtils.equals("1w", searchDateType)) {
			dateTime = dateTime.minusWeeks(1);
		} else if(StringUtils.equals("1m", searchDateType)) {
			dateTime = dateTime.minusMonths(1);
		} else if(StringUtils.equals("6m", searchDateType)) {
			dateTime = dateTime.minusMonths(6);
		}
		return item.regTime.after(dateTime);
	}

}
