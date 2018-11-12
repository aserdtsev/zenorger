package ru.serdtsev.zenorger.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
@EnableWebSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter() {
  @Throws(Exception::class)
  override fun configure(http: HttpSecurity) {
    http.csrf().disable()
            .authorizeRequests().antMatchers("/").permitAll().anyRequest().authenticated()
            .and().httpBasic()
  }

  @Bean
  fun encoder(): BCryptPasswordEncoder {
    return BCryptPasswordEncoder()
  }

  @Autowired
  @Throws(Exception::class)
  fun configureGlobal(auth: AuthenticationManagerBuilder) {
    auth.inMemoryAuthentication()
        .withUser("andrey.serdtsev@gmail.com")
        .password("\$2a\$04\$FBkPSIKW3NnenSDmZDRhQe4zLNqeOfDOiXt6J6d7H16V.WxaKqB06")
        .roles("USER")
  }
}