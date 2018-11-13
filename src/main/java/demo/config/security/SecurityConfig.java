package demo.config.security;

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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String INTERNAL_PATH = "/internal/**";
	private static final String API_PATH = "/api/**";
	private static final String AUTHENTICATION_PATH = "/api/authentication/**";
	private static final String COMPANY_ADMINISTRATION_PATH = "/api/company/**";
	private static final String COMPANY_CREATION_PATH = "/api/company/create";
	private static final String USER_ADMINISTRATION_PATH = "/api/user/**";

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
		//filter.setAuthenticationSuccessHandler(new JwtSuccessHandler());
		return filter;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		// disable the cross-site request
		http.csrf().disable()
				.authorizeRequests().antMatchers("**/rest/**").authenticated()
				.and()
				.exceptionHandling().authenticationEntryPoint(entryPoint)
				.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.addFilterBefore(authenticationsTokenFilter(), UsernamePasswordAuthenticationFilter.class);
		http.headers().cacheControl();
	}

}
