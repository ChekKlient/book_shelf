package com.ChekKlient.book_shelf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@SpringBootApplication
public class BookShelfApplication {

	@Configuration
	@EnableWebSecurity
	@EnableGlobalMethodSecurity(securedEnabled = true)
	public static class SecurityConfiguration extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			//@formatter:off
			auth
					.inMemoryAuthentication()
					.withUser("admin").password("p").roles("ADMIN");
			//@formatter:on
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			//@formatter:off
			http
					//.csrf().disable() // Use Vaadin's CSRF protection
					.authorizeRequests()
					.antMatchers("/").permitAll()
					.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
					.anyRequest().authenticated() // User must be authenticated to access any part of the application
                    .and()
                    .formLogin()
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .and()
                    .logout()
                    .permitAll()
                    .and()
                    .csrf().disable()
                    .exceptionHandling()
                    .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/log1n"));
            //@formatter:on
		}

		@Override
		public void configure(WebSecurity web) throws Exception {
			web.ignoring().antMatchers("/css/*"); // Static resources are ignored
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(BookShelfApplication.class, args);
	}
}
