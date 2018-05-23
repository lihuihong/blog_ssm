package top.heylhh.blog.service.article_info;



import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import top.heylhh.blog.dao.article_info.IArticleInfoDAO;
import top.heylhh.blog.view.ArticleInfo;

@Service("ArticleInfoService")
public class ArticleInfoService {
	
	@Autowired
	private IArticleInfoDAO iArticleInfoDAO;
	
	/**
	 * 查询所有文章
	 * @param param
	 * @return
	 */
	public List<ArticleInfo> list(Map<String, Object> param) {
		return iArticleInfoDAO.list(param);
	}

	/**
	 * 查询单个文章的信息
	 * @param id
	 * @return
	 */
	public ArticleInfo selectById(String id) {
		ArticleInfo	articleInfo = iArticleInfoDAO.selectById(id);
		if (articleInfo != null) {
			//获取当前的浏览量 
			int nViewCount = articleInfo.getViewCount();
			//浏览量自增
			nViewCount ++;
			iArticleInfoDAO.updateViewCount(id,nViewCount);
		}
		return articleInfo;
	}

	public void save(ArticleInfo articleInfo) {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = formatter.format(currentTime);
		
		// 判断是新增还是更新
		if (StringUtils.isEmpty(articleInfo.getId())) {
			// 新增
			articleInfo.setStatus(1);
			articleInfo.setUpdateTime(now);
			articleInfo.setViewCount(1);
			
			iArticleInfoDAO.insert(articleInfo);
		} else {
			// 更新
			articleInfo.setUpdateTime(now);
			
			iArticleInfoDAO.update(articleInfo);
		}
		
	}

	/**
	 * 批量移动文章到某个分类
	 * @param param
	 */
	public void batchUpdate(Map<String, Object> param) {
		iArticleInfoDAO.batchUpdate(param);
	}

	/**
	 * 批量删除文章
	 * @param idArr
	 */
	public void batchDelete(String[] idArr) {
		iArticleInfoDAO.batchDelete(idArr);
	}
	

}
