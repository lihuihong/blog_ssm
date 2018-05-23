package top.heylhh.blog.action.protal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import top.heylhh.blog.service.article_info.ArticleInfoService;
import top.heylhh.blog.service.type_info.TypeInfoService;
import top.heylhh.blog.view.ArticleInfo;
import top.heylhh.blog.view.Result;
import top.heylhh.blog.view.TypeInfo;

@Controller
@RequestMapping("portal")
public class ProtalAction {

	@Autowired
	private ArticleInfoService articleInfoService;
	@Autowired
	private TypeInfoService typeInfoService;

	/**
	 * 查询所有文章（正常）
	 * 
	 * @return
	 */
	@RequestMapping("index.action")
	public String list(ModelMap map, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(value = "pageSize", defaultValue = "4") int pageSize) {

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("status", "1");

		// pageHelper分页插件
		// 只需要在查询之前调用，传入当前页码，以及每一页显示多少条
		PageHelper.startPage(pageNum, pageSize);
		List<ArticleInfo> list = articleInfoService.list(param);
		PageInfo<ArticleInfo> pageInfo = new PageInfo<ArticleInfo>(list);
		map.put("pageInfo", pageInfo);

		// 查询所有文章分类
		map.put("typeList", typeInfoService.list());

		return "portal/index";
	}

	/**
	 * 页面一加载，就向后台请求文章分类的数据
	 * 
	 * @return
	 */
	@RequestMapping("get_type.json")
	@ResponseBody
	public Result getType(ArticleInfo articleInfo) {

		List<TypeInfo> typeList = typeInfoService.list();

		return Result.success().add("typeList", typeList);
	}

	/**
	 * 根据文章分类查询所有文章
	 * 
	 * @return
	 */
	@RequestMapping("type.action")
	public String type(ModelMap map, @RequestParam(value = "typeId") String typeId,
			@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(value = "pageSize", defaultValue = "4") int pageSize) {

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("typeId", typeId);
		param.put("status", "1");

		// pageHelper分页插件
		// 只需要在查询之前调用，传入当前页码，以及每一页显示多少条
		PageHelper.startPage(pageNum, pageSize);
		List<ArticleInfo> list = articleInfoService.list(param);
		if (list.size() == 0) {
			return "portal/404";
		}
		PageInfo<ArticleInfo> pageInfo = new PageInfo<ArticleInfo>(list);
		map.put("pageInfo", pageInfo);

		// 查询所有文章分类
		map.put("typeInfo", typeInfoService.selectById(typeId));

		return "portal/type";
	}
	
	/**
	 * 根据根据主键查询文章详情
	 * 
	 * @return
	 */
	@RequestMapping("article.action")
	public String article(ModelMap map, @RequestParam(value = "id") String id) {

		
		ArticleInfo articleInfo = articleInfoService.selectById(id);
		if (articleInfo == null) {
			return "portal/404";
		}
		map.put("articleInfo", articleInfo);
		return "portal/article";
	}
	
	/**
	 * 关于我的
	 * 
	 * @return
	 */
	@RequestMapping("about.action")
	public String about(ModelMap map) {
		return "portal/about";
	}
	
	
	/**
	 * 搜索文章
	 * 
	 * @return
	 */
	@RequestMapping("search.action")
	public String search(ModelMap map,
			@RequestParam(value = "keyWord") String keyWord,
			@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(value = "pageSize", defaultValue = "4") int pageSize) {

		Map<String, Object> param = new HashMap<String, Object>();
		if (!StringUtils.isEmpty(keyWord)) {
			param.put("keyWord", "%" + keyWord.trim() + "%");
		}
		param.put("status", "1");

		// pageHelper分页插件
		// 只需要在查询之前调用，传入当前页码，以及每一页显示多少条
		PageHelper.startPage(pageNum, pageSize);
		List<ArticleInfo> list = articleInfoService.list(param);
		PageInfo<ArticleInfo> pageInfo = new PageInfo<ArticleInfo>(list);
		map.put("pageInfo", pageInfo);

		map.put("keyWord", keyWord);
		return "portal/search";
	}
}
