package com.db;

import java.sql.SQLException;
import java.util.TimerTask;

//定时任务，每天使用爬虫爬取新数据到house表中，并更新buildCode表中的average字段
//创建人：龚灿	创建时间：20190910	最近更新时间：20190910
public class UpdateDatabase extends TimerTask{
	public void run() {
		// TODO Auto-generated method stub
		try {
			DataInterview.updateAverage();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
