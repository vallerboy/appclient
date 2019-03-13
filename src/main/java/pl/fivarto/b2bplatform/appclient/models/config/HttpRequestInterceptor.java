package pl.fivarto.b2bplatform.appclient.models.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import pl.fivarto.b2bplatform.appclient.models.UserSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

@Configuration
public class HttpRequestInterceptor extends HandlerInterceptorAdapter {

    final UserSession userSession;
    private static List<String> RESOURCE_FOLDERS = Arrays.asList("css", "vendor", "fonts", "image", "img", "js", "scss");

    @Autowired
    public HttpRequestInterceptor(UserSession userSession) {
        this.userSession = userSession;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(isResourceCatalog(request.getRequestURI())){
            return super.preHandle(request, response, handler);
        }

        if(request.getRequestURI().contains("register")){
            return true;
        }

        if(request.getRequestURI().contains("api")){
            return true;
        }

        if(!request.getRequestURI().contains("login") && !userSession.isLogin()){
            response.sendRedirect("/login");
            return false;
        }

        if(request.getRequestURI().contains("admin") && userSession.isLogin() && !userSession.getCustomer().isAdmin()){
            response.sendRedirect("/");
            return false;
        }

        return super.preHandle(request, response, handler);
    }

    private boolean isResourceCatalog(String request) {
        return RESOURCE_FOLDERS.stream().anyMatch(request::contains);
    }
}
