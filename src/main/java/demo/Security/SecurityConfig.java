package demo.Security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import lombok.extern.slf4j.Slf4j;

import demo.Services.CustomUserDetailService;

import static demo.Security.SecurityConstants.H2_URLS;
import static demo.Security.SecurityConstants.SIGN_UP_URLS;

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

	@Autowired
	private JwtAuthenticationEntryPoint unauthorizedhandler;

	@Autowired
	private CustomUserDetailService customUserDetailService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;


	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter(){
		return new JwtAuthenticationFilter();
	}



	//i AuthenticationManagerBuilder bruger vi vores customUserDetailService som fortæller hvilken encyption vi bruger til db
   //

	@Override
	protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(customUserDetailService).passwordEncoder(bCryptPasswordEncoder);
	}


	//med denne kan vi autowire det i vores rest controller
	@Override
	@Bean(BeanIds.AUTHENTICATION_MANAGER)
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
				// indtil videre disabler vi cors csrf da vi fokusere på JWT
				http.cors().and().csrf().disable()
						.exceptionHandling()
						.authenticationEntryPoint(unauthorizedhandler).and()
						.sessionManagement()
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
						.and()
						.headers().frameOptions().sameOrigin() //enabler H2 db
						.and()
						.authorizeRequests()
						.antMatchers("/",
								"/favicon.ico",
								"/**/*.png",
								"/**/*.gif",
								"/**/*.svg",
								"/**/*.jpg",
								"/**/*.html",
								"/**/*.css",
								"/**/*.js"
								).permitAll()
						.antMatchers(SIGN_UP_URLS).permitAll()
						.antMatchers(H2_URLS).permitAll()
						.anyRequest().authenticated();

				http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
	}



}
