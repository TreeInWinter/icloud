/**
 *
 */
package com.travelzen.framework.core.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.travelzen.framework.core.common.ReturnCode;
import com.travelzen.framework.core.dict.Currency;
import com.travelzen.framework.core.dict.PayDetailPayState;
import com.travelzen.framework.core.dict.PayOrderSource;
import com.travelzen.framework.core.dict.PayType;
import com.travelzen.framework.core.exception.BizException;
import com.travelzen.framework.core.time.DateTimeUtil;

/**
 * @author shuiren
 *
 */
public class ValidationUtil {
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static Pattern pattern = Pattern.compile(EMAIL_PATTERN);
	private static Map<String, Pattern> cachedPattern = new HashMap<String, Pattern>();
	//PNR
	public static final String RULE_PNR = "^[A-Z\\d]{6}$";
	public static final String MESSAGE_PATTERN_PNR = "pnr格式不正确";
	public static final String MESSAGE_REQUIRED_PNR = "pnr不能为空";
	//票号
	public static final String RULE_TICKETNO = "^(\\d{3}-?)?\\d{10}$";
	public static final String MESSAGE_PATTERN_TICKETNO = "票号格式不正确";
	public static final String MESSAGE_REQUIRED_TICKETNO = "票号不能为空";
	//票序号
	public static final String RULE_CONSECUTIVESEQNO = "^C[1-9]\\d{0,2}$";
	public static final String MESSAGE_PATTERN_CONSECUTIVESEQNO = "票序号格式不正确";
	public static final String MESSAGE_REQUIRED_CONSECUTIVESEQNO = "票序号不能为空";
	//客户备注
	public static final String RULE_EXTERNALMEMO = "[\\s\\S]{0,2000}";
	public static final String MESSAGE_PATTERN_EXTERNALMEMO = "客户备注字数不超过2000";
	//内部备注
	public static final String RULE_INNERMEMO = "[\\s\\S]{0,2000}";
	public static final String MESSAGE_PATTERN_INNERMEMO= "内部备注字数不超过2000";
	//金额
	public static final String RULE_MONEY= "^(-?\\d+)(\\.\\d+)?$";
	public static final String MESSAGE_PATTERN_MONEY = "金额格式不正确";
	public static final String MESSAGE_REQUIRED_MONEY= "金额不能为空";
	//票面价
	public static final String RULE_AIRFARE = "^[1-9]\\d*0(\\.0+)?$";
	public static final String MESSAGE_PATTERN_AIRFARE = "票面价格式不正确";
	public static final String MESSAGE_REQUIRED_AIRFARE = "票面价不能为空";
	//税款
	public static final String RULE_TAX = "^\\d*0(\\.0+)?$";
	public static final String MESSAGE_PATTERN_TAX = "票面价格式不正确";
	public static final String MESSAGE_REQUIRED_TAX = "票面价不能为空";
	//返点
	public static final String RULE_COMMRATIO = "^\\d{1,2}(\\.\\d+)?$";
	public static final String MESSAGE_PATTERN_COMMRATIO = "返点格式不正确";
	public static final String MESSAGE_REQUIRED_COMMRATIO = "返点不能为空";
	//返款
	public static final String RULE_COMMAMOUNT = "^\\d+(\\.0+)?$";
	public static final String MESSAGE_PATTERN_COMMAMOUNT = "返款格式不正确";
	public static final String MESSAGE_REQUIRED_COMMAMOUNT = "返款不能为空";
	//附加费
	public static final String RULE_EXTRAFEE = "^\\d+(\\.\\d{1,2})?$";
	public static final String MESSAGE_PATTERN_EXTRAFEE = "附加费格式不正确";
	public static final String MESSAGE_REQUIRED_EXTRAFEE = "附加费不能为空";
	//离港机场航站楼
	public static final String RULE_DEPARTMENTAIRPORTCODETERMIMAL = "^[A-Z]{3}(\\s+[A-Z0-9*]{0,9})?$";
	public static final String MESSAGE_DEPARTMENTAIRPORTCODETERMIMAL = "离港机场格式不正确";
	public static final String MESSAGE_REQUIRED_DEPARTMENTAIRPORTCODETERMIMAL = "离港机场不为空";
	//到港机场航站楼
	public static final String RULE_ARRIVALAIRPORTCODETERMINALRULE = "^[A-Z]{3}(\\s+[A-Z0-9*]{0,9})?$";
	public static final String MESSAGE_ARRIVALAIRPORTCODETERMINALRULE = "到港机场格式不正确";
	public static final String MESSAGE_REQUIRED_ARRIVALAIRPORTCODETERMINALRULE = "到港机场不为空";
	//航司二字码+航班号校验
	public static final String RULE_AIRPLAINCODE_FLIGHTNO_RULE = "^[0-9A-Z]{2}[0-9a-zA-Z]*$";
	//离港时间和到港时间
	private static final String RULE_ARRIVAL_DEPARTURE_DATETIME = "^\\s*([\\d]{4}-[0,1]{1}[\\d]{1}-[0-3]{1}[0-9]{1}\\s+[0-2]{1}[0-9]{1}:[0-6]{1}[0-9]{1})?\\s*$";
	public static final String RULE_ARRIVAL_DATETIME = RULE_ARRIVAL_DEPARTURE_DATETIME;
	public static final String RULE_DEPARTURE_DATETIME = RULE_ARRIVAL_DEPARTURE_DATETIME;
	//退款时，退款金额为正
	public static final String MESSAGE_REFUND_AMOUNT_POSITIVE = "退款金额不能为正";
	/**
	 * 验证value是否匹配正则表达式
	 * @param regexp
	 * @param value
	 * @param validationMessage
	 * @throws BizException
	 */
	public static void validatePattern(String regexp, String value, String validationMessage) throws BizException {
		if(StringUtils.isBlank(value))
			return;
		Pattern pattern = cachedPattern.get(regexp);
		if(pattern == null){
			pattern = Pattern.compile(regexp);
			cachedPattern.put(regexp, pattern);
		}
		Matcher matcher = pattern.matcher(value);
		if(!matcher.matches())
			throw BizException.instance(ReturnCode.E_DATA_VALIDATION_ERROR, validationMessage);
	}
	/**
	 * 验证value不为空
	 * @param value
	 * @param validationMessage
	 * @throws BizException
	 */
	public static void validateRequired(String value, String validationMessage) throws BizException {
		if(StringUtils.isBlank(value))
			throw BizException.instance(ReturnCode.E_DATA_VALIDATION_ERROR, validationMessage);
	}
	/**
	 * 验证value是否匹配正则表达式
	 * @param regexp
	 * @param value
	 * @return
	 * @throws BizException
	 */
	public static boolean isValidatePattern(String regexp, String value) throws BizException {
		Pattern pattern = cachedPattern.get(regexp);
		if(pattern == null){
			pattern = Pattern.compile(regexp);
			cachedPattern.put(regexp, pattern);
		}
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}
	/***/
	public static boolean isValidEmail(final String emailAddress) {
		Matcher matcher = pattern.matcher(emailAddress);
		return matcher.matches();
	}

	/**
	 * 验证订单来源
	 *
	 * @param orderSource
	 * @throws BizException
	 */
	public static void validateOrderSource(String orderSource) throws BizException {
		orderSource = StringUtils.trim(orderSource);
		if (StringUtils.isEmpty(orderSource))
			throw BizException.instance(ReturnCode.E_DATA_VALIDATION_ERROR, "订单来源不能为空");
		for (PayOrderSource source : PayOrderSource.values())
			if (orderSource.equals(source.getValue()))
				return;
		throw BizException.instance(ReturnCode.E_DATA_VALIDATION_ERROR, "非法的订单来源");
	}

	/**
	 * 验证支付订单号
	 *
	 * @param orderNo
	 */
	public static void validatePayOrderNo(String orderNo) {
		orderNo = StringUtils.trim(orderNo);
		if (StringUtils.isEmpty(orderNo))
			throw BizException.instance(ReturnCode.E_DATA_VALIDATION_ERROR, "订单号不能为空");
		if (orderNo.length() > 18)
			throw BizException.instance(ReturnCode.E_DATA_VALIDATION_ERROR, "订单号最大长度为18位");
	}

	/**
	 * 验证币种
	 *
	 * @param currency
	 */
	public static void validateCurrency(String currency) {
		currency = StringUtils.trim(currency);
		if (StringUtils.isEmpty(currency))
			throw BizException.instance(ReturnCode.E_DATA_VALIDATION_ERROR, "币种不能为空");
		for (Currency cur : Currency.values())
			if (currency.equals(cur.toString()))
				return;
		throw BizException.instance(ReturnCode.E_DATA_VALIDATION_ERROR, "不支持该币种");
	}

	/**
	 * 验证金额
	 *
	 * @param amount
	 */
	public static void validateAmount(long amount) {
		if (amount <= 0)
			throw BizException.instance(ReturnCode.E_DATA_VALIDATION_ERROR, "金额只能为正整数");
	}

	/**
	 * 验证用户id
	 *
	 * @param customerId
	 */
	public static void validateCustomerId(String customerId) {
		customerId = StringUtils.trim(customerId);
		if (StringUtils.isEmpty(customerId))
			throw BizException.instance(ReturnCode.E_DATA_VALIDATION_ERROR, "用户标识不能为空");
		if (customerId.length() > 32)
			throw BizException.instance(ReturnCode.E_DATA_VALIDATION_ERROR, "用户标识最大长度为32位");
	}

	/**
	 * 验证加密串
	 *
	 * @param encryMsg
	 */
	public static void validateEncryMsg(String encryMsg) {
		encryMsg = StringUtils.trim(encryMsg);
		if (StringUtils.isEmpty(encryMsg))
			throw BizException.instance(ReturnCode.E_DATA_VALIDATION_ERROR, "密文不能为空");
	}

	/**
	 * 验证前台通知地址
	 *
	 * @param fgNotifyUrl
	 */
	public static void validateFgNotifyUrl(String fgNotifyUrl) {
		fgNotifyUrl = StringUtils.trim(fgNotifyUrl);
		if (StringUtils.isEmpty(fgNotifyUrl))
			throw BizException.instance(ReturnCode.E_DATA_VALIDATION_ERROR, "前台通知地址不能为空");
		if (fgNotifyUrl.length() > 2048)
			throw BizException.instance(ReturnCode.E_DATA_VALIDATION_ERROR, "前台通知地址最大长度为2048位");
	}

	/**
	 * 验证后台通知地址
	 *
	 * @param bgNotifyUrl
	 */
	public static void validateBgNotifyUrl(String bgNotifyUrl) {
		bgNotifyUrl = StringUtils.trim(bgNotifyUrl);
		if (StringUtils.isEmpty(bgNotifyUrl))
			throw BizException.instance(ReturnCode.E_DATA_VALIDATION_ERROR, "后台通知地址不能为空");
		if (bgNotifyUrl.length() > 2048)
			throw BizException.instance(ReturnCode.E_DATA_VALIDATION_ERROR, "后台通知地址最大长度为2048位");
	}

	/**
	 * 验证支付方式
	 *
	 * @param payType
	 */
	public static void validatePayType(String payType) {
		payType = StringUtils.trim(payType);
		if (StringUtils.isEmpty(payType))
			throw BizException.instance(ReturnCode.E_DATA_VALIDATION_ERROR, "支付方式不能为空");
		for (PayType type : PayType.values())
			if (payType.equals(type.toString()))
				return;
		throw BizException.instance(ReturnCode.E_DATA_VALIDATION_ERROR, "不支持该支付方式");

	}

	/**
	 * 验证支付平台标识
	 *
	 * @param gateId
	 */
	public static void validateGateId(String gateId) {
		gateId = StringUtils.trim(gateId);
		if (StringUtils.isEmpty(gateId))
			throw BizException.instance(ReturnCode.E_DATA_VALIDATION_ERROR, "支付平台标识不能为空");
		if (gateId.length() > 4)
			throw BizException.instance(ReturnCode.E_DATA_VALIDATION_ERROR, "支付平台标识的最大长度为4");
	}

	/**
	 * 验证支付状态
	 *
	 * @param payState
	 */
	public static void validatePayDetailPayState(String payState) {
		payState = StringUtils.trim(payState);
		if (StringUtils.isEmpty(payState))
			throw BizException.instance(ReturnCode.E_DATA_VALIDATION_ERROR, "支付状态不能为空");
		for (PayDetailPayState state : PayDetailPayState.values())
			if (payState.equals(state.toString()))
				return;
		throw BizException.instance(ReturnCode.E_DATA_VALIDATION_ERROR, "非法的支付状态");
	}

	/**
	 * 验证支付平台清算日期
	 *
	 * @param bankCheckDate
	 */
	public static void validateBankCheckDate(String bankCheckDate) {
		bankCheckDate = StringUtils.trim(bankCheckDate);
		if (!DateTimeUtil.validateDate8(bankCheckDate))
			throw BizException.instance(ReturnCode.E_DATA_VALIDATION_ERROR, "支付平台清算日期不合法");
	}

	/**
	 * @param pnr
	 */
	public static void validatePnr(String pnr) {
		pnr = StringUtils.trim(pnr);
		if (StringUtils.isEmpty(pnr))
			throw BizException.instance(ReturnCode.E_DATA_VALIDATION_ERROR, "pnr不能为空");
		if (pnr.length() != 6)
			throw BizException.instance(ReturnCode.E_DATA_VALIDATION_ERROR, "pnr长度必须为6位");
	}

	public static boolean isValidPnr(String pnr) {
		pnr = StringUtils.trim(pnr);
		return StringUtils.isNotBlank(pnr) && pnr.length() == 6;
	}

	/**
	 * @param officeNo
	 */
	public static void validateOfficeNo(String officeNo) {
		officeNo = StringUtils.trim(officeNo);
		if (StringUtils.isEmpty(officeNo))
			throw BizException.instance(ReturnCode.E_DATA_VALIDATION_ERROR, "officeNo不能为空");
	}

	public static void validateFlightOrderItemId(String flightOrderItemId) {
		flightOrderItemId = StringUtils.trim(flightOrderItemId);
		if (StringUtils.isEmpty(flightOrderItemId))
			throw BizException.instance(ReturnCode.E_DATA_VALIDATION_ERROR, "flightOrderItemId不能为空");
	}


	//=================Hotel module=============================
	public static void validateHotelOrderId(String hotelOrderId) {
		hotelOrderId = StringUtils.trim(hotelOrderId);
		if (StringUtils.isEmpty(hotelOrderId))
			throw BizException.instance(ReturnCode.E_DATA_VALIDATION_ERROR, "hotelOrderId不能为空");
	}

	public static boolean isNull(Object... objects){
		boolean isNull = false;
		if(objects == null || objects.length == 0){
			isNull = true;
		}
		for(Object object:objects){
			if(object == null){
				isNull = true;
				break;
			}
			if(object instanceof String){
				String objStr = (String)object;
				if(StringUtils.isBlank(objStr)){
					isNull = true;
					break;
				}
			}

			if(object instanceof Collection){
				Collection objColl = (Collection)object;
				if(objColl.isEmpty()){
					isNull = true;
					break;
				}
			}

			if(object instanceof Map){
				Map objMap = (Map)object;
				if(objMap.isEmpty()){
					isNull = true;
					break;
				}
			}
		}
		return isNull;
	}
	public static boolean isNotNull(Object... objects){
		return !isNull(objects);
	}

}
