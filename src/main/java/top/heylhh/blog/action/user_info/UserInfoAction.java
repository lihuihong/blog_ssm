package top.heylhh.blog.action.user_info;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import top.heylhh.blog.exception.LException;
import top.heylhh.blog.service.user_info.UserInfoService;
import top.heylhh.blog.view.Result;
import top.heylhh.blog.view.UserInfo;

@Controller
@RequestMapping("admin")
public class UserInfoAction {

	@Autowired
	private UserInfoService userInfoService;
	
	/**
	 * 首页
	 */
	@RequestMapping("index.action")
	public String index() {
		return "admin/index";
	}
	/**
	 * 跳转到登陆页面
	 */
	@RequestMapping("login.action")
	public String login() {
		return "admin/login";
	}
	
	/**
	 * 登陆验证
	 * @param map
	 * @param request
	 * @return
	 * @throws LException
	 */
	@RequestMapping("login.json")
	@ResponseBody
	public Result loginAll(ModelMap map,HttpServletRequest request) throws LException {
		// 获取参数
		String loginName = request.getParameter("login_name");
		String passWord = request.getParameter("pass_word");
		
		// 校验参数
		// 判断参数是否为空
		if (StringUtils.isEmpty(loginName) || StringUtils.isEmpty(passWord)) {
			throw new LException("用户名、密码不能为空");
		}
		//  判断用户名密码是否正确
		UserInfo userInfo = userInfoService.selectUser(loginName, passWord);
		if (userInfo==null) {
			throw new LException("用户名或密码不正确");
		}
		
		// 设置session
		request.getSession().setAttribute("userInfo", userInfo);
		
		return Result.success();
	}
	@RequestMapping("login_out.action")
	public String loginOut(HttpSession session){
		//销毁session
		session.invalidate();
		return "admin/login";
	}

}