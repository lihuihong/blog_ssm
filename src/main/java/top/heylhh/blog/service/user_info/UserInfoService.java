package top.heylhh.blog.service.user_info;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import top.heylhh.blog.dao.user_info.IUserInfoDAO;
import top.heylhh.blog.view.UserInfo;


@Service("UserInfoService")
public class UserInfoService {
	
	@Autowired
	private IUserInfoDAO iUserInfoDAO;

	/**
	 * 校验用户登录
	 * @param loginName 登录名
	 * @param passWord 登录密码
	 * @return
	 */
	public UserInfo selectUser(String loginName, String passWord) {
		
		return iUserInfoDAO.checkUser(loginName, passWord);
	}
	
}
