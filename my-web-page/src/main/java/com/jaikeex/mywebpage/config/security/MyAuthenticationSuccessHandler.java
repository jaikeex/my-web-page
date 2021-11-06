package com.jaikeex.mywebpage.config.security;

import com.jaikeex.mywebpage.mainwebsite.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.security.web.savedrequest.RequestCache;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyAuthenticationSuccessHandler
        extends SavedRequestAwareAuthenticationSuccessHandler
        {

    UserService accountManagementService;
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    public MyAuthenticationSuccessHandler(UserService accountManagementService) {
        this.accountManagementService = accountManagementService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        final RequestCache requestCache = new HttpSessionRequestCache();
        final SavedRequest savedRequest = requestCache.getRequest(request, response);
        String targetUrl = getTargetUrl(savedRequest);

        String username = authentication.getName();
        accountManagementService.updateUserStatsOnLogin(username);
        redirectStrategy.sendRedirect(request, response, targetUrl);

    }

    private String getTargetUrl(SavedRequest savedRequest) {
        String targetUrl;
        if (savedRequest != null) {
            targetUrl = savedRequest.getRedirectUrl();
        }
        else {
            targetUrl = "/";
        }
        return targetUrl;
    }
}
