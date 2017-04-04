package com.lttd.freezer.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Arith {

	private static final int DEF_DIV_SCALE = 10;

	/**
	 * 　　* 两个Double数相加 　　* @param v1 　　* @param v2 　　* @return Double 　　
	 */
	public static Double add(Double v1, Double v2) {

		BigDecimal b1 = new BigDecimal(v1.toString());

		BigDecimal b2 = new BigDecimal(v2.toString());

		return b1.add(b2).doubleValue();
	}

	/**
	 * 　　* 三个Double数相加 　　* @param v1 　　* @param v2 　　* @return Double 　　
	 */
	public static Double add(Double v1, Double v2, Double v3) {

		BigDecimal b1 = new BigDecimal(add(v1, v2).toString());
		BigDecimal b2 = new BigDecimal(v3.toString());

		return b1.add(b2).doubleValue();
	}

	/**
	 * 　　* 两个Double数相减
	 * 
	 * 　　* @param v1
	 * 
	 * 　　* @param v2
	 * 
	 * 　　* @return Double
	 * 
	 * 　　
	 */
	public static Double sub(Double v1, Double v2) {

		BigDecimal b1 = new BigDecimal(v1.toString());

		BigDecimal b2 = new BigDecimal(v2.toString());

		return getNDouble(b1.subtract(b2).doubleValue(), 3);
	}

	/**
	 * 　　* 两个Double数相减 保留多位小数
	 * 
	 * 　　* @param v1
	 * 
	 * 　　* @param v2
	 * 
	 * 　　* @return Double
	 * 
	 * 　　
	 */
	public static Double sub(Double v1, Double v2, int scale) {

		BigDecimal b1 = new BigDecimal(v1.toString());

		BigDecimal b2 = new BigDecimal(v2.toString());

		return getNDouble(b1.subtract(b2).doubleValue(), scale);
	}

	/**
	 * 　　* 两个Double数相乘
	 * 
	 * 　　* @param v1
	 * 
	 * 　　* @param v2
	 * 
	 * 　　* @return Double
	 * 
	 * 　　
	 */
	public static Double mul(Double v1, Double v2) {

		BigDecimal b1 = new BigDecimal(v1.toString());

		BigDecimal b2 = new BigDecimal(v2.toString());

		return b1.multiply(b2).doubleValue();
	}

	/**
	 * 　　* 两个Double数相除
	 * 
	 * 　　* @param v1
	 * 
	 * 　　* @param v2
	 * 
	 * 　　* @return Double
	 * 
	 * 　　
	 */

	public static Double div(Double v1, Double v2) {

		BigDecimal b1 = new BigDecimal(v1.toString());

		BigDecimal b2 = new BigDecimal(v2.toString());

		return b1.divide(b2, DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 两个Double数相除，并保留scale位小数
	 * 
	 * @param v1
	 * @param v2
	 * @param scale
	 * @return Double
	 */
	public static Double div(Double v1, Double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}

		BigDecimal b1 = new BigDecimal(v1.toString());
		BigDecimal b2 = new BigDecimal(v2.toString());
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 
	 * 保留N位小数
	 * 
	 * @param doub
	 * @return
	 */
	public static double getNDouble(Double doub, int scale) {

		BigDecimal d = new BigDecimal(doub.toString());
		BigDecimal i = d.setScale(scale, RoundingMode.HALF_EVEN);
		return i.doubleValue();

	}
}
