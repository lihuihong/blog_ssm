package top.heylhh.blog.dao.user_info;

import org.apache.ibatis.annotations.Param;

import top.heylhh.blog.view.UserInfo;

public interface IUserInfoDAO {


	/**
	 * 校验用户
	 * @param loginName 登录名
	 * @param passWord 登录密码
	 * @return
	 */
	UserInfo checkUser(@Param("loginName") String loginName, @Param("passWord") String passWord);

}
