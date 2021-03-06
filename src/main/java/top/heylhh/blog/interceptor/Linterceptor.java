package top.heylhh.blog.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import top.heylhh.blog.view.UserInfo;

/*
 *拦截器
 */
public class Linterceptor implements HandlerInterceptor {

	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
				//获取请求的地址ַ
				String url = request.getRequestURI();
				
				//  对特殊地址，直接放行
				if (url.indexOf("login")>=0 || url.indexOf("portal")>=0) {
					return true;
				}
				
				// 判断session，session存在的话，登录后台.getSession();
				HttpSession session = request.getSession();
				UserInfo userInfo = (UserInfo)session.getAttribute("userInfo");
				if (userInfo!=null) {
					//身份存在，放行
					return true;
				}
				
				//ִ 执行这里表示用户身份需要验证，跳转到登录界面 
				request.getRequestDispatcher("/WEB-INF/page/admin/login.jsp").forward(request, response);
				
				return false;
	}

}
