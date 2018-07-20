package com.joo.api.login.thirdParty.support;


import com.github.scribejava.core.model.Verb;
import com.joo.api.login.build.LoginAPI;
import com.joo.api.login.build.LoginFactory;
import com.joo.api.login.vo.UserVo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GoogleAPI extends LoginFactory {
	
	private static final Logger logger = LoggerFactory.getLogger(GoogleAPI.class);
	
	//TODO : host값을 어디서 초기화 해야 하는지 고민중
	private String host = "http://localhost";
	
	private String serviceName;
	private String clientId;
	private String clientSecret;
	private String redirectURL;
	private String accessTokenEndPoint;
	private String authorizationBaseURL;
	
	@Override
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	@Override
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	@Override
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	@Override
	public void setRedirectURL(String redirectURL) {
		this.redirectURL = host + redirectURL;
	}

	@Override
	public void setAccesstokenEndpoint(String accessTokenEndPoint) {
		this.accessTokenEndPoint = accessTokenEndPoint;
		
	}

	@Override
	public void setAuthorizationBaseURL(String authorizationBaseURL) {
		this.authorizationBaseURL = authorizationBaseURL;
	}

	@Override
	public String getServiceName() {
		return serviceName;
	}

	@Override
	public String getClientId() {
		return clientId;
	}

	@Override
	public String getClientSecret() {
		return clientSecret;
	}

	@Override
	public String getRedirectURL() {
		return redirectURL;
	}

	@Override
	public String getAccesstokenEndpoint() {
		return accessTokenEndPoint;
	}

	@Override
	public String getAuthorizationBaseURL() {
		return authorizationBaseURL;
	}

	@Override
	public UserVo profileToUserVo(JSONObject userProfile){
		UserVo userVo = new UserVo();
		
		if(userProfile == null || !userProfile.containsKey("name") || !userProfile.containsKey("emails")) {
			logger.error("통신 실패!");
			return null;
		}
		
		try {
			JSONArray emails = (JSONArray) userProfile.get("emails");	//TODO : google에선 emails로 넘어오는데, 왜 이렇게 array로 넘겨주는지는 알아봐야함
			JSONObject nameObj = (JSONObject) userProfile.get("name");
			
			String id =(String) userProfile.get("id");
			String name = (String)nameObj.get("familyName") + nameObj.get("givenName");
			String nickName = (String) userProfile.get("displayName");
			
			JSONObject firstEmail = (JSONObject) emails.get(0);
			String email = (String)firstEmail.get("value");
			
			userVo.setId(id)
				.setName(name)
				.setNickName(nickName)
				.setEmail(email)
				.setServiceName("google");
		}catch (Exception e) {
			throw e;
		}
		return userVo;
	}

	@Override
	public String logoutProcess() throws IOException {
		
		String requestKey = getPropertiesKey(LoginAPI.LOGOUT_KEY);
		Map<String, String> params = new HashMap<>();
		params.put("token", getAccessTokenFromSession());
		
		return requestAPI(Verb.GET,requestKey , params);
	}
}
