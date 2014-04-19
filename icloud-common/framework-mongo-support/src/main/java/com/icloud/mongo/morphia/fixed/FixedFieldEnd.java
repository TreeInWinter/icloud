package com.icloud.mongo.morphia.fixed;

import java.util.regex.Pattern;

import com.github.jmkgreen.morphia.query.CriteriaContainerImpl;
import com.github.jmkgreen.morphia.query.FieldEndImpl;
import com.github.jmkgreen.morphia.query.QueryImpl;
import com.github.jmkgreen.morphia.utils.Assert;

/**
 * 这个类特意用来解决有些查询方法不支持正则表达式敏感字符的问题
 * 
 * @author chengwenlee
 */
public class FixedFieldEnd <T extends CriteriaContainerImpl> extends FieldEndImpl<T> {

	public FixedFieldEnd(QueryImpl<?> query, String field, T target, boolean validateName) {
		super(query, field, target, validateName);
	}

    public T startsWith(String prefix) {
        Assert.parametersNotNull("val", prefix);
        return super.startsWith(Pattern.quote(prefix)); // 解决不支持正则表达式敏感字符的问题
    }

    public T startsWithIgnoreCase(String prefix) {
        Assert.parametersNotNull("val", prefix);
        return super.startsWithIgnoreCase(Pattern.quote(prefix)); // 解决不支持正则表达式敏感字符的问题
    }

    public T endsWith(String suffix) {
        Assert.parametersNotNull("val", suffix);
        return super.endsWith(Pattern.quote(suffix)); // 解决不支持正则表达式敏感字符的问题
    }

    public T endsWithIgnoreCase(String suffix) {
        Assert.parametersNotNull("val", suffix);
        return super.endsWithIgnoreCase(Pattern.quote(suffix)); // 解决不支持正则表达式敏感字符的问题
    }

    public T contains(String string) {
        Assert.parametersNotNull("val", string);
        return super.contains(Pattern.quote(string)); // 解决不支持正则表达式敏感字符的问题
    }

    public T containsIgnoreCase(String string) {
        Assert.parametersNotNull("val", string);
        return super.containsIgnoreCase(Pattern.quote(string)); // 解决不支持正则表达式敏感字符的问题
    }
	
}
