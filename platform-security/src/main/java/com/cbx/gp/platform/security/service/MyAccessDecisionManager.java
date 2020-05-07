package com.cbx.gp.platform.security.service;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Iterator;


@Service("myAccessDecisionManager")
public class MyAccessDecisionManager implements AccessDecisionManager {

    // decide 方法是判定是否拥有权限的决策方法，
    // authentication 是MyUserDetailsService中循环添加到 GrantedAuthority 对象中的权限信息集合.
    // object 包含客户端发起的请求的requset信息，可转换为 HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
    // configAttributes 为MyInvocationSecurityMetadataSource的getAttributes(Object object)这个方法返回的结果，此方法是为了判定用户请求的url 是否在权限表中，如果在权限表中，则返回给 decide 方法，用来判定用户是否有此权限。如果不在权限表中则放行。
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {

        if(null== configAttributes || configAttributes.size() <=0) {
            return;
        }
        ConfigAttribute c;
        
        String needRole;
        //遍历此URL包含的权限
        for(Iterator<ConfigAttribute> iter = configAttributes.iterator(); iter.hasNext(); ) {
            c = iter.next();
            //获取角色名字
            needRole = c.getAttribute();
            //authentication 是在 UserPermissionService 存放的权限集合，权限根据Role来获取。存放的是RoleName
            for(GrantedAuthority ga : authentication.getAuthorities()) {
            	//如果请求的URL中包含该用户的权限，则继续处理剩下的请求。
                if(needRole.trim().equals(ga.getAuthority())) {
                    return;
                }
            }
        }
        //遍历完所有的的权限以后，又没包含这个权限。
        System.out.println("no Right");
        throw new AccessDeniedException("No Right");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}