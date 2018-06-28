package com.joo.api.login.thirdParty.build;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.*;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.joo.api.login.vo.UserVo;
import com.joo.api.utils.WebUtil;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public abstract class LoginFactory implements LoginAPI{
	
	private static final Logger logger = LoggerFactory.getLogger(LoginFactory.class);
	
	//TODO : 어디서 host값을 받아올지 고민중
	private String host = "http://localhost";
	
	/**
	 * serviceName만 interface에 getter를 만들어 놓음.
	 * 그 외 정보는 다른곳에서 구지 핸들링 할 필요가 없을꺼 같음 
	 */
	
	private static final JSONParser JSON_PARSER = new JSONParser();
	
	//request 주소를 담은 프로퍼티
	@Resource(name="requestURL")
	private Properties properties;

	@Value("#{appProperty['login.isCheckState']}")
	protected boolean IS_CHECK_STATE;
	
	public InnerAPI innerAPI = new InnerAPI();
	
	public abstract void setServiceName(String serviceName);

	public abstract void setClientId(String clientId);

	public abstract void setClientSecret(String clientSecret);

	public abstract void setRedirectURL(String redirectURL);

	public abstract void setAccesstokenEndpoint(String accessTokenEndPoint);
	
	public abstract void setAuthorizationBaseURL(String authorizationBaseURL);
	
	public abstract String getServiceName();
	
	public abstract String getClientId();
	
	public abstract String getClientSecret();
	
	public abstract String getRedirectURL();
	
	public abstract String getAccesstokenEndpoint();
	
	public abstract String getAuthorizationBaseURL();
	
	public abstract UserVo getUserVo(JSONObject userProfile) throws Exception;
	
	public abstract String logoutProcess() throws IOException;
	
	
	/**
	 * 세션에 담긴 값을 넣는다.
	 * @param session
	 * @return
	 */
	private String getSessionState(HttpSession session){
		return (String) session.getAttribute(LoginAPI.LOGIN_SESSION_STATE_KEY);
	}

	@Override
	public String getAuthorizationUrl(HttpSession session, String state) {
		
		//Scribe에서 제공하는 인증 URL 생성
		OAuth20Service oauthService = getServiceBuilder(true).state(state).build(innerAPI);
		
		return oauthService.getAuthorizationUrl();
	}

	@Override
	public OAuth2AccessToken getOAuthAccessToken(HttpSession session, String code, String state) throws IOException {
		
		//Callback으로 전달받은 세선검증용 난수값과 세션에 저장되어있는 값이 일치하는지 확인
		String sessionState = getSessionState(session);
		
		if(!IS_CHECK_STATE || sessionState !=null && sessionState.equals(state)){
			OAuth20Service oauthService = getServiceBuilder(true).build(innerAPI);
			// Scribe에서 제공하는 AccessToken 획득 기능으로 네아로 Access Token을 획득 
			OAuth2AccessToken accessToken = oauthService.getAccessToken(code);
			return accessToken;
		}
		return null;
	}
	
	@Override
	public String requestAPI(Verb method, String commandKey, Map<String, String> params) throws IOException {
		
		String accessToken = getAccessTokenFromSession();
		OAuth20Service service = getServiceBuilder(false).build(innerAPI);
		boolean hasServiceURL = properties.containsKey(commandKey);
		
		//해당 키값이 프로퍼티에 없을 때
		if(!hasServiceURL){
			logger.warn("해당 키가 프로퍼티에 존재하지 않음. key : "+commandKey);
			
			return null;
		};
		
		String serviceURL = properties.getProperty(commandKey);
		OAuthRequest oauthReq = new OAuthRequest(method, serviceURL, service);
		
		//token을 부여한다.
		oauthReq.addQuerystringParameter(OAuthConstants.ACCESS_TOKEN, accessToken);
		
		if(params != null){
			Iterator<String> paramsKeys = params.keySet().iterator();
			
			switch(method){
				case GET :
					while(paramsKeys.hasNext()){
						String key = paramsKeys.next();
						oauthReq.addQuerystringParameter(key, params.get(key));
					}
					break;
				case POST :
					while(paramsKeys.hasNext()){
						String key = paramsKeys.next();
						oauthReq.addBodyParameter(key, params.get(key));
					}
					break;
			default:
				logger.warn("get, post를 제외한 다른 메소드는 준비중");
				break; 
			}
		}
		
		Response modelResp = oauthReq.send();
		String result = modelResp.getBody();
		
		logger.debug("result 결과 : "+result);
		
		return result;
	}
	
	@Override
	public String getAccessTokenFromSession() {
		
		UserVo userVo = (UserVo) WebUtil.getSessionAttribute(LoginAPI.LOGIN_SESSION_KEY);
		
		if(userVo == null)	return null;
		
		String accessToken = userVo.getAccessToken();
		
		return accessToken;
	}
	
	@Override
	public UserVo getUserProfile(OAuth2AccessToken oauthToken) throws IOException, ParseException {
		OAuth20Service oauthService = getServiceBuilder(true).build(innerAPI);
		
		String requestKey = getPropertiesKey(LoginAPI.USER_PROFILE);
		boolean requestURL = properties.containsKey(requestKey);
		
		if(!requestURL){
			logger.error("url이 존재하지 않음. properties key : "+requestKey);
			return null;
		}
		
		//TODO : requestAPI 완성되면 합칠 예정
		OAuthRequest request = new OAuthRequest(Verb.GET, properties.getProperty(requestKey), oauthService);
		oauthService.signRequest(oauthToken, request);
		
		Response response = request.send();
		String strResult = response.getBody();
		JSONObject userProfile = (JSONObject)JSON_PARSER.parse(strResult);
		
		/*
		if(userMethod == null) {
			logger.error("UserMethod가 정의되어 있지 않음");
			
			return null;
		}
		*/
		
		UserVo userVo = null;
		try {
			userVo = getUserVo(userProfile);
		} catch (Exception e) {
			//parsing exception
			//e.printStackTrace();
			
			userVo = null;
		}
		
		return userVo;
	}
	
	@Override
	public boolean login(HttpServletRequest req, String code, String state) throws IOException, ParseException {
		
		HttpSession session = req.getSession();
		OAuth2AccessToken oauthToken = getOAuthAccessToken(session, code, state);
    	
		if(oauthToken == null) {
			logger.error("로그인 잘못됨! state값과 session에 저장된 state 값이 다름");
			return false;
		}
		
    	String token = oauthToken.getAccessToken();
    	UserVo userVo = getUserProfile(oauthToken);
    	
		if(userVo == null) {
			logger.error("로그인 잘못됨!");
			
			return false;
		}
		
		userVo.setAccessToken(token);
		
		/*
		 * TODO : userVo로 DB에 등록된 사용자를 조회. DB 구축되면 구현
    	Object userData = loginService.getUser(userVo);
    	*/
    	WebUtil.setSessionAttribute(req, LoginAPI.LOGIN_SESSION_KEY, userVo);
    	logger.info("login success. User Vo :"+userVo);
    	
		return true;
	}
	
	@Override
	public boolean logOut() {
		
		String result = null;
		
		try {
			result = logoutProcess();
			logger.info("로그아웃 성공. msg : "+result);
		} catch (IOException e) {
			logger.warn("logout 요청에 실패!");
		}finally {
			WebUtil.removeSessionAttribute(LoginAPI.LOGIN_SESSION_KEY);
		}
		
		return true;
	}
	
	@Override
	public boolean accountVerify() {
		//TODO : 세션에 로그인 정보가 있는지, access token이 유효한지.....
		//판별 api 찾고있는중
		return false;
	}
	
	/**
	 * 레퍼런스에는 DefaultApi20를 service에 주입을 해야한다고 나와있음.
	 * 다른곳에 만들어도 쓸때도 없어서 그냥 inner class로 선언함
	 * @author KIM
	 *
	 */
	private class InnerAPI extends DefaultApi20{
		
		@Override
		public String getAccessTokenEndpoint() {
			return LoginFactory.this.getAccesstokenEndpoint();
		}

		@Override
		protected String getAuthorizationBaseUrl() {
			return LoginFactory.this.getAuthorizationBaseURL();
		}
	}
	
	private ServiceBuilder getServiceBuilder(boolean addCallback){
		
		ServiceBuilder sb = new ServiceBuilder().apiKey(getClientId());
		
		String clientSecret = getClientSecret(); 
		
		if(clientSecret != null && !clientSecret.isEmpty())
			sb.apiSecret(clientSecret);
		
		if(addCallback){
			sb.callback(getRedirectURL());
		}
		
		return sb;
	}
	
	protected String getPropertiesKey(String commandKey){
		return getServiceName()+"."+commandKey;
	}
}
