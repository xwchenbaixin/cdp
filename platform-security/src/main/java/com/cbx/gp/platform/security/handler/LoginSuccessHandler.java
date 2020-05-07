package com.cbx.gp.platform.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler{
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		super.onAuthenticationSuccess(request,response,authentication);
		/*User user=(User) authentication.getPrincipal();
		String targetUrl="/"+user.getRoleName().toLowerCase()+"/turnPage?pn=";
		switch (user.getRoleId()) {
		case 1:
			targetUrl+=Constants.STUDENT_INDEX;
			break;
		case 2:
			targetUrl+=Constants.TEACHER_INDEX;
			break;
		case 3:
			targetUrl+=Constants.DEAN_INDEX;
			break;

		default:
			break;
		}
		//登录成功后跳转至角色对应的首界面
		//System.err.println(targetUrl);
		System.out.println(targetUrl);
		response.sendRedirect(targetUrl);
		//super.onAuthenticationSuccess(request, response, authentication);*/
	}

}
