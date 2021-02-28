package com.chandy.test.springboot.config.auth;

import com.chandy.test.springboot.config.auth.dto.SessionUser;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/* HandlerMethodArgumentResolver 인터페이스는 조건에 맞는 메소드가 있으면 구현체가 지정한 값으로
해당 메소드의 파라미터로 넘길 수 있음 */
@RequiredArgsConstructor
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

  private final HttpSession httpSession;

  // 컨트롤러 메소드가 특정 파라미터를 지원하는 판단
  // 이 경우 파라미터에 @LoginUser 어노테이션이 붙어 있고 클래스타입이 SessionUser.class이면 true
  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    boolean isLoginUserAnnotation = parameter.getParameterAnnotation(LoginUser.class) != null;
    boolean isUserClass = SessionUser.class.equals(parameter.getParameterType());
    return isLoginUserAnnotation && isUserClass;
  }

  // 파라미터에 전달할 객체 생성. 여기서는 세션에서 객체를 가져옴
  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    return httpSession.getAttribute("user");
  }

}
