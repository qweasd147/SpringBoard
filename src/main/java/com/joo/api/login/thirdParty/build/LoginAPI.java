package com.joo.api.login.thirdParty.build;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.Verb;
import com.joo.api.login.vo.UserVo;
import org.json.simple.parser.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

public interface LoginAPI{
	
	/**
	 * session에 login 관련 담을 key
	 */
	String LOGIN_SESSION_STATE_KEY="LoginState";
	String LOGIN_SESSION_KEY="LOGIN_SESSION_INFO";
	
	/**
	 * 프로퍼티에서 프로필 정보 요청 api 주소를 불러올 키값
	 */
	String USER_PROFILE="v1.user.profile";
	
	/**
	 * 프로퍼티에서 logout api 주소를 불러올 키값
	 */
	String LOGOUT_KEY="v1.token.delete";
	
	/**
	 * 로그인 요청 URL을 반환한다.
	 * @param session
	 * @param state
	 * @return
	 */
	String getAuthorizationUrl(HttpSession session, String state);
	
	/**
	 * 외부 제공지에서 accesstoken을 요청한다.
	 * @param session
	 * @param code
	 * @param state
	 * @return
	 * @throws IOException
	 */
	OAuth2AccessToken getOAuthAccessToken(HttpSession session, String code, String state) throws IOException;
	
	/**
	 * 세션에서 accessToken을 가져온다.
	 * @return
	 */
	String getAccessTokenFromSession();
	
	/**
	 * 서비스 제공하는 쪽에서의 유저 프로필 정보를 조회하여 UserVo로 넘겨준다.
	 * @return
	 * @throws IOException 
	 * @throws ParseException 
	 */
	UserVo getUserProfile(OAuth2AccessToken oauthToken) throws IOException, ParseException;
	
	/**
	 * 서비스에서 제공하는 API를 요청한다.
	 * @param method get, post 등
	 * @param commandKey
	 * @return
	 * @throws IOException 
	 */
	String requestAPI(Verb method, String commandKey, Map<String, String> params) throws IOException;

	/**
	 * 로그인 처리를 수행한다.
	 * @param req
	 * @param code
	 * @param state
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	boolean login(HttpServletRequest req, String code, String state) throws IOException, ParseException;
	
	/**
	 * 로그아웃 처리한다.
	 * @return
	 */
	boolean logOut();
	
	/**
	 * 사용자 정보가 유효한지 판별한다(세션, token 유효성 검사).
	 * @return
	 */
	boolean accountVerify();
	
	/**
	 * 현재 인스턴스의 서비스명(naver, kakao 등)을 가져온다.
	 * @return
	 */
	String getServiceName();
}
