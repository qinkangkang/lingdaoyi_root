package com.lingdaoyi.cloud.feign;

import javax.servlet.http.HttpServletRequest;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("lingdaoyi-system-api")
public interface SystemFeignClient {

	/**
	 * 上传图片
	 * 
	 * @param request
	 * @param type
	 * @param base64
	 * @return
	 */
	@RequestMapping(value = "/system/imageUploadDG")
	public String imageUploadDG(@RequestParam(value = "type") String type, @RequestParam(value = "file") String base64);

}
