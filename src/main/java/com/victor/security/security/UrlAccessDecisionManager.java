package com.victor.security.security;

import java.util.Collection;
import java.util.Iterator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Param:
 * @return:
 * @Author: fhz
 * @Date: 10:40 2018-12-27
 */
@Slf4j
@Component
public class UrlAccessDecisionManager implements AccessDecisionManager {

  @Override
  public void decide(Authentication authentication, Object o,
      Collection<ConfigAttribute> collection)
      throws AccessDeniedException, InsufficientAuthenticationException {
    Iterator<ConfigAttribute> iterator = collection.iterator();
    while (iterator.hasNext()) {
      ConfigAttribute configAttribute = iterator.next();
      String needRole = configAttribute.getAttribute();
      if ("ROLE_LOGIN".equals(needRole)) {
        if (authentication instanceof AnonymousAuthenticationToken) {
          throw new BadCredentialsException("未登录");
        } else {
          return;
        }
      }
      //当前用户所具有的权限
      Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
      for (GrantedAuthority authority : authorities) {
        if (authority.getAuthority().equals(needRole)) {
          return;
        }
      }
    }
    throw new AccessDeniedException("权限不足!");
  }

  @Override
  public boolean supports(ConfigAttribute configAttribute) {
    return false;
  }

  @Override
  public boolean supports(Class<?> aClass) {
    return false;
  }
}
