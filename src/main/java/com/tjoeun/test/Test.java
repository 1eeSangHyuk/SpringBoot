package com.tjoeun.test;

public class Test {
	
	public static void main(String[] args) {
		
		Student s1 = Student.builder()
		                    .name("더조은")
		                    .height(179)
		                    .build();		
		System.out.println("s1 : " + s1);
		// Student s = new Student("더조은", 179);
		
		Student s2 = Student.builder()
                				.name("아이티")
                				.build();
		System.out.println("s2 : " + s2);
	}

}






