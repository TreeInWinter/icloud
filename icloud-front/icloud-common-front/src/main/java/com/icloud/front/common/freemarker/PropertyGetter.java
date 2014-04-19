package com.icloud.front.common.freemarker;

import java.util.List;

import com.icloud.framework.core.util.TZUtil;
import com.icloud.front.common.utils.WebEnv;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

/**
 * 从 properties/web-env.properties 文件获取值的Freemarker方法
 *
 * @author jiangning.cui
 */
public class PropertyGetter implements TemplateMethodModel {

	@SuppressWarnings("rawtypes")
	@Override
	public Object exec(List args) throws TemplateModelException {
		if (TZUtil.isEmpty(args)) {
			return null;
		}
		Object key = args.get(0);
		if (key == null) {
			return null;
		}
		return WebEnv.get(key.toString(), "");
	}

}
