package top.heylhh.blog.action.article_info;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
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
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

import top.heylhh.blog.service.article_info.ArticleInfoService;
import top.heylhh.blog.service.type_info.TypeInfoService;
import top.heylhh.blog.view.ArticleInfo;
import top.heylhh.blog.view.Result;

@Controller
@RequestMapping("article_info")
public class ArticleInfoAction {

	@Autowired
	private ArticleInfoService articleInfoService;
	@Autowired
	private TypeInfoService typeInfoService;

	/**
	 * 查询所有文章（正常）
	 * 
	 * @return
	 */
	@RequestMapping("list_normal.action")
	public String list(ModelMap map,
			@RequestParam(required = false, value = "typeId") String typeId,
			@RequestParam(required = false, value = "startDate") String startDate,
			@RequestParam(required = false, value = "endDate") String endDate,
			@RequestParam(required = false, value = "keyWord") String keyWord,
			@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(value = "pageSize", defaultValue = "5") int pageSize) {

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("typeId", typeId);
		param.put("startDate", startDate);
		param.put("endDate", endDate);
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
		
		// 查询所有文章分类
		map.put("typeList", typeInfoService.list());
		
		map.put("typeId", typeId);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("keyWord", keyWord);
		
		return "admin/article_info/list_normal";
	}
	
	
	/**
	 * 查询所有文章（回收站）
	 * @return
	 */
	@RequestMapping("list_recycle.action")
	public String listRecycle(ModelMap map,
			@RequestParam(required = false, value = "typeId") String typeId,
			@RequestParam(required = false, value = "startDate") String startDate,
			@RequestParam(required = false, value = "endDate") String endDate,
			@RequestParam(required = false, value = "keyWord") String keyWord,
			@RequestParam(value="pageNum", defaultValue="1") int pageNum,
			@RequestParam(value="pageSize", defaultValue="3") int pageSize) {
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("typeId", typeId);
		param.put("startDate", startDate);
		param.put("endDate", endDate);
		if (!StringUtils.isEmpty(keyWord)) {
			param.put("keyWord", "%" + keyWord.trim() + "%");
		}
		
		param.put("status", "0");
		
		// pageHelper分页插件
		// 只需要在查询之前调用，传入当前页码，以及每一页显示多少条
		PageHelper.startPage(pageNum, pageSize);
		List<ArticleInfo> list = articleInfoService.list(param);
		PageInfo<ArticleInfo> pageInfo = new PageInfo<ArticleInfo>(list);
		map.put("pageInfo", pageInfo);
		
		// 查询所有文章分类
		map.put("typeList", typeInfoService.list());
		
		map.put("typeId", typeId);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("keyWord", keyWord);
		
		return "admin/article_info/list_recycle";
	}
	

	/**
	 * 文章编辑
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("edit.action")
	public String edit(ModelMap map, @RequestParam(required = false, value = "id") String id) {

		// 单个文章的信息
		if (!StringUtils.isEmpty(id)) {
			ArticleInfo articleInfo = articleInfoService.selectById(id);
			map.put("articleInfo", articleInfo);
		} else {

		}
		// 查询所有文章分类
		map.put("typeList", typeInfoService.list());
		return "admin/article_info/edit";
	}

	/**
	 * 文章保存
	 * 
	 * @return
	 */
	@RequestMapping("save.json")
	@ResponseBody
	public Result save(ArticleInfo articleInfo) {

		articleInfoService.save(articleInfo);

		return Result.success();
	}
	
	
	/**
	 * 批量移动文章到某个分类
	 * @return
	 */
	@RequestMapping("move.json")
	@ResponseBody
	public Result move(
			@RequestParam(value = "idArr") String[] idArr,
			@RequestParam(value = "typeId") String typeId) {
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("idArr", idArr);
		param.put("typeId", typeId);
		
		articleInfoService.batchUpdate(param);
		
		return Result.success();
	}
	
	/**
	 * 批量更新文章的状态
	 * @return
	 */
	@RequestMapping("update_status.json")
	@ResponseBody
	public Result recycle(
			@RequestParam(value = "idArr") String[] idArr,
			@RequestParam(value = "status") String status) {
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("idArr", idArr);
		param.put("status", status);
		
		articleInfoService.batchUpdate(param);
		
		return Result.success();
	}
	
	
	/**
	 * 批量删除文章
	 * @return
	 */
	@RequestMapping("delete.json")
	@ResponseBody
	public Result delete(
			@RequestParam(value = "idArr") String[] idArr) {

		articleInfoService.batchDelete(idArr);
		
		return Result.success();
	}

	/**
	 * 上传文件到七牛云
	 * 
	 * @throws IOException
	 */
	@RequestMapping("upload.json")
	@ResponseBody
	public Result upload(MultipartFile file) throws IOException {

		/**
		 * 构造一个带指定Zone对象的配置类 华东 : Zone.zone0() 华北 : Zone.zone1() 华南 : Zone.zone2() 北美 :
		 * Zone.zoneNa0()
		 */
		Configuration cfg = new Configuration(Zone.zone0());
		// ...其他参数参考类注释
		UploadManager uploadManager = new UploadManager(cfg);
		// ...生成上传凭证，然后准备上传
		String accessKey = "WRV7MsQJ2wrjSOlogx640kxIys7ZvCNyPeD9Xmwn";
		String secretKey = "NyU6BdhO39z3nGqwF4xjKwO9DBahNPpLF0mfu6eE";
		String bucket = "mytest1";
		// 默认不指定key的情况下，以文件内容的hash值作为文件名
		String key = null;

		String imgUrl = "";
		try {
			// 数据流上传
			InputStream byteInputStream = file.getInputStream();
			Auth auth = Auth.create(accessKey, secretKey);
			String upToken = auth.uploadToken(bucket);
			try {
				Response response = uploadManager.put(byteInputStream, key, upToken, null, null);
				// 解析上传成功的结果
				DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
				System.out.println(putRet.key);
				System.out.println(putRet.hash);
				imgUrl = putRet.hash;
			} catch (QiniuException ex) {
				Response r = ex.response;
				System.err.println(r.toString());
				try {
					System.err.println(r.bodyString());
				} catch (QiniuException ex2) {
					// ignore
				}
			}
		} catch (UnsupportedEncodingException ex) {
			// ignore
		}

		return Result.success().add("imgUrl", "http://op9mul7kx.bkt.clouddn.com/" + imgUrl);
	}
}
