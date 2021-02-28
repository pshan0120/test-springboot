package com.chandy.test.springboot.config.auth;

import com.chandy.test.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity // Spring Security 설정 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final CustomOAuth2UserService customOAuth2UserService;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .csrf().disable().headers().frameOptions().disable()  // h2-console 화면 사용을 위함
    .and()
      .authorizeRequests()  // 권한별 관리 옵션, authorizeRequests가 선언되어야 antMatchers 사용 가능
      // 권한 관리 대상 지정 옵션, URl 및 HTTP메소드별로 관리 가능
      // 지정된 URl은 전체 열람 권한
      .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll()
      // api 주소 호출은 USER 권한만 가능
      .antMatchers("/api/v1/**").hasRole(Role.USER.name())
      // 설정값 이외 나머지 URL은 authenticated, 즉 로그인된 사용자만 가능
      .anyRequest().authenticated()
    .and()
      // Oauth2 로그아웃 설정
      .logout()
        // 로그아웃시 이동할 URL
        .logoutSuccessUrl("/")
    .and()
      // Oauth2 로그인 설정
      .oauth2Login()
        // Oauth2 로그인 성공 후 사용자 정보 가져올 때 설정 담당
        .userInfoEndpoint()
          // 로그인 성공 후 후처리 인터페이스 구현체 등록. 사용자 정보를 가져오고 나서 추가 진행될 기능 명시
          .userService(customOAuth2UserService);
  }

}
