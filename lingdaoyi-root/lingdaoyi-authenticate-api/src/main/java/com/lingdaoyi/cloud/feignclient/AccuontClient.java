package com.lingdaoyi.cloud.feignclient;


import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("lingdaoyi-account-api")
//@RequestMapping("/lingdaoyi-account-api")
public interface AccuontClient {
	/**
	 * 通过ticket判断用户是否有效
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/account/getAccountByTicket", consumes = "application/json")
	public String getAccountByTicket(@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false) Integer clientType);

	/**
	 * 根据主键获取用户信息
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/account/getAccountById", consumes = "application/json")
	public String getAccountById(@RequestParam(value = "accountId", required = false) Long accountId);

	/**
	 * 保存用户鉴权信息
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/accountIdentity/saveIdentityInfo", consumes = "application/json")
	public String saveIdentityInfo(@RequestParam(value = "accountId", required = false) Long accountId,
			@RequestParam(value = "idcard", required = false) String idcard,
			@RequestParam(value = "realname", required = false) String realname,
			@RequestParam(value = "area", required = false) String area,
			@RequestParam(value = "sex", required = false) String sex,
			@RequestParam(value = "enkey", required = false) String enkey,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false) Integer clientType);

	/**
	 * 补全鉴权信息
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/accountIdentity/perfectIdentityInfo", consumes = "application/json")
	public String perfectIdentityInfo(@RequestParam(value = "accountId", required = false) Long accountId,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "num", required = false) String num,
			@RequestParam(value = "sex", required = false) String sex,
			@RequestParam(value = "address", required = false) String address,
			@RequestParam(value = "birth", required = false) String birth,
			@RequestParam(value = "nationality", required = false) String nationality,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "issue", required = false) String issue,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "laborUrl", required = false) String laborUrl,
			@RequestParam(value = "ticket", required = false) String ticket,
			@RequestParam(value = "clientType", required = false) Integer clientType);

	/**
	 * 修改用户的认证状态
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/accountIdentity/getAuthStatus", consumes = "application/json")
	public String getAuthStatus(@RequestParam(value = "accountId", required = false) Long accountId);
	
	/**
	 * 获取国际信息
	 * @param currencySort
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/accountCountry/getCountryInfo", consumes = "application/json")
	public String getCountryInfo(@RequestParam(value = "currencySort", required = false)String currencySort);
	/**
	 * 国旗列表
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/accountCountry/getCountryFlagList", consumes = "application/json")
	public String getCountryFlagList();
}
