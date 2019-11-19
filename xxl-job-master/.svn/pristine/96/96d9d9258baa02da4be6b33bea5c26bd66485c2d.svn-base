package com.ddfc.util;

import org.apache.commons.lang3.StringUtils;

public class DoubleUtils {
	/**
	 * 乘法计算
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static Double multi(Double v1, Double v2) {
		if (v1 == null || v2 == null) {
			return 0.0;
		} else {
			return v1 * v2;
		}
	}

	/**
	 * 美元换算人民币
	 * 
	 * @param usd
	 * @param rate
	 * @return
	 */
	public static Double transToCny(Double usd, Double rate) {
		if (usd == null) {
			return null;
		} else {
			// 汇率默认6.6
			rate = rate == null ? 6.6 : rate;
			return usd * rate;
		}
	}

	/**
	 * 字符串转换成Double
	 * 
	 * @param v
	 * @return
	 */
	public static Double getDoubleFromString(String v) {
		if (StringUtils.isBlank(v)) {
			return null;
		} else {
			try {
				Double d = Double.parseDouble(v);
				return d;
			} catch (NumberFormatException e) {
				return 0.0;
			}
		}
	}
}
