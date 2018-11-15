package demo.security;


import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import demo.Security.JwtAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
	securedEnabled = true,
	jsr250Enabled = true,
	prePostEnabled = true
)

/*
Security config extends WebsecurityConfigureAdapter
i configure metode kan vi customize default configs og bestemme hvilke endpoints, file ext vi vil tillade
Vi kan disable cors configs, da vi lige nu fokusere på tokens
Med authenticationEntryPoint kan vi bestemme hvilke exceptions der skal smide, som er intialiseret i
JwtAuthenticationEntryPoint
 */

public class SecurityConfig extends WebSecurityConfigurerAdapter {


	private static final String INTERNAL_PATH = "/internal/**";
	private static final String API_PATH = "/api/**";
	private static final String AUTHENTICATION_PATH = "/api/authentication/**";
	private static final String COMPANY_ADMINISTRATION_PATH = "/api/company/**";
	private static final String COMPANY_CREATION_PATH = "/api/company/create";
	private static final String USER_ADMINISTRATION_PATH = "/api/user/**";

	@Autowired
	private JwtAuthenticationEntryPoint unauthorizedhandler;



	@Autowired
	private JwtAuthenticationProvider jwtAuthenticationProvider;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;




	//i AuthenticationManagerBuilder bruger vi vores customUserDetailService som fortæller hvilken encyption vi bruger til db
   //



	//med denne kan vi autowire det i vores rest controller


	@Bean
	public AuthenticationManager authenticationManager() throws Exception {
		return new ProviderManager(Collections.singletonList(jwtAuthenticationProvider));
	}




	@Bean
	public JwtAuthenticationTokenFilter authenticationsTokenFilter () throws Exception {
		JwtAuthenticationTokenFilter filter = new JwtAuthenticationTokenFilter();

		filter.setAuthenticationManager(authenticationManager());
		//filter.setAuthenticationSuccessHandler(new JwtSuccessHandler());
		return filter;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
				// indtil videre disabler vi cors csrf da vi fokusere på JWT

		http.csrf().disable()
				.authorizeRequests().antMatchers("**/rest/**").authenticated()
				.and()
				.exceptionHandling().authenticationEntryPoint(unauthorizedhandler)
				.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.addFilterBefore(authenticationsTokenFilter(), UsernamePasswordAuthenticationFilter.class);
		http.headers().cacheControl();

	}



}
