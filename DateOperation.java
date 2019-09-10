package com.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

//此类用于进行String与Date间的转换，并包含有获取以前的时间信息的功能，测试成功
//创建人：龚灿	创建时间：20190827	最近更新时间：20190827
public class DateOperation {
	//该函数用于获取上past个月的第一天日期，以字符串形式返回，测试成功
	//创建人：龚灿	创建时间：20190827	最近更新时间：20190827
	//输入为上几个月，输出为past个月前的第一天日期，past=0得到当月第一天日期
	static String getPastFirst(int past) {
		if(past<0 || past>11)
			return null;
		Date currentTime = new Date();
		String currentDateString = getStringDate(currentTime);
		String[] str = currentDateString.split("-");
		int year = Integer.parseInt(str[0]);
		int month = Integer.parseInt(str[1]);
		month -= past;
		if(month<=0) {
			month += 12;
			--year;
		}
		str[2] = "01";
		String pastDateString = new String();
		//统一格式yyyy-MM-dd
		if(month<10)
			pastDateString = String.valueOf(year)+"-0"+String.valueOf(month)+"-"+str[2];
		else
			pastDateString = String.valueOf(year)+"-"+String.valueOf(month)+"-"+str[2];
		return pastDateString;
	}
	
	//该函数用于获取下个月第一天的日期，以字符串形式返回，测试成功
	//创建人：龚灿	创建时间：20190827	最近更新时间：20190827
	//无需输入，输出为下个月第一天的日期
	static String getNextFirst() {
		Date currentTime = new Date();
		String currentDateString = getStringDate(currentTime);
		String[] str = currentDateString.split("-");
		int year = Integer.parseInt(str[0]);
		int month = Integer.parseInt(str[1]);
		month++;
		if(month>=12) {
			month -= 12;
			year++;
		}
		str[2] = "01";
		String pastDateString = new String();
		//统一格式yyyy-MM-dd
		if(month<10)
			pastDateString = String.valueOf(year)+"-0"+String.valueOf(month)+"-"+str[2];
		else
			pastDateString = String.valueOf(year)+"-"+String.valueOf(month)+"-"+str[2];
		return pastDateString;
	}
	
	//Date转String，测试成功
	//创建人：龚灿	创建时间：20190827	最近更新时间：20190827
	public static String getStringDate(Date time) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = formatter.format(time);
		return dateStr;
	}
	
	//String转Date，测试成功
	//创建人：龚灿	创建时间：20190827	最近更新时间：20190827
	public static Date getDate(String str) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    	Date date = formatter.parse(str);
    	return date;
	}
}
