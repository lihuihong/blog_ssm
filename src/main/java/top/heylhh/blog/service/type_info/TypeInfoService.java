package top.heylhh.blog.service.type_info;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import top.heylhh.blog.dao.article_info.IArticleInfoDAO;
import top.heylhh.blog.dao.type_info.ITypeInfoDAO;
import top.heylhh.blog.exception.LException;
import top.heylhh.blog.view.TypeInfo;

@Service("TypeInfoService")
public class TypeInfoService {

	@Autowired
	private ITypeInfoDAO iTypeInfoDAO;
	@Autowired
	private IArticleInfoDAO iArticleInfoDAO;

	/**
	 * 查询所有文章分类
	 * 
	 * @return
	 */
	public List<TypeInfo> list() {
		return iTypeInfoDAO.list();
	}

	/**
	 * 批量更新/插入
	 */
	public void save(String[] idArr, String[] sortArr, String[] nameArr) {
		// 遍历其中一个数组
		for (int i = 0; i < idArr.length; i++) {
			// 判断是需要插入还是需要更新
			if (StringUtils.isEmpty(idArr[i])) {
				// 插入
				iTypeInfoDAO.insert(sortArr[i], nameArr[i]);
			} else {
				// 更新
				iTypeInfoDAO.update(idArr[i], sortArr[i], nameArr[i]);
			}
		}
	}

	/**
	 * 批量删除文章分类
	 * 
	 * @param idArr
	 * @throws LException 
	 */
	public void delete(String[] idArr) throws LException {
		// 判断该分类id有没有被使用
		int nCount = iArticleInfoDAO.countByTypeIdArr(idArr, "1");
		if (nCount > 0) {
			// 被占用了，禁止删除
			throw new LException("已存在分类，无法删除");
		}

		// 先删除该分类下所有回收站的文章
		iArticleInfoDAO.batchDeleteByTypeIdArr(idArr);
		// 删除该分类
		iTypeInfoDAO.delete(idArr);
	}

	/**
	 * 根据主键查询文章分类
	 * @param typeId
	 * @return
	 */
	public TypeInfo selectById(String typeId) {
		
		return iTypeInfoDAO.selectById(typeId);
	}
}
