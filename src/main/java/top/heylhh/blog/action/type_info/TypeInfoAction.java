package top.heylhh.blog.action.type_info;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import top.heylhh.blog.exception.LException;
import top.heylhh.blog.service.type_info.TypeInfoService;
import top.heylhh.blog.view.Result;
import top.heylhh.blog.view.TypeInfo;

@Controller
@RequestMapping("type_info")
public class TypeInfoAction {
	
	@Autowired
	private TypeInfoService typeInfoService;
	
	/*
	 * 查询所有文章分类
	 */
	@RequestMapping("list.action")
	public String list(ModelMap map){
		List<TypeInfo> list = typeInfoService.list();
		map.put("list", list);
		
		return "admin/type_info/list";
	}
	
	@RequestMapping("save.json")
	@ResponseBody
	public Result save(@RequestParam(value="idArr") String[] idArr,
			@RequestParam(value="sortArr") String[] sortArr,
			@RequestParam(value="nameArr") String[] nameArr) {
		typeInfoService.save(idArr,sortArr,nameArr);
		
		return Result.success();
	}
	
	@RequestMapping("delete.json")
	@ResponseBody
	public Result delete(@RequestParam(value="idArr") String[] idArr) throws LException{
		typeInfoService.delete(idArr);
		return Result.success();
	}
	

}
