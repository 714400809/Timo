package com.db;

import java.util.Date;
import java.util.ArrayList;

public class Building {
	public String name = new String();//楼盘名
	public int value = 0;//楼盘均价
	public String photo = new String();//楼盘照片
	public String addr = new String();//楼盘地址
	public String url = new String();//楼盘跳转网址
	public int id = 0;
	public ArrayList<BuildingType> type = new ArrayList<BuildingType>();
	public int[] values = new int[14];//楼盘以往12个月的均价和未来2个月预测的均价
	public Date firstTime;
}
