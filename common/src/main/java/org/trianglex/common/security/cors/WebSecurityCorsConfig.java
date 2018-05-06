package org.trianglex.common.security.cors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.trianglex.common.constant.PropertiesConstant.SECURITY_PATH_PATTERN;

@Configuration
@Import(FrameworkCorsProperties.class)
public class WebSecurityCorsConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private FrameworkCorsProperties frameworkCorsProperties;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers(SECURITY_PATH_PATTERN)
                .authenticated()
                .and()
                .httpBasic();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(frameworkCorsProperties.getAllowedOrigins());
        configuration.setAllowedMethods(frameworkCorsProperties.getAllowedMethods());
        configuration.setAllowCredentials(frameworkCorsProperties.isAllowCredentials());
        configuration.setAllowedHeaders(frameworkCorsProperties.getAllowedHeaders());
        configuration.setExposedHeaders(frameworkCorsProperties.getExposedHeaders());
        configuration.setMaxAge(frameworkCorsProperties.getMaxAge().getSeconds());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(frameworkCorsProperties.getPath(), configuration);
        return source;
    }
}
