package com.ischoolbar.programmer.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ischoolbar.programmer.entity.User;
import com.ischoolbar.programmer.service.UserService;
import com.ischoolbar.programmer.service.impl.UserServiceImple;
import com.ischoolbar.programmer.util.CpachaUtil;

/*
 * 系统主页控制器
 */
//在访问一个链接的时候(这个页面中)  都是在/system后面的
//@Controller是一个接口	    在web.xml中已经配置有扫描包
//根据页面的需要跳转的路径来找到具体执行哪儿个    

@Controller
@RequestMapping("/system")
public class SystemController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/index",method = RequestMethod.GET)
	public ModelAndView index(ModelAndView modelAndView) {
		modelAndView.setViewName("system/index");
		return modelAndView;
	}
	/*
	 * 登陆页面
	 */
	@RequestMapping(value = "/login",method = RequestMethod.GET)
	public ModelAndView login(ModelAndView modelAndView) {
		modelAndView.setViewName("system/login");
		return modelAndView;
	}
	/**
	 * 登录表单提交
	 * ResponseBody将数据以json的形式返回到视图
	 * @return
	 */
	@RequestMapping(value = "/login",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> login(
			@RequestParam(value="username",required=true) String username,
			@RequestParam(value="password",required=true) String password,
			@RequestParam(value="vcode",required=true) String vcode,
			@RequestParam(value="type",required=true) int type,
			HttpServletRequest request
			){
		Map<String, String> ret = new HashMap<String, String>();
		if(StringUtils.isEmpty(username)){
			ret.put("type", "error");
			ret.put("msg", "用户名不能为空!");
			return ret;
		}
		if(StringUtils.isEmpty(password)){
			ret.put("type", "error");
			ret.put("msg", "密码不能为空!");
			return ret;
		}
		if(StringUtils.isEmpty(vcode)){
			ret.put("type", "error");
			ret.put("msg", "验证码不能为空!");
			return ret;
		}
		String loginCpacha = (String)request.getSession().getAttribute("loginCpacha");
		if(StringUtils.isEmpty(loginCpacha)){
			ret.put("type", "error");
			ret.put("msg", "长时间未操作，会话已失效，请刷新后重试!");
			return ret;
		}
		if(!vcode.toUpperCase().equals(loginCpacha.toUpperCase())){
			ret.put("type", "error");
			ret.put("msg", "验证码错误!");
			return ret;
		}
		request.getSession().setAttribute("loginCpacha", null);
		if (type ==1 ) {
			//是管理员
			//从数据库中去查找用户
			User user = userService.findByUserName(username);
			if (user == null) {
				ret.put("type", "error");
				ret.put("msg", "不存在该管理员！");
				return ret;
			}
			if(!password.equals(user.getPassword())){
				ret.put("type", "error");
				ret.put("msg", "密码错误!");
				return ret;
			}
			request.getSession().setAttribute("user", user);
		}
		if (type == 2) {
			//学生
		}
		
		ret.put("type", "success");
		ret.put("msg", "登录成功!");
		return ret;
	}
	
	//点击“获取验证码的按钮进行处理”
	@RequestMapping(value = "/get_cpacha",method = RequestMethod.GET)
	public void get_cpacha(HttpServletRequest request,
			//显示多少个字符    宽  高
			@RequestParam(value = "v1",defaultValue = "4",required = false) Integer v1,
			@RequestParam(value = "w",defaultValue = "98",required = false) Integer w,
			@RequestParam(value = "h",defaultValue = "33",required = false) Integer h,
			HttpServletResponse response) {
		CpachaUtil cpachaUtil = new CpachaUtil(v1,w,h);
		String generatorVCode = cpachaUtil.generatorVCode();
		request.getSession().setAttribute("loginCpacha", generatorVCode);
		//(传进去，允许画干扰线)
		BufferedImage generatorRotateVCodeImage = cpachaUtil.generatorRotateVCodeImage(generatorVCode, true);
		try {
			//放入response.getOutputStream()  之后自动显示到页面
			ImageIO.write(generatorRotateVCodeImage, "gif", response.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
