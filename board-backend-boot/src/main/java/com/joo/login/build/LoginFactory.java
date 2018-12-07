package com.joo.login.build;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.*;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.joo.model.dto.UserDto;
import com.joo.service.user.UserService;
import com.joo.utils.WebUtil;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.function.BiConsumer;

public abstract class LoginFactory implements LoginAPI{
	
	private static final Logger logger = LoggerFactory.getLogger(LoginFactory.class);

	@Autowired
	private UserService userService;
	
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
	
	public abstract UserDto profileToUserVo(JSONObject userProfile) throws Exception;
	
	public abstract String logoutProcess(String thirdpartyToken) throws IOException;
	
	
	/**
	 * 세션에 담긴 값을 넣는다.
	 * @param session
	 * @return
	 */
	public static String getSessionState(HttpSession session){
		return (String) session.getAttribute(LoginAPI.LOGIN_SESSION_STATE_KEY);
	}

	@Override
	public String getAuthorizationUrl(HttpSession session, String state) {
		
		//Scribe에서 제공하는 인증 URL 생성
		OAuth20Service oauthService = getServiceBuilder(true).state(state).build(innerAPI);
		
		return oauthService.getAuthorizationUrl();
	}

	@Override
	public OAuth2AccessToken getOAuthAccessToken(String sessionState, String code, String state) throws IOException {
		
		//Callback으로 전달받은 세선검증용 난수값과 세션에 저장되어있는 값이 일치하는지 확인
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
		
		String accessToken = null;
		if(params != null)	accessToken = params.get(ACCESS_TOKEN);

		OAuth20Service service = getServiceBuilder(false).build(innerAPI);
		boolean hasServiceURL = properties.containsKey(commandKey);
		
		//해당 키값이 프로퍼티에 없을 때
		if(!hasServiceURL){
			logger.warn("해당 키가 프로퍼티에 존재하지 않음. key : "+commandKey);
			
			return null;
		}
		
		String serviceURL = properties.getProperty(commandKey);
		OAuthRequest oauthReq = new OAuthRequest(method, serviceURL, service);
		
		//token을 부여한다.
		if(!StringUtils.isEmpty(accessToken))
			oauthReq.addQuerystringParameter(OAuthConstants.ACCESS_TOKEN, accessToken);
		
		if(params != null){
			BiConsumer<String, String> addParameter;

			switch(method){
				case GET :
					addParameter = (key, value)->oauthReq.addQuerystringParameter(key, value);
					break;
				case POST :
					addParameter = (key, value)->oauthReq.addBodyParameter(key, value);
					break;
			default:
				logger.warn("get, post를 제외한 다른 메소드는 준비중");
				throw new UnsupportedOperationException("get, post를 제외한 다른 메소드는 준비중");
			}

			params.keySet().stream()
					.filter(key -> !ACCESS_TOKEN.equals(params.get(key)))
					.forEach((key)-> addParameter.accept(key, params.get(key)));
		}
		
		Response modelResp = oauthReq.send();
		String result = modelResp.getBody();

		if(logger.isDebugEnabled()){
			int code = modelResp.getCode();

			String message = modelResp.getMessage();
			logger.debug("성공? : " + HttpStatus.valueOf(code).is2xxSuccessful());
			logger.debug("message : " + message);
		}

		return result;
	}
	
	@Override
	public String getAccessTokenFromSession() {
		
		UserDto user = WebUtil.getSessionAttribute(LoginAPI.LOGIN_SESSION_KEY);

		if(user == null)	return null;
		String accessToken = user.getThirdPartyToken();
		
		return accessToken;
	}
	
	@Override
	public UserDto getUserVoWithProfile(OAuth2AccessToken oauthToken) throws IOException, ParseException {
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
		
		UserDto userDto;
		try {
			userDto = profileToUserVo(userProfile);
		} catch (Exception e) {
			logger.error("파싱 에러남 : "+e.getMessage());
			userDto = null;
		}
		
		return userDto;
	}
	
	@Override
	public UserDto getValidatedUserVo(String sessionState, String code, String state) throws IOException, ParseException {
		
		OAuth2AccessToken oauthToken = getOAuthAccessToken(sessionState, code, state);
    	
		if(oauthToken == null) {
			logger.error("로그인 잘못됨! state값과 session에 저장된 state 값이 다름");
			return null;
		}

		UserDto userDtoFromThird = getUserVoWithProfile(oauthToken);
    	
		if(userDtoFromThird == null) {
			logger.error("로그인 잘못됨!");
			
			return null;
		}

		UserDto userVoFromDB = userService.getUserByCandidate(userDtoFromThird.getServiceName(), userDtoFromThird.getId());

		if(userVoFromDB == null){
			//기존에 등록된 정보가 없으면 새롭게 등록한다.
			userService.regist(userDtoFromThird);
			userDtoFromThird.setState(UserDto.State.ENABLED);

			userVoFromDB = userDtoFromThird;
		}

		//토큰 정보를 담아준다.
		String token = oauthToken.getAccessToken();
		userVoFromDB.setThirdPartyToken(token);

		return userVoFromDB;
	}
	
	@Override
	public boolean logout(String thirdpartyToken) {
		
		String result;
		
		try {
			result = logoutProcess(thirdpartyToken);
			logger.info("로그아웃 성공. msg : "+result);
		} catch (IOException e) {
			logger.warn("logout 요청에 실패!");
		}finally {
			//세션 방식으로 하지 않음
			//WebUtil.removeSessionAttribute(LoginAPI.LOGIN_SESSION_KEY);
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
	private class InnerAPI extends DefaultApi20 {
		
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
