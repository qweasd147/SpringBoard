package com.joo.security;

import com.joo.utils.CookieUtils;
import com.joo.web.controller.common.BaseController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/v1/authen")
@CrossOrigin(origins = "*")
public class LoginController implements BaseController{

    @Value("${jwt.header}")
    private String tokenHeader;

    @GetMapping("/logout")
    public ResponseEntity selectBoardList(@AuthenticationPrincipal CustomUserDetails customUserDetails){

        CookieUtils.removeCookie(tokenHeader);
        //SecurityContextHolder.getContext().setAuthentication(null);

        return successRespResult();
    }
}
