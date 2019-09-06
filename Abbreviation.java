package com.db;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class Abbreviation {
	//此静态函数用于获取中文的首拼音缩写，20190821龚灿，测试成功
	public static String getAbbreviation(String chinese) throws BadHanyuPinyinOutputFormatCombination {
        char[] charArray = chinese.toCharArray();
        StringBuilder pinyin = new StringBuilder();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        // 设置为小写格式
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        // 设置声调格式
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < charArray.length; i++) {
            //匹配中文,非中文转换会转换成null
            if (Character.toString(charArray[i]).matches("[\\u4E00-\\u9FA5]+")) {
                String[] hanyuPinyinStringArray = PinyinHelper.toHanyuPinyinStringArray(charArray[i], defaultFormat);
                if (hanyuPinyinStringArray != null) {
                    pinyin.append(hanyuPinyinStringArray[0].charAt(0));
                }
            }
            //匹配数字和字母，将其在对应位置保留
            if (Character.toString(charArray[i]).matches("[A-Za-z0-9]+")) {
                pinyin.append(charArray[i]);
            }
        }
        return pinyin.toString();
	}
}

//Maven项目pom.xml添加：
//<dependency>
//	<groupId>com.belerweb</groupId>
//	<artifactId>pinyin4j</artifactId>
//	<version>2.5.0</version>
//</dependency>
