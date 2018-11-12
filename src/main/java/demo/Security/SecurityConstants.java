package demo.Security;

/*
SecurityConstants brges final strings, der bruges i både SecurityConfig og JwtTokenProvider
Sign up og h2 urls urls til at tilllade rest end point og endelser
Resten bruges til at generere og validere en user token. Token_PREFIX bearer behøver white space, da de sender token i http headeren

 */

public class SecurityConstants {

	public static final String SIGN_UP_URLS = "api/**";
	public static final String H2_URLS = "h2-console/**";
	public static final String SECRET = "SecretKeyToGenJWTs";
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final long EXPIRATION_TIME = 30_000;
}
