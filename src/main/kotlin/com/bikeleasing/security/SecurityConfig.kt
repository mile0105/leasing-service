package com.bikeleasing.security

import com.bikeleasing.security.jwt.JwtAuthenticationEntryPoint
import com.bikeleasing.security.jwt.JwtTokenAuthenticationFilter
import com.bikeleasing.security.jwt.JwtUtils
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
class SecurityConfig(
    private val userDetailsService: UserDetailsService,
    private val jwtUtils: JwtUtils
) : WebSecurityConfigurerAdapter() {

    @Throws(Exception::class)
    protected override fun configure(auth: AuthenticationManagerBuilder) {
        // Configure DB authentication provider for user accounts
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder())
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    @Throws(Exception::class)
    protected override fun configure(http: HttpSecurity) {
        http
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .exceptionHandling().authenticationEntryPoint(JwtAuthenticationEntryPoint())
            .and()
            .addFilterBefore(
                JwtTokenAuthenticationFilter(userDetailsService, jwtUtils),
                UsernamePasswordAuthenticationFilter::class.java
            )
            .authorizeRequests()
            .antMatchers(
                "/api/v1/users/register**",
                "/api/v1/users/login**"
            ).permitAll()
            .antMatchers("/oauth2/**").permitAll()
            .anyRequest().authenticated()
    }

    @Throws(Exception::class)
    override fun configure(web: WebSecurity) {
        web.ignoring().mvcMatchers(
            "/api/v1/users/register**", "/oauth2/**", "/api/v1/users/register/google**",
            "/api/v1/users/login**"
        )
    }
}
