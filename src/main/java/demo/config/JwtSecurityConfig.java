package demo.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.extern.slf4j.Slf4j;

import demo.security.JwtAuthenticationEntryPoint;
import demo.security.JwtAuthenticationProvider;
import demo.security.JwtAuthenticationTokenFilter;
import demo.security.JwtSuccessHandler;

@Slf4j
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@Configuration
public class JwtSecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String INTERNAL_PATH = "/internal/**";
	private static final String API_PATH = "/rest/**";
	private static final String AUTHENTICATION_PATH = "/rest/authentication/**";
	private static final String ROLE_LIST_PATH = "/rest/authentication/roles";
	private static final String USER_ADMINISTRATION_PATH = "/rest/user/**";
	private static final String USER_LIST_PATH = "/rest/user/all";
	private static final String PASSWORD_RESET_REQUEST_PATH = "/rest/password/**";


	// Declare AuthenticationProvider
	@Autowired
	private JwtAuthenticationProvider authenticationProvider;
	@Autowired
	private JwtAuthenticationEntryPoint entryPoint;

	// Create AuthenticationManager with custom authenticationprovider, so we can write promissions
	@Bean
	public AuthenticationManager authenticationManager() {
		return new ProviderManager(Collections.singletonList(authenticationProvider));
	}

	// AuthencationManager injected as the manager into this filter
	@Bean
	public JwtAuthenticationTokenFilter authenticationsTokenFilter () {
		JwtAuthenticationTokenFilter filter = new JwtAuthenticationTokenFilter();
		filter.setAuthenticationManager(authenticationManager());
		filter.setAuthenticationSuccessHandler(new JwtSuccessHandler());
		return filter;
	}

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder(11);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		// spring security can control when session will create "stateless" means no session will be created or used
		// antMatchers states that this HttpSecurity will only be aplicate to URLSs that start with /rest/
		http
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authorizeRequests().antMatchers(AUTHENTICATION_PATH).permitAll()
				.and()
				.authorizeRequests().antMatchers(INTERNAL_PATH).permitAll()
				.and()
				.authorizeRequests().antMatchers(PASSWORD_RESET_REQUEST_PATH).permitAll()
				.and()
				.authorizeRequests().antMatchers(USER_LIST_PATH).authenticated()
				.and()
				.authorizeRequests().antMatchers(USER_ADMINISTRATION_PATH).authenticated()
				.and()
				.authorizeRequests().antMatchers(ROLE_LIST_PATH).hasAnyAuthority("ADMIN", "SUPERUSER")
				.and()
				.authorizeRequests().antMatchers(API_PATH).authenticated()
				.and()
				.csrf().disable()
				.exceptionHandling().authenticationEntryPoint(entryPoint)
				.and();
		http.addFilterBefore(authenticationsTokenFilter(), UsernamePasswordAuthenticationFilter.class);
		http.headers().cacheControl();
	}

}
