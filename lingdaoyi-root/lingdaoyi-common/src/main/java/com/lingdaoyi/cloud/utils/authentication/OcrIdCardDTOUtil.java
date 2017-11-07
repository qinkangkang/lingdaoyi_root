package com.lingdaoyi.cloud.utils.authentication;

import com.lingdaoyi.cloud.dto.OcrIdCardDTO;
import com.lingdaoyi.cloud.dto.OcrIdCardDTO.Outputs;
import com.lingdaoyi.cloud.dto.OcrIdCardDTO.Outputs.OutputValue;

/**
 * ocrIdCard获取数据工具类
 * 
 * @author jack
 *
 */
public class OcrIdCardDTOUtil {
	/**
	 * 获取ocrIdCard返回数据的主要数据 用此方法未加任何判断，因此可能会出现异常
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public static String getDataValue(OcrIdCardDTO dto) throws Exception {

		Outputs[] outputs = dto.getOutputs();

		OutputValue outputValue = outputs[0].getOutputValue();

		String dataValue = outputValue.getDataValue();

		return dataValue;
	}
}
