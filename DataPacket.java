package com.db;

import java.util.ArrayList;

//此类为城市的封装类，包含了城市的各种信息，也是返回给前端的数据包，测试成功
//创建人：李名啟		创建时间：20190825	最近更新时间：20190904
public class DataPacket {
	public String name = new String();//城市名
	public int id = 0;//0代表城市，1代表区域，2代表楼盘
	public ArrayList<Region> type = new ArrayList<Region>();
}
