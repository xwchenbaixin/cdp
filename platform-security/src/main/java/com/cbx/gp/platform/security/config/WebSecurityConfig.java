package com.cbx.gp.platform.security.config;

import com.cbx.gp.platform.security.handler.LoginFailureHandler;
import com.cbx.gp.platform.security.handler.LoginSuccessHandler;
import com.cbx.gp.platform.security.service.MyFilterSecurityInterceptor;
import com.cbx.gp.platform.security.service.MyInvocationSecurityMetadataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


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

  //密码生成策略
  @Bean
  public Md5PasswordEncoder passwordEncoder() {
    return new Md5PasswordEncoder();
  }


  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    System.out.println(new Md5PasswordEncoder().encode("cbx318"));
    auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder());
  }

  @Override
  public void configure(WebSecurity web) {
    //配置不需要拦截的url
    web.ignoring().antMatchers("/css/**", "/js/**","/orderManager/aliPlayCallback","/userManager/registUser");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
/*    http.authorizeRequests()
            // 文件夹验证
            //.antMatchers("/Img/**").access("hasRole('ROLE_TEACHER')")
            .anyRequest().authenticated()//任何请求，登录后可以访问
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
            .csrf().disable();*/

    /*基于 Session
     * http.sessionManagement()
            .maximumSessions(1)//最大session并发数量1
            //.sessionRegistry(getSessionRegistry())
            .maxSessionsPreventsLogin(false)//false之后登录踢掉之前登录,true则不允许之后登录
            //.expiredSessionStrategy(new MerryyounExpiredSessionStrategy())//登录被踢掉时的自定义操作
            .and()
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            .invalidSessionUrl("/login");//session失效以后跳转的连接
          */
    // 基于token，所以不需要session
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true); // 是否支持安全证书
    config.addAllowedOrigin("*"); // 设置允许当前域名使用
    config.addAllowedHeader("*"); // 允许任何头
    config.addAllowedMethod("*"); // 允许任何方法（post、get等）
    source.registerCorsConfiguration("/**", config);//配置所有接口都有效
    // 预检请求的有效期，单位为秒。
    //        config.setMaxAge(3600L);


    http.sessionManagement()
            .maximumSessions(1)//最大session并发数量1
            //.sessionRegistry(getSessionRegistry())
            .maxSessionsPreventsLogin(false)//false之后登录踢掉之前登录,true则不允许之后登录 .and()
            .and()
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            .and()
            .formLogin()
            //loginProcessingUrl用于指定前后端分离的时候调用后台登录接口的名称
            .loginProcessingUrl("/login")
            .successHandler(loginSuccessHandler)
            .failureHandler(loginFailureHandler)
            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.OPTIONS, "/**")
            .permitAll()
            // 对于获取token的rest api要允许匿名访问
//            .antMatchers("/login")
//            .permitAll()
            // 除上面外的所有请求全部需要鉴权认证
            .anyRequest()
            .authenticated()
            .and()
            .cors().configurationSource(source)
            .and()
            .csrf().disable();;

    http.addFilterBefore(myFilterSecurityInterceptor, FilterSecurityInterceptor.class);
    // 禁用缓存
    http.headers().cacheControl();
  }

  @Bean
  public FilterRegistrationBean registration(MyFilterSecurityInterceptor myFilterSecurityInterceptor) {
    FilterRegistrationBean registration = new FilterRegistrationBean(myFilterSecurityInterceptor);
    registration.setEnabled(false);
    return registration;
  }

}
