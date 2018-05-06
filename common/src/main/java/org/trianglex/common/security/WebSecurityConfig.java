package org.trianglex.common.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static org.trianglex.common.constant.PropertiesConstant.SECURITY_PATH_PATTERN;

public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeRequests()
                .antMatchers(SECURITY_PATH_PATTERN)
                .authenticated()
                .and()
                .httpBasic();
    }

}
