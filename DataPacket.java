package com.db;

import java.util.ArrayList;

public class DataPacket {
	public String name = new String();//城市名
	public int id = 0;//0代表城市，1代表区域，2代表楼盘
	public ArrayList<Region> type = new ArrayList<Region>();
}
