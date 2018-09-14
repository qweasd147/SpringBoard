package com.joo.api.login;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.joo.api.common.Result;
import com.joo.api.common.controller.BaseController;
import com.joo.api.login.build.HandleLoginFactory;
import com.joo.api.login.build.LoginAPI;
import com.joo.api.login.build.LoginFactory;
import com.joo.api.login.vo.UserVo;
import com.joo.api.security.TokenUtils;
import com.joo.api.security.custom.CustomUserDetails;
import com.joo.api.security.custom.TokenBasedAuthentication;
import com.joo.api.utils.CookieUtils;
import com.joo.api.utils.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * login, logout 처리 controller.
 * 차후 loginFactory 인스턴스(beab)가 추가 된다 하더라도, 현재 class는
 * 수정사항 없이 관리 할 예정
 * @author joo
 *
 */

@RequestMapping(value = "/api/authen")
@CrossOrigin(origins = "*")
@Controller
public class LoginController implements BaseController{

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private static final Integer TOKEN_EXPIRATION = TokenUtils.expiration;

    @Value("#{appProperty['jwt.header']}")
    private String tokenHeader;

    /**
     * 로그인 성공 후 이동할 URL
     */
    @Value("#{appProperty['redirect.domain']}")
    private String cbDomain;

    @Autowired
    private List<? extends LoginFactory> loginFactoryList;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenUtils tokenUtils;

    /**
     * 로그인 페이지로 이동 요청 바인딩
     * @return
     */
    @RequestMapping("/login")
    public String loginList(HttpServletRequest req, Model model) {

        //로그인 URL 값을 model에 넣는다.
        new HandleLoginFactory(loginFactoryList).setLoginURLParams(model);

        return "loginList";
    }

    /**
     * 로그인 이동 URL 목록을 반환한다.
     * @return
     */
    @RequestMapping("/loginURL")
    @ResponseBody
    public ResponseEntity loginURL() {

        // third party 로그인 URL 값을 담아서 보내준다.
        Map<String, String> urlMap = new HandleLoginFactory(loginFactoryList).getLoginURLParams();

        return new ResponseEntity<Result>(Result.getSuccessResult(urlMap), HttpStatus.OK);
    }

    /**
     * 각 서비스 별 login 처리를 진행한다.
     * code, state는 callback을 호출 시, 외부(각 api 서비스 하는 곳)에서 제공받음
     * @param code
     * @param state
     * @param req
     * @return
     * @throws Exception
     */
    @RequestMapping("login/{serviceName}/callback")
    public String serviceLoginCB(@RequestParam String code
            , @PathVariable("serviceName") String serviceName
            , @RequestParam String state, HttpServletRequest req) throws Exception {

        String beanName = serviceName+"Login";

        LoginAPI serviceFactory = (LoginAPI) WebUtil.getBean(req, beanName);

        if(serviceFactory == null){
            //TODO : null값 일 시, redirect 처리 어찌 해야할지
            logger.error("bean을 찾을 수 없음! :"+beanName);
            return "redirect:"+cbDomain;
        }

        String sessionState = LoginFactory.getSessionState(req.getSession());
        UserVo userVo = serviceFactory.getValidatedUserVo(sessionState, code, state);

        /*
        기존 세션 방식 로그인
        WebUtil.setSession(LoginAPI.LOGIN_SESSION_KEY, userVo);
        logger.info("login success. User Vo :"+userVo);
        */

        CustomUserDetails userDetails = (CustomUserDetails) this.userDetailsService.loadUserByUsername(String.valueOf(userVo.getIdx()));
        userDetails.setThirdPartyToken(userVo.getThirdPartyToken());
        String token = this.tokenUtils.createToken(userDetails);

        CookieUtils.setCookie(tokenHeader, token, TOKEN_EXPIRATION);

        TokenBasedAuthentication authentication = new TokenBasedAuthentication(userDetails);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "redirect:"+cbDomain;
    }

    @RequestMapping("/logout")
    @ResponseBody
    public ResponseEntity logout(@AuthenticationPrincipal CustomUserDetails customUserDetails){

        LoginAPI loginAPI = (LoginAPI) WebUtil.getBean(customUserDetails.getServiceName()+"Login");
        loginAPI.logout(customUserDetails.getThirdPartyToken());

        SecurityContextHolder.getContext().setAuthentication(null);
        //CookieUtils.removeCookie(tokenHeader);

        return successRespResult();
    }
}
