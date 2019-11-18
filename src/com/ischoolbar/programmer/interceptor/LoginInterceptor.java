package com.ischoolbar.programmer.interceptor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.ischoolbar.programmer.entity.User;

/**
 * 登录过滤拦截器
 * @author llq
 *
 */
public class LoginInterceptor  implements HandlerInterceptor{

	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object arg2) throws Exception {
		// TODO Auto-generated method stub
		String url = request.getRequestURI();
		System.out.println("进入拦截器，url = " + url);
		Object user = request.getSession().getAttribute("user");
		if(user == null){
//			表示未登录或者登录状态失效
			System.out.println("未登录或登录失效，url = " + url);
			
			//ajax请求  的时候 判断时候登陆   
			if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
				//X-Requested-With代表是ajax请求     若是  给用户提示信息
				Map<String, String> regMap = new HashMap<String, String>();
				regMap.put("type", "error");
				regMap.put("msg", "信息已过期，请登录！");
				response.getWriter().write(JSONObject.fromObject(regMap).toString());
			}
			
			response.sendRedirect(request.getContextPath() + "/system/login");
			return false;
		}
		return true;
	}

}
