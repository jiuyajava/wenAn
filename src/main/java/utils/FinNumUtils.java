package utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lohocc
 * @Date 2016年2月29日上午10:45:36
 */
public class FinNumUtils {
	public static final int HUNDRED_SCALE = 100;

	public static final int TEN_THOUSAND_SCALE = 10000;

	public static final int BILLION_SCALE = 1000000000;

	public static final String DB_SCALE = "10000";

	/**
	 * 数据库转换：DBLong转换为BigDecimal并保留两位小数
	 */
	public static BigDecimal to2ScaleBigDecimal(Long from){
		if(from == null){
			return new BigDecimal("0.00");
		}
		return new BigDecimal(from).divide(new BigDecimal(DB_SCALE)).setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * 数据库转换：DBLong转换为BigDecimal并保留四位小数
	 */
	public static BigDecimal to4ScaleBigDecimal(Long from){
		if(from == null){
			return new BigDecimal("0.0000");
		}
		return new BigDecimal(from).divide(new BigDecimal(DB_SCALE)).setScale(4, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * 数据库转换：BigDecimal转换为DBLong
	 */
	public static long toDBLong(BigDecimal from){
		if(from == null){
			return 0;
		}
		return from.multiply(new BigDecimal(DB_SCALE)).longValue();
	}

	/**
	 * 导出使用：把BigDecimal的金额转换为保留2位小数的double
	 * @return 
	 */
	public static Double transMoneyForExport(BigDecimal money){
		if(money==null){
			return 0.00;
		}
		DecimalFormat df = new DecimalFormat("###########0.00");
		return  Double.valueOf(df.format(money)) ;
	}
	
	/**
	 * 前台展示使用：DBLong转保留两位小数的String
	 */
	public static String toShowStrFromDBLong(Long from){
		if(from == null){
			from = 0L;
		}
		DecimalFormat decimal=new DecimalFormat("0.00");
		return decimal.format(div(new BigDecimal(from), new BigDecimal(DB_SCALE)));
	}
	
	/**
	 * 特定场景使用：将最后入库的DBLong四舍五入 保留两位精度
	 */
	public static long to2ScaleDBLong(long from){
		BigDecimal scaleBigDecimal = to2Sacle(to2ScaleBigDecimal(from));
		return toDBLong(scaleBigDecimal);
	}
	
	/**
	 * 四舍五入 保留两位小数；传null返回0
	 */
	public static BigDecimal to2Sacle(BigDecimal from){
		if(from == null){
			return  new BigDecimal("0.00");
		}
		return from.setScale(2, BigDecimal.ROUND_HALF_UP);
	}
	
	public static BigDecimal to2SacleDown(BigDecimal from){
		if(from == null){
			return  new BigDecimal("0.00");
		}
		return from.setScale(2, BigDecimal.ROUND_DOWN);
	}

	/**
	 * 四舍五入 保留六位小数；传null返回0
	 */
	public static BigDecimal to6Sacle(BigDecimal from){
		if(from == null){
			return  new BigDecimal("0.00");
		}
		return from.setScale(6, BigDecimal.ROUND_HALF_EVEN);
	}

	/** 
	 * 提供（相对）精确的除法运算 
	 * @return 两个参数的商
	 */  
	public static BigDecimal div(BigDecimal b1, BigDecimal b2) {  
		return b1.divide(b2);  
	}  

	/** 
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入。 
	 * @param scale 表示表示需要精确到小数点以后几位。
	 * @return 两个参数的商 
	 */  
	public static BigDecimal div(BigDecimal b1, BigDecimal b2, int scale) {
		return b1.divide(b2, scale);
	}

	private static final Pattern PATTERN_INT = Pattern.compile("[0-9]*");

	/**
	 * 判断是否是数字
	 */
	public static boolean isInteger(String stringValue) {
		Matcher isNum = PATTERN_INT.matcher(stringValue);
		if( !isNum.matches() ){
			return false; 
		} 
		return true; 
	}

	private static final Pattern PATTERN = Pattern.compile("[0-9]+([.]{1}[0-9]{1,4})?$");

	/**
	 * 判断是否是数字且小数不能超过四位
	 */
	public static boolean isExchangeRate(BigDecimal b) {
		if(b == null){
			return false;
		}
		String stringValue = b.toPlainString();

		Matcher isNum = PATTERN.matcher(stringValue);
		if( !isNum.matches() ){
			return false;
		}
		return true;
	}

	/**
	 * 首字母转大写
	 */
	public static String toUpperCaseFirstOne(String s) {
		if (Character.isUpperCase(s.charAt(0))) {
			return s;
		} else {
			return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
		}
	}
	
	
	public static class CNUtil {
		/**
		 * 汉语中数字大写
		 */
		private static final String[] CN_UPPER_NUMBER = { "零", "壹", "贰", "叁", "肆","伍", "陆", "柒", "捌", "玖"};
		/**
		 * 汉语中货币单位大写，这样的设计类似于占位符
		 */
		private static final String[] CN_UPPER_MONETRAY_UNIT = { "分", "角", "元","拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "兆", "拾","佰", "仟" };
		/**
		 * 特殊字符：整
		 */
		private static final String CN_FULL = "整";
		/**
		 * 特殊字符：负
		 */
		private static final String CN_NEGATIVE = "负";
		/**
		 * 金额的精度，默认值为2
		 */
		private static final int MONEY_PRECISION = 2;
		/**
		 * 特殊字符：零元整
		 */
		private static final String CN_ZEOR_FULL = "零元" + CN_FULL;
		/**
		 * 把输入的金额转换为汉语中人民币的大写
		 */
		public static String toCN(BigDecimal numberOfMoney) {
			StringBuffer sb = new StringBuffer();
			int signum = numberOfMoney.signum();
			// 零元整的情况
			if (signum == 0) {
				return CN_ZEOR_FULL;
			}
			//这里会进行金额的四舍五入
			long number = numberOfMoney.movePointRight(MONEY_PRECISION)
					.setScale(0, 4).abs().longValue();
			// 得到小数点后两位值
			long scale = number % 100;
			int numUnit = 0;
			int numIndex = 0;
			boolean getZero = false;
			// 判断最后两位数，一共有四中情况：00 = 0, 01 = 1, 10, 11
			if (!(scale > 0)) {
				numIndex = 2;
				number = number / 100;
				getZero = true;
			}
			if ((scale > 0) && (!(scale % 10 > 0))) {
				numIndex = 1;
				number = number / 10;
				getZero = true;
			}
			int zeroSize = 0;
			while (true) {
				if (number <= 0) {
					break;
				}
				// 每次获取到最后一个数
				numUnit = (int) (number % 10);
				if (numUnit > 0) {
					if ((numIndex == 9) && (zeroSize >= 3)) {
						sb.insert(0, CN_UPPER_MONETRAY_UNIT[6]);
					}
					if ((numIndex == 13) && (zeroSize >= 3)) {
						sb.insert(0, CN_UPPER_MONETRAY_UNIT[10]);
					}
					sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
					sb.insert(0, CN_UPPER_NUMBER[numUnit]);
					getZero = false;
					zeroSize = 0;
				} else {
					++zeroSize;
					if (!(getZero)) {
						sb.insert(0, CN_UPPER_NUMBER[numUnit]);
					}
					if (numIndex == 2) {
						if (number > 0) {
							sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
						}
					} else if (((numIndex - 2) % 4 == 0) && (number % 1000 > 0)) {
						sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
					}
					getZero = true;
				}
				// 让number每次都去掉最后一个数
				number = number / 10;	
				++numIndex;
			}
			// 如果signum == -1，则说明输入的数字为负数，就在最前面追加特殊字符：负
			if (signum == -1) {
				sb.insert(0, CN_NEGATIVE);
			}
			// 输入的数字小数点后两位为"00"的情况，则要在最后追加特殊字符：整
			if (!(scale > 0)) {
				sb.append(CN_FULL);
			}
			return sb.toString();
		}
	}

	/**
	 * 用含税价格和税率，计算未税价格
	 * @param amountWithTax
	 * @param taxRate
	 * @return
	 */
	public static BigDecimal getAmountWithoutTax(BigDecimal amountWithTax, BigDecimal taxRate){
		amountWithTax=getNullSafeBigDecimal(amountWithTax);
		taxRate=getNullSafeBigDecimal(taxRate);
		return amountWithTax.divide(taxRate.add(BigDecimal.ONE), 6, RoundingMode.HALF_UP);
	}
	/**
	 * 如果数值为空则返回0，否则返回此数值
	 * @param decimal
	 * @return
	 */
	public static BigDecimal getNullSafeBigDecimal(BigDecimal decimal){
		return getNullSafeBigDecimal(decimal, BigDecimal.ZERO);
	}

	/**
	 * 如果数值为空则返回0，否则返回此数值
	 * @param decimal
	 * @return
	 */
	public static BigDecimal getNullSafeBigDecimal(BigDecimal decimal, BigDecimal defaultValue){
		if(decimal==null){
			return defaultValue;
		}
		return decimal;
	}
}
