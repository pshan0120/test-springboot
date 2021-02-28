package com.chandy.test.springboot.config.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 이 어노테이션이 생성될 수 있는 위치 지정
// PARAMETER라서 메소드의 파라미터로 선언한 객체에서만 사용 가능
// 이외에도 클래스 선언문에 쓸 수 있는 TYPE 등 있음
@Target(ElementType.PARAMETER)
// 어노테이션 유지기간?
@Retention(RetentionPolicy.RUNTIME)
// 이 파일을 어노테이션 클래스로 지정, &LoginUser라는게 생긴 것임
public @interface LoginUser {

}
