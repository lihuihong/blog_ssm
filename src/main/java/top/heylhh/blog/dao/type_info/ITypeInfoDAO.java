package top.heylhh.blog.dao.type_info;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import top.heylhh.blog.view.TypeInfo;





public interface ITypeInfoDAO {


	/**
	 * 查询所有文章分类
	 */
	List<TypeInfo> list();

	/**
	 * 插入一条新的数据
	 * @param sort 排序
	 * @param name 分类名称
	 */
	void insert(@Param("sort")String sort, @Param("name")String name);

	/**
	 * 更新一条数据
	 * @param id 主键id
	 * @param sort 排序
	 * @param name 分类名称
	 */
	void update(@Param("id")String id, @Param("sort")String sort, @Param("name")String name);

	/**
	 * 批量删除文章分类
	 * @param idArr 主键数组
	 */
	void delete(@Param("idArr")String[] idArr);

	/**
	 * 根据主键查询文章分类
	 * @param id
	 * @return
	 */
	TypeInfo selectById(String id);


}
