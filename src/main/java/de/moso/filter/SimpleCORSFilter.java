package de.moso.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class SimpleCORSFilter implements Filter {

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", /*"http://localhost:3000"*/ "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization, X-Auth-Username, X-Auth-Password, X-Auth-Token, X-Auth-SmartThings") ;

        response.setHeader("Access-Control-Expose-Headers", "Set-Cookie, Cookie, X-Auth-SmartThings");
//        response.setHeader("Access-Control-Allow-Credentials", "true");

        HttpServletRequest request = (HttpServletRequest) req;
        request.getHeaderNames();
        chain.doFilter(req, res);
    }

    public void init(FilterConfig filterConfig) {
    }

    public void destroy() {
    }

}
