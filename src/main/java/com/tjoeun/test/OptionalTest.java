package com.tjoeun.test;

import java.util.Optional;

public class OptionalTest {

	public static void main(String[] args) {
		String str1 = "spring";
		
		// Optional 객체 생성
		Optional<String> opt1 = Optional.of(str1);
		Optional<String> opt2 = Optional.of("아카데미");
		
		// NullPointerException 발생
//		Optional<String> opt3 = Optional.of(null);
//		System.out.println("opt3 : "+opt3);
		
		// Optional 객체 생성 - Null 가능성 있는 경우 
		//	- Returns an Optional describing the given value, if non-null, otherwise returns an empty Optional.
		// 권장
		Optional<String> opt0 = Optional.ofNullable("학원");
		Optional<String> opt4 = Optional.ofNullable(null);
		// 권장하지 않음
		Optional<String> opt5 = null;
		Optional<String> opt6 = Optional.<String>empty();
		Optional<String> opt7 = Optional.empty();
		
		System.out.println("opt1 : "+opt1);
		System.out.println("opt2 : "+opt2);
		System.out.println("opt0 : "+opt0);
		System.out.println("opt4 : "+opt4);
		System.out.println("opt4 : "+opt4.getClass().getName());		
		
		// 값 가져오기
		String str4 = opt2.get();
		System.out.println("str4 : "+str4);
		
		// 값 가져오기 - Null 가능성 있는경우 
		// 	- If a value is present, returns the value, otherwise returns other.
		String str5 = opt2.orElse("Null이면 이 문장을 반환");
		System.out.println("str5 : "+str5);
		String str6 = opt4.orElse("Null이면 이 문장을 반환");
		System.out.println("str6 : "+str6);
		// 값 가져오기 - Null 가능성 있는경우 
		//	- If a value is present, returns the value, otherwise returns the result produced by the supplying function.
		String str8 = opt2.orElseGet(() -> new String("Null이면 이 문장을 반환"));
		System.out.println("str8 : "+str8);
		String str7 = opt4.orElseGet(() -> new String("Null이면 이 문장을 반환"));
		System.out.println("str7 : "+str7);
		String str12 = opt4.orElseGet(() -> new String(""));
		System.out.println("str12 : "+str12);
		String str11 = opt4.orElseGet(String::new);	// 람다식 축약
		System.out.println("str11 : "+str11);
		
		Optional<String> opt9 = Optional.ofNullable(null);
		String str9 = opt9.orElseGet(() -> new String("Null이면 이 문장을 반환"));
		System.out.println("str9 : "+str9);
		
		// Optional.ofNullable(str13) 이 null 이 아닌 경우에만 출력
		String str13 = "asdf";
		if(Optional.ofNullable(str13).isPresent()) {
			System.out.println("str13 : "+str13);
		}
		Optional.ofNullable(str13).ifPresent(System.out::println);
		if(Optional.ofNullable(null).isPresent()) {
			System.out.println("str13 : "+str13);
		}
		Optional.ofNullable(null).ifPresent(System.out::println);
		
		/*
		Optional<String> opt10 = Optional.ofNullable(null);
		String str10 = opt10.orElseThrow(() -> new NullPointerException());
		System.out.println("str10 : "+str10);
		*/
		
		int number1 = Optional.of("1234")
							  .filter(strNum -> strNum.length() > 0)
							  .map(Integer::parseInt)
							  .get();
		
		int number2 = Optional.of("")
				  .filter(strNum -> strNum.length() > 0)
				  .map(Integer::parseInt)
				  .orElse(-1);
		
		Optional.of("9876")
				.map(Integer::parseInt)
				.ifPresent(num -> System.out.println("num : "+num));
		
		// 길이가 0 인 배열 <-- item 이 하나도 없는 배열
		// 권장
		Object[] objArr1 = new Object[0];
		// 권장하지 않음
		Object[] objArr2 = null;
		
		// 권장
		String str2 = "";
		// 권장하지 않음
		String str3 = null;
	}

}

