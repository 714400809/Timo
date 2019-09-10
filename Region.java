package com.db;

import java.util.ArrayList;

//此类为区域的封装类，包含了区域的各种信息，测试成功
//创建人：李名啟		创建时间：20190825	最近更新时间：20190825
public class Region {
	public String name = new String();//区域名
	public int value = 0;//区域均价
	public int id = 1;
	public ArrayList<Building> type = new ArrayList<Building>();
}
