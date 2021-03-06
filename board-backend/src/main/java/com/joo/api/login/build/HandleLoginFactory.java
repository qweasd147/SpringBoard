package com.joo.api.login.build;

import com.joo.api.security.custom.CustomUserDetails;
import com.joo.api.utils.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * LoginFactory를 핸들링 하는게 목적.
 * 필요에 따라 리스트나 단일 factory를 핸들링 할 꺼라서 필요 할때 마다
 * 인스턴스 새로 만들어서 쓰는게 좋을꺼 같음. 뭐 bean에 올라간 인스턴스
 * 데이터를 수정하는건 아니라서 별 상관 없기도 함....
 * 
 * 소스가 많지도 않고 딱히 큰 장점은 없는데 다른 소스 보다가 이런식으로 만든게 보여서 그냥 이런식으로 만듦
 *
 * TODO : 근데 의도에 맞을려면 deep copy 해야함... 일단 보류
 * TODO : 현재 Model객체에 직접 접근하여 값을 넣는 부분도 있긴 한데 바꾸긴 해야하는데 일단 보류
 * TODO : 생각 할 수록 불필요할꺼 같음.. 걷어낼 예정
 * @author joo
 *
 */
public class HandleLoginFactory {
	
	private LoginAPI loginAPI;
	private List<? extends LoginAPI> loginAPIList;
	
	private static final Logger logger = LoggerFactory.getLogger(HandleLoginFactory.class);
	
	public HandleLoginFactory(LoginAPI loginAPI) {
		this.loginAPI = loginAPI;
	}
	
	public HandleLoginFactory(List<? extends LoginAPI> loginAPIList) {
		this.loginAPIList = loginAPIList;
	}

	/**
	 * 1. 세션 유효성 검증을 위하여 난수를 생성
	 * 2. 생성한 난수 값을 session에 저장
	 * 3. URL param값을 넣음
	 * 
	 * 1,2 목적 : 현재 프로젝트에 있는 login list(정확히 setLoginURLParams 메소드)를 거쳐 가야만 로그인 처리 가능
	 * @param model
	 */
	public void setLoginURLParams(Model model) {
		Map<String, String> urlMap = getLoginURLParams();
		model.addAllAttributes(urlMap);
	}

	public Map<String, String> getLoginURLParams(){

		Map<String, String> urlMap = new HashMap<>();

		if(loginAPIList != null){

			String state = UUID.randomUUID().toString();
			WebUtil.setSession(LoginAPI.LOGIN_SESSION_STATE_KEY, state);

			HttpSession session = WebUtil.getSession();

			for(int i=0;i<loginAPIList.size();i++){
				LoginAPI loginFactoryClazz = loginAPIList.get(i);

				logger.info("load login factory. "+loginFactoryClazz.getServiceName());

				String authURL = loginFactoryClazz.getAuthorizationUrl(session, state);
				String serviceUrlName = loginFactoryClazz.getServiceName()+"URL";

				urlMap.put(serviceUrlName, authURL);
			}
		}else{
			logger.warn("not exist LoginFactory instance");
		}

		return urlMap;
	}
	
	public Map<String, String> getLogOutResult(Map<String, String> params) {
		
		if(loginAPI == null) {
			logger.warn("loginAPI이 존재하지 않음");
		}

		Objects.requireNonNull(this.loginAPI);

		CustomUserDetails userDetails = (CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		boolean logOutResult = loginAPI.logout(userDetails.getThirdPartyToken());

		if(logOutResult) {
			logger.debug("result", "로그아웃 처리됨");
    	}else {
    		logger.error("시스템 오류");
    	}
		
		return params;
	}
}
