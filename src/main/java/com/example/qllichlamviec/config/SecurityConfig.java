package com.example.qllichlamviec.config;


import com.example.qllichlamviec.config.jwt.JwtAuthenticationTokenFilter;
import com.example.qllichlamviec.config.jwt.RestAuthenticationEntryPoint;
import com.example.qllichlamviec.config.jwt.UserAccessDeniedHandler;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.NameTokenizers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.example.qllichlamviec.config.modalmap.LocalDateConverter;
import com.example.qllichlamviec.config.modalmap.LocalDateTimeConverter;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
@Slf4j
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    //JWT
    @Value("${url.client}")
    private String urlClient;
    @Value("${environments.url.upload}")
    private String forderPath;

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() throws Exception {
        JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter = new JwtAuthenticationTokenFilter();
        return jwtAuthenticationTokenFilter;
    }

    @Bean
    public Path getPath() {
        return Paths.get(forderPath);
    }

    @Bean
    public RestAuthenticationEntryPoint restServicesEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }

    @Bean
    public UserAccessDeniedHandler userAccessDeniedHandler() {
        return new UserAccessDeniedHandler();
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

//    ****

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            // Tạo UserDetails từ cơ sở dữ liệu hoặc một nguồn khác
            // Đây là một ví dụ với user trong bộ nhớ
            if ("user".equals(username)) {
                return org.springframework.security.core.userdetails.User.withUsername("user")
                        .password("{noop}password") // {noop} để không mã hóa password
                        .roles("USER")
                        .build();
            } else {
                throw new UsernameNotFoundException("User not found");
            }
        };
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }

    //JWT
    protected void configure(final HttpSecurity http) throws Exception {
        http.csrf().ignoringAntMatchers("/ql-lich/**");
        http.cors().and()
                //config jwt
                .antMatcher("/ql-lich/**").httpBasic().authenticationEntryPoint(restServicesEntryPoint()).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
                .antMatchers(HttpMethod.POST, "/ql-lich/user/dang-nhap").permitAll()
                .antMatchers("/ql-lich/public/**"
                ).permitAll()
                .antMatchers("/**").access("hasRole('ROLE_USER')")
                .and()
                .addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().accessDeniedHandler(userAccessDeniedHandler());
        http
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .httpBasic();

    }

    //Encode
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    //Encode
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(urlClient));
        configuration.setAllowedMethods(Arrays.asList("HEAD",
                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type", "Access-Control-Allow-Headers", "Origin,Accept", "X-Requested-With", "Access-Control-Request-Method", "Access-Control-Request-Headers"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setSourceNameTokenizer(NameTokenizers.UNDERSCORE)
                .setDestinationNameTokenizer(NameTokenizers.UNDERSCORE);
        modelMapper.addConverter(new LocalDateConverter());
        modelMapper.addConverter(new LocalDateTimeConverter());
        return modelMapper;
    }

}
