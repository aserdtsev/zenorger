package ru.serdtsev.zenorger.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import javax.sql.DataSource

@Configuration
@EnableWebSecurity(debug = false)
class WebSecurityConfig(val dataSource: DataSource) : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
        http.httpBasic()
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        http.authorizeRequests().anyRequest().hasAnyRole("USER")
    }

    override fun configure(web: WebSecurity?) {
        web!!.ignoring().antMatchers(
                "/static/**",
                "/api/user/signUp",
                "/swagger*/**",
                "/v2/api-docs",
                "/webjars/**",
                "/csrf",
                "/error")
    }

    @Bean
    fun encoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

    @Autowired
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select login, password, true from zenorger.service_user where login = ?")
                .authoritiesByUsernameQuery("select login, 'ROLE_USER' from zenorger.service_user where login = ?")
    }
}