package com.db;

import java.sql.Connection;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;

public class App {

    public static void main(String[] args) throws SQLException, ParseException {
    	//String string = new String("杭州湾大道1818弄, 杭州湾大道1878弄, 龙山路280弄, 杭州湾大道1908弄, 龙山路346弄, 龙山路280弄19支, 龙翔路649.663.667.665.669号, 海芙路149.151.153.145.147号, 杭州湾大道1816号");
    	//System.out.println(string.length());
    	/*
    	DataPacket[] packets = DataInterview.queryAll("楼盘J");
    	if(packets!=null) {
    		for(DataPacket packet : packets) {
    			System.out.println(packet.name);
    			System.out.println(packet.inputText);
    			for(Region region : packet.type) {
    				System.out.println("    "+region.id);
    				System.out.println("    "+region.name);
    				System.out.println("    "+region.value);
    				for(Building build : region.type) {
    					System.out.println("        "+build.id);
    					System.out.println("        "+build.name);
    					System.out.println("        "+build.value);
    					System.out.println("        "+build.url);
    					System.out.println("        "+build.addr);
    					for(BuildingType type : build.type) {
    						System.out.println("                "+type.id);
    						System.out.println("                "+type.name);
    						System.out.println("                "+type.value);
    						System.out.println("                "+type.url);
    						System.out.println("                "+type.area);
    					}
    				}
    			}
    		}
    	}
    	else System.out.println("error");
    	*/
    	//DataInterview.addHouse("鼓楼", "楼盘B", "四室三厅一卫", "2019-08-24 01:01:01", 140000, 90, "http://baidu.com");
    	//DataInterview.addHouse("兴宁区", "楼盘G", "三室两厅一卫", "2019-08-24 01:01:01", 120000, 85, "http://baidu.com");
    	//DataInterview.addHouse("江南区", "楼盘I", "三室两厅一卫", "2019-08-24 01:01:01", 120000, 85, "http://baidu.com");
    	//DataInterview.addHouse("良庆区", "楼盘H", "四室两厅两卫", "2019-08-24 01:01:01", 135000, 140, "http://baidu.com");
    	//DataInterview.addHouse("兴宁区", "楼盘J", "三室两厅一卫", "2019-08-24 01:01:01", 120000, 100, "http://baidu.com");
    	//DataInterview.addHouse("江南区", "楼盘K", "四室两厅两卫", "2019-08-24 01:01:01", 115000, 130, "http://baidu.com");

    }
}