package com.lingdaoyi.cloud.utils;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springside.modules.mapper.JsonMapper;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JavaType;
import com.google.common.collect.Maps;
import com.lingdaoyi.cloud.dto.DictionaryDTO;
import com.lingdaoyi.cloud.utils.init.Constant;
import com.lingdaoyi.cloud.utils.redis.RedisMoudel;
import com.lingdaoyi.cloud.utils.redis.RedisUtils;

public class DictionaryUtil {

	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);

	// 订单状态
	public static final long OrderStatus = 1;

	// 支付方式
	public static final long PayType = 2;

	// 转账类型
	public static final long TransferType = 3;
	
	//支付状态
	public static final long payStatus = 4;

	// 收入
	public static final long Income = 5;
	
	//支持转账的国家名称
	public  static final long SupportCounty = 6;

	// 字典数据缓存容器
	private static Map<Long, Map<String, Map<Integer, String>>> statusClassMap = Maps.newHashMap();

	// name与code对应的数据字典缓存容器
	private static Map<Long, Map<String, String>> nameCodeMap = Maps.newHashMap();

	public static String getString(Long classId, Integer value) {
		if (value == null) {
			return StringUtils.EMPTY;
		}
		return getString(classId, value, Constant.defaultLanguage);
	}

	public static String getString(Long classId, Integer value, String language) {
		if (value == null) {
			return StringUtils.EMPTY;
		}
		if(statusClassMap==null || statusClassMap.size()<1){
			DictionaryUtil.initDictionary();
		}
		Map<String, Map<Integer, String>> codeMainMap = statusClassMap.get(classId);
		Map<Integer, String> codeMap = codeMainMap.get(language);
		return StringUtils.trimToEmpty(codeMap.get(value));
	}

	public static String getCode(Long classId, Integer value) {
		if (value == null) {
			return StringUtils.EMPTY;
		}
		return getCode(classId, value, Constant.englishLanguage);
	}

	public static String getCode(Long classId, Integer value, String language) {
		if (value == null) {
			return StringUtils.EMPTY;
		}
		if(statusClassMap==null || statusClassMap.size()<1){
			DictionaryUtil.initDictionary();
		}
		Map<String, Map<Integer, String>> codeMainMap = statusClassMap.get(classId);
		Map<Integer, String> codeMap = codeMainMap.get(language);
		return StringUtils.trimToEmpty(codeMap.get(value));
	}

	public static Map<Integer, String> getStatueMap(Long classId) {
		return getStatueMap(classId, Constant.defaultLanguage);
	}

	public static Map<Integer, String> getStatueMap(Long classId, String language) {
		if (StringUtils.isBlank(language)) {
			language = Constant.defaultLanguage;
		}
		if(statusClassMap==null || statusClassMap.size()<1){
			DictionaryUtil.initDictionary();
		}
		Map<String, Map<Integer, String>> codeMap = statusClassMap.get(classId);
		return codeMap.get(language);
	}

	public static String getName(Long classId, String code) {
		if (StringUtils.isBlank(code)) {
			return StringUtils.EMPTY;
		}
		return getNameCodeMap().get(classId).get(code);
	}

	public static Map<Long, Map<String, String>> getNameCodeMap() {
		if(nameCodeMap==null || nameCodeMap.size()<1){
			DictionaryUtil.initDictionary();
		}
		return nameCodeMap;
	}

	public static Map<Long, Map<String, Map<Integer, String>>> getCodeCalssMap() {
		if(statusClassMap==null || statusClassMap.size()<1){
			DictionaryUtil.initDictionary();
		}
		return statusClassMap;
	}

	public static void setCodeCalssMap(Map<Long, Map<String, Map<Integer, String>>> codeCalssMap,
			Map<Long, Map<String, String>> codeMap) {
		statusClassMap = codeCalssMap;
		nameCodeMap = codeMap;
	}

	public static void clear() {
		statusClassMap = null;
		nameCodeMap = null;
	}

	public static void initDictionary() {
		DictionaryUtil.clear();
		Map<Long, DictionaryDTO> classIdMap = Maps.newTreeMap();
		Map<Long, Map<String, Map<Integer, String>>> dictionaryCalssMap = Maps.newHashMap();
		Map<Long, Map<String, String>> nameCodeMap = Maps.newHashMap();
		String secondDictonary = "";
		try {
			secondDictonary = RedisUtils.getValue("secondDictonary", RedisMoudel.Dictionary);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JavaType jt = mapper.contructCollectionType(List.class, DictionaryDTO.class);
		List<DictionaryDTO> list = mapper.fromJson(secondDictonary, jt);

		Map<String, Map<Integer, String>> dictionaryMap = null;
		Map<Integer, String> dictionaryCnMap = null;
		Map<Integer, String> dictionaryEnMap = null;
		Map<String, String> dictionaryNameMap = null;
		long tempId = 0;
		
		for (DictionaryDTO tDictionary : list) {
			if(!classIdMap.containsKey(tDictionary.getClassId())){
				classIdMap.put(tDictionary.getClassId(), tDictionary);
			}
		}
		
		for (Map.Entry<Long, DictionaryDTO> entry : classIdMap.entrySet()) {
			
			dictionaryMap = Maps.newHashMap();
			dictionaryCnMap = Maps.newTreeMap();
			dictionaryEnMap = Maps.newTreeMap();
			dictionaryNameMap = Maps.newTreeMap();
			for (DictionaryDTO tDictionary : list) {
				if(tDictionary.getClassId()==entry.getKey()){
					dictionaryCnMap.put(tDictionary.getValue(), tDictionary.getName());
					dictionaryEnMap.put(tDictionary.getValue(), tDictionary.getCode());
					
					if (tDictionary.getCode() != null) {
						dictionaryNameMap.put(tDictionary.getCode(), tDictionary.getName());
					}
				}
			}
			dictionaryMap.put("zh_CN", dictionaryCnMap);
			dictionaryMap.put("en_US", dictionaryEnMap);
			dictionaryCalssMap.put(entry.getKey(), dictionaryMap);
			nameCodeMap.put(entry.getKey(), dictionaryNameMap);
		}


		if (!CollectionUtils.isEmpty(list)) {
			DictionaryUtil.setCodeCalssMap(dictionaryCalssMap, nameCodeMap);
		}
	}
}