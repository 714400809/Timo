package com.db;

import java.util.Timer;

//此类为爬虫线程类，专用于服务器每天爬取并更新数据
//创建人：龚灿	创建时间：20190910	最近更新时间：20190910
public class PachongThread extends Thread{
	public void run() {
		Timer timer = new Timer();
		//服务器运行时，每天爬取一次信息并更新楼盘均价
		timer.schedule(new UpdateDatabase(), 24*60*60*1000, 30*1000);
	}
}
