package com.xws.tim12;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.xws.tim12.security.JwtAuthenticationRequest;
import com.xws.tim12.security.TokenUtils;

import feign.FeignException;

@Component
public class AuthFilter extends ZuulFilter{

	@Autowired
	private AuthClient authClient;
	
	@Autowired
	private TokenUtils tokenUtils;
	
	@Override
	public String filterType() {
		return "pre";
	}
	
	@Override
	public int filterOrder() {
		return 1;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}
	
	@Override
	public Object run() throws ZuulException {
		
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		
		if(request.getHeader("Authorization") == null) {
			setFailedRequest("User does not exist", 403);
			return null;
		} 
		
		String authRequest = request.getHeader("Authorization");
		System.out.println(authRequest);
		
		if(authRequest.contains("Bearer")) {
			System.out.println("IMA TOKEN");
			String token = tokenUtils.getToken(request);
			System.out.println(token);
			
			String username = tokenUtils.getUsernameFromToken(token);
			System.out.println(username);
			
			String role = tokenUtils.getRoleFromToken(token);
			System.out.println(role);
			
			ctx.addZuulRequestHeader("role", role);
		} else {
			System.out.println("IMA USERNAME I PASSWORD");
		}
		
		try {
			//System.out.println(jwt.getUsername());
			//System.out.println(jwt.getPassword());
			//authClient.login(jwt);
			//ctx.addZuulRequestHeader("username", username);
			//ctx.addZuulRequestHeader("password", password);
			//ctx.addZuulRequestHeader("Authorization", "jwt");
		} catch (FeignException.NotFound e) {
			setFailedRequest("User does not exist", 403);
		}
		
		return null;
	}
	
    private void setFailedRequest(String body, int code) {
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.setResponseStatusCode(code);
        if (ctx.getResponseBody() == null) {
            ctx.setResponseBody(body);
            ctx.setSendZuulResponse(false);
        }
    }
}
