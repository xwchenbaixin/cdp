package com.cbx.gp.platform.security.config;

import com.cbx.gp.platform.security.handler.LoginFailureHandler;
import com.cbx.gp.platform.security.handler.LoginSuccessHandler;
import com.cbx.gp.platform.security.service.MyFilterSecurityInterceptor;
import com.cbx.gp.platform.security.service.MyInvocationSecurityMetadataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService myUserDetailsService;
	
	@Autowired
    private MyFilterSecurityInterceptor myFilterSecurityInterceptor;

	@Autowired
	private LoginSuccessHandler loginSuccessHandler;

	@Autowired
	private LoginFailureHandler loginFailureHandler;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//System.out.println("加密后:"+new BCryptPasswordEncoder().encode("cbx318"));
		auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder());
	}
	
	@Override
    public void configure(WebSecurity web) {
		//主要是配置URL
        web.ignoring().antMatchers("/css/**","/js/**");
    }

	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http.authorizeRequests()
			.anyRequest().authenticated()//任何请求，登录后可以访问
             // 文件夹验证
            .antMatchers("/TAECHER/**").access("hasRole('ROLE_TEACHER')")
			.and()
			.formLogin()
			.loginPage("/login")//登录界面的URL
			//.loginProcessingUrl("/")//登陆界面发起登陆请求的URL
			.failureUrl("/login?error")
			.permitAll()//登录页面用户任意访问
			.successHandler(loginSuccessHandler)
			//.failureHandler(loginFailureHandler)
			.and()
			.logout()
			.logoutSuccessUrl("/login")  //退出登录后的默认url是"/login"
			.permitAll()//注销行为任意访问
			.and()
			.csrf().disable();
		
		http.sessionManagement()
			.maximumSessions(1)//最大session并发数量1
			//.sessionRegistry(getSessionRegistry())
			.maxSessionsPreventsLogin(false)//false之后登录踢掉之前登录,true则不允许之后登录
			//.expiredSessionStrategy(new MerryyounExpiredSessionStrategy())//登录被踢掉时的自定义操作
			.and()
			.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
			.invalidSessionUrl("/login");//session失效以后跳转的连接
		http.addFilterBefore(myFilterSecurityInterceptor, FilterSecurityInterceptor.class);
		
	}

	@Bean
	public FilterRegistrationBean registration(MyFilterSecurityInterceptor myFilterSecurityInterceptor) {
		FilterRegistrationBean registration = new FilterRegistrationBean(myFilterSecurityInterceptor);
		registration.setEnabled(false);
		return registration;
	}
}
