package com.ischoolbar.programmer.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ischoolbar.programmer.entity.User;
import com.ischoolbar.programmer.page.Page;
import com.ischoolbar.programmer.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService UserService;
	
	/*
	 * 用户管理列表页面
	 */
	@RequestMapping(value = "/list",method = RequestMethod.GET)
	public ModelAndView list(ModelAndView modelAndView) {
		modelAndView.setViewName("user/user_list");
		return modelAndView;
	}
	
	/*
	 * 显示用户列表
	 * 有分页的需求   单独一个类
	 * @ResponseBody序列化 返回数据json
	 */
	@RequestMapping(value = "/get_list",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getList(
			@RequestParam(value="username",required=false,defaultValue="") String username,
			Page page
			){
		Map<String, Object> ret = new HashMap<String, Object>();
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("username", "%"+username+"%");
		queryMap.put("offset", page.getOffset());
		queryMap.put("pageSize", page.getRows());
		ret.put("rows", UserService.get_list(queryMap));
		ret.put("total", UserService.getTotal(queryMap));
		return ret;
	}
	
	/*
	 * 添加
	 * @ResponseBody序列化 返回数据json
	 * User user对于形参   因为在实体类中有注解  自动放入容器中，所以会将页面的数据放入其中
	 */
	@RequestMapping(value = "/add",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> add(User user) {
		Map<String, String> ret = new HashMap<String, String>();
		
		if (user == null) {
			ret.put("type", "erroe");
			ret.put("msg", "数据绑定出错，请联系开发人员！");
			return ret;
		}
		if (StringUtils.isEmpty(user.getUsername())) {
			ret.put("type", "error");
			ret.put("msg", "用户名不能为空！");
			return ret;
		}
		if (StringUtils.isEmpty(user.getPassword())) {
			ret.put("type", "error");
			ret.put("msg", "密码不能为空！");
			return ret;
		}
		//判断是否重复
		User findByUserName = UserService.findByUserName(user.getUsername());
		if (findByUserName!=null) {
			ret.put("type", "error");
			ret.put("msg", "用户名重复！");
			return ret;
		}
		//用户添加失败
		int addUser = UserService.addUser(user);
		if (addUser<=0) {
			ret.put("type", "error");
			ret.put("msg", "添加失败！");
			return ret;
		}
		
		ret.put("type", "success");
		ret.put("msg", "添加成功！");
		return ret;
	}
	
	/*
	 * 删除
	 */
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> delete(
			@RequestParam(value="ids[]",required=true) Long[] ids
		){
		Map<String, String> ret = new HashMap<String, String>();
		if(ids == null){
			ret.put("type", "error");
			ret.put("msg", "不存在!");
			return ret;
		}
		String idsString = "";
		for(Long id:ids){
			idsString += id + ",";
		}
		idsString = idsString.substring(0,idsString.length()-1);
		if(UserService.delete(idsString) <= 0){
			ret.put("type", "error");
			ret.put("msg", "ɾ��ʧ��!");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "删除成功!");
		return ret;
	}
	
	/**
	 * 修改
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> edit(User user){
		Map<String, String> ret = new HashMap<String, String>();
		if(user == null){
			ret.put("type", "error");
			ret.put("msg", "不存在");
			return ret;
		}
		if(StringUtils.isEmpty(user.getUsername())){
			ret.put("type", "error");
			ret.put("msg", "账号为空");
			return ret;
		}
		if(StringUtils.isEmpty(user.getPassword())){
			ret.put("type", "error");
			ret.put("msg", "密码为空！");
			return ret;
		}
		User existUser = UserService.findByUserName(user.getUsername());
		if(existUser != null){
			if(user.getId() != existUser.getId()){
				ret.put("type", "error");
				ret.put("msg", "���û����Ѿ�����!");
				return ret;
			}
			
		}
		if(UserService.edit(user) <= 0){
			ret.put("type", "error");
			ret.put("msg", "修改失败");
			return ret;
		}
		ret.put("type", "success");
		ret.put("msg", "修改成功");
		return ret;
	}
	
}
