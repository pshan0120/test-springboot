package com.chandy.test.springboot.config.auth.dto;

import com.chandy.test.springboot.domain.user.Role;
import com.chandy.test.springboot.domain.user.User;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthAttributes {
  private Map<String, Object> attributes;
  private String nameAttributeKey;
  private String name;
  private String email;
  private String picture;

  @Builder
  public OAuthAttributes(
      Map<String, Object> attributes, String nameAttributeKey,
      String name, String email, String picture) {
    this.attributes = attributes;
    this.nameAttributeKey = nameAttributeKey;
    this.name = name;
    this.email = email;
    this.picture = picture;
  }
  // OAuth2User 리턴값은 Map이라서 개별 값을 변환해야 함
  public static OAuthAttributes of(String registrationId,
      String userNameAttribteName,
      Map<String, Object> attributes) {
    if("naver".equals(registrationId)) {
      return ofNaver("id", attributes);
    }

    return ofGoogle(userNameAttribteName, attributes);
  }

  public static OAuthAttributes ofGoogle(String userNameAttribteName,
      Map<String, Object> attributes) {
    return OAuthAttributes.builder()
        .name((String) attributes.get("name"))
        .email((String) attributes.get("email"))
        .picture((String) attributes.get("picture"))
        .attributes(attributes)
        .nameAttributeKey(userNameAttribteName)
        .build();
  }

  private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
    Map<String, Object> response = (Map<String, Object>) attributes.get("response");

    return OAuthAttributes.builder()
        .name((String) response.get("name"))
        .email((String) response.get("email"))
        .picture((String) response.get("profile_image"))
        .attributes(response)
        .nameAttributeKey(userNameAttributeName)
        .build();
  }

  // User 엔티티 생성
  // 생성 시점은 처음 가입할 때
  // 기본 권한을 GUEST로 주기 위해 role 사용
  public User toEntity() {
    return User.builder()
        .name(name)
        .email(email)
        .picture(picture)
        .role(Role.GUEST)
        .build();
  }

}
