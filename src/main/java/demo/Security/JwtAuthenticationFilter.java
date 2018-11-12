package demo.Security;

import java.io.IOException;
import java.util.Collections;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import demo.model.User;
import demo.service.impl.CustomUserDetailService;
import demo.model.User;
import demo.service.impl.CustomUserDetailService;

import static demo.Security.SecurityConstants.HEADER_STRING;
import static demo.Security.SecurityConstants.TOKEN_PREFIX;



public class JwtAuthenticationFilter extends OncePerRequestFilter {
@Autowired
private JwtTokenProvider jwtTokenProvider;

@Autowired
private CustomUserDetailService customUserDetailService;


	/**
	 * Same contract as for {@code doFilter}, but guaranteed to be
	 * just invoked once per request within a single request thread.
	 * See {@link #shouldNotFilterAsyncDispatch()} for details.
	 * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the
	 * default ServletRequest and ServletResponse ones.
	 *
	 * @param request
	 * @param response
	 * @param filterChain
	 *
	 * /'
	 * bruger validate token fra jwttokenprovider
	 * og validere uf fra userid
	 * bruger ikke credential men token til at validere
	 *
	 *
	 */
	@Override protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		try {
			//grabbing token from request
			String jwt = getJwtFromRequest(request);

			if(StringUtils.hasText(jwt)&& jwtTokenProvider.validateToken(jwt)){
				Long userId = jwtTokenProvider.getUserIdFromJWT(jwt);
				User userDetails = customUserDetailService.loadUserById(userId);
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(

						userDetails, null, Collections.emptyList());
				auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(auth);

			}

		}catch (Exception e){

logger.error("could not user auth in security context");
		}

		filterChain.doFilter(request, response);

	}

	//tager token fra http kald
	private String getJwtFromRequest (HttpServletRequest request){
		String bearerToken = request.getHeader(HEADER_STRING);
		if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)){

			//return token som er den generede token efter "bearer som 6 chars" i headeren
			return bearerToken.substring(7, bearerToken.length());
		}
		return null;

	}

}
