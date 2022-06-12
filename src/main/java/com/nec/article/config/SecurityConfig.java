package com.nec.article.config;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;


@Configuration
@EnableWebSecurity
@Order(SecurityProperties.BASIC_AUTH_ORDER)
public class SecurityConfig extends WebSecurityConfigurerAdapter {



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
            .antMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                .antMatchers("/article/**").hasRole("USER")
                .antMatchers("/article/create").hasRole("ADMIN")
                .antMatchers("/article/approve", "/article/getPending/**").hasRole("SUPER_ADMIN")
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
http.cors();
    }


    @Bean
    @Override
    public UserDetailsService userDetailsService() {

        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        final User.UserBuilder userBuilder = User.builder().passwordEncoder(encoder::encode);
        UserDetails user = userBuilder
                .username("user")
                .password("user")
                .roles("USER")
                .build();

        UserDetails admin = userBuilder
                .username("admin")
                .password("admin")
                .roles("USER","ADMIN")
                .build();

        UserDetails superAdmin = userBuilder
                .username("super_admin")
                .password("super_admin")
                .roles("USER","ADMIN", "SUPER_ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin, superAdmin);
    }
    /*@Bean
    @Override
    protected UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        UserDetails user1 = User
                .withUsername("admin")
                .authorities("ADMIN", "STAFF_MEMBER")
                .passwordEncoder(passwordEncoder()::encode)
                .password("admin")
                .build();
        manager.createUser(user1);

        UserDetails user2 = User
                .withUsername("super_admin")
                .authorities("SUPER_ADMIN", "ADMIN", "STAFF_MEMBER")
                .passwordEncoder(passwordEncoder()::encode)
                .password("1234")
                .build();
        manager.createUser(user2);

        UserDetails user3 = User
                .withUsername("reader")
                .authorities("READER")
                .passwordEncoder(passwordEncoder()::encode)
                .password("reader")
                .build();
        manager.createUser(user3);


        return manager;
    }*/


}

