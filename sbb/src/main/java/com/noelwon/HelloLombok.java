package com.noelwon;

import lombok.Getter;
//import lombok.Setter;
import lombok.RequiredArgsConstructor;


//lombok의 경우 build.gradle에서 dependency 부분에 롬복을 추가해준다. 그럼 일반적으로 사용했던 getter, setter 메소드를 자동으로 만들어 준다.

@RequiredArgsConstructor
@Getter
//@Setter   fianl을 붙여줘서 setter로 속성값을 변경할 수 없기 때문에 주석을 잡아준다.
public class HelloLombok {
//	private String hello;
//	private int lombok;
	
	// final 로 만들면 속성값을 수정할 수 없다. 
	private final String hello;
	private final int lombok;

//	@RequiredArgsConstructor 롬복에서 생성자도 자동으로 생성된다.
//	public HelloLombok(String hello, int lombok) {
//		super();
//		this.hello = hello;
//		this.lombok = lombok;
//	}
	
	
//	롬복을 사용하면 아래와 같은 getter setter 부분이 자동으로 생성된다. 
//	public void setHello(String hello) {
//		this.hello = hello;
//	}
//	
//	public void setLombok(int lombok) {
//		this.lombok = lombok;
//	}
//	
//	public String getHello() {
//		return this.hello;
//	}
//	
//	public int getLombok() {
//		return this.lombok;
//	}
	

	public static void main(String[] args) {
//		HelloLombok helloLombok = new HelloLombok();
		HelloLombok helloLombok = new HelloLombok("헬로",5);
//      helloLombok.setHello("헬로");
//      helloLombok.setLombok(5);
      
      System.out.println(helloLombok.getHello());
      System.out.println(helloLombok.getLombok());
      
	}


}
