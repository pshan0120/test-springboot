package com.chandy.test.springboot.config.auth;

import com.chandy.test.springboot.config.auth.dto.OAuthAttributes;
import com.chandy.test.springboot.config.auth.dto.SessionUser;
import com.chandy.test.springboot.domain.user.User;
import com.chandy.test.springboot.domain.user.UserRepository;
import java.util.Collections;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
  
  private final UserRepository userRepository;
  private final HttpSession httpSession;
  
  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
    OAuth2User oAuth2User = delegate.loadUser(userRequest);
    
    // 로그인 진행중 서비스 구분 코드(다른 서비스도 쓸 수 있기 때문에)
    String registrationId = userRequest.getClientRegistration().getRegistrationId();
    // 키가 되는 필드값. Primary Key와 같은 의미
    String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
        .getUserInfoEndpoint().getUserNameAttributeName();

    // OAuth2UserService에서 가져온 OAuth2User의 attribute를 담는 클래스
    // 다른 소셜 로그인에서도 사용
    OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName,
        oAuth2User.getAttributes());
    
    User user = saveOrUpdate(attributes);

    // SessionUser는 User클래스로 쓰지 않고 따로 만듬
    httpSession.setAttribute("user", new SessionUser(user));

    return new DefaultOAuth2User(
        Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
        attributes.getAttributes(),
        attributes.getNameAttributeKey()
    );
  }

  private User saveOrUpdate(OAuthAttributes attributes) {
    User user = userRepository.findByEmail(attributes.getEmail())
        .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
        .orElse(attributes.toEntity());

    return userRepository.save(user);
  }
  

}
