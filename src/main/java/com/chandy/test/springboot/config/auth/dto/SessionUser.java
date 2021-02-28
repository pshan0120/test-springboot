package com.chandy.test.springboot.config.auth.dto;

import com.chandy.test.springboot.domain.user.User;
import java.io.Serializable;
import lombok.Getter;

@Getter
/* 세션 저장을 위해서는 직렬화 구현이 필요한데 User 클래스에서 하면 엔티티이기 때문에
  자식 엔티티까지 포함하고 있다면 성능 이슈나 부수 효과가 발생할 수 있음
  따라서 Dto를 추가로 만드는 것이 유지보수에 유리
 */
public class SessionUser implements Serializable {
  private String name;
  private String email;
  private String picture;

  public SessionUser(User user) {
    this.name = user.getName();
    this.email = user.getEmail();
    this.picture = user.getPicture();
  }

}
