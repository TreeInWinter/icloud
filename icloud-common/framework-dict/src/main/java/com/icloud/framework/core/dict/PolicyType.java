package com.icloud.framework.core.dict;

import org.apache.commons.lang3.StringUtils;

public enum PolicyType {
	D_PUTONG("普通", false,true, "PUTONG"), // 国内-普通
	D_QUANGUO("全国", false,true, "QUANGUO"), // 国内-全国
	D_TESHUGAOFAN("特殊高返", false,true, "TESHUGAOFAN"), // 国内-特殊高返
	D_WANGFANDABAO("往返打包", true,true, "WANGFANDABAO"), // 国内-往返打包
	D_TECANG("特舱", false,true, "TECANG"), // 国内-特舱
	D_TUANDUI("团队", true,true, "TUANDUI"), // 国内-团队
	D_TEJIA("特价", true, true, "TEJIA"), // 国内-特价
	I_PUTONG("普通", false, false, "PUTONG"), // 国际-普通
	I_TEJIA("特价", false, false, "TEJIA"), // 国际-特价
	;
	private String desc;
	private boolean forcePriceApply;
	private boolean isDomestic;
	private String outPolicyType;

	private PolicyType(String desc, boolean forcePriceApply, boolean isDomestic, String outPolicyType) {
		this.desc = desc;
		this.forcePriceApply = forcePriceApply;
		this.isDomestic = isDomestic;
		this.outPolicyType = outPolicyType;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public boolean isForcePriceApply() {
		return forcePriceApply;
	}

	public void setForcePriceApply(boolean forcePriceApply) {
		this.forcePriceApply = forcePriceApply;
	}

	public static PolicyType valueOf(boolean isDomestic, String outPolicyType) {
		outPolicyType = StringUtils.trimToEmpty(outPolicyType);
		for(PolicyType policyType : PolicyType.values()){
			if(policyType.isDomestic == isDomestic && policyType.outPolicyType.equals(outPolicyType))
				return policyType;
		}
		return null;
	}
}
