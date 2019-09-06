package com.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//测试中
public class DataInterview {
	//链接数据库服务器的字符串
	private static String sqlurl = new String("jdbc:sqlserver://yuyanjia.database.chinacloudapi.cn:1433;database=experiment;"
			+ "user=yuyanjia@yuyanjia;password=Qwer1234;encrypt=true;trustServerCertificate=false;"
			+ "hostNameInCertificate=*.database.chinacloudapi.cn;loginTimeout=30;");
	private static HashMap<String, String> newBuild = new HashMap<String, String>();//key为楼盘名，value为区域名
	static {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//该函数用于向数据库cityCode中插入记录，20190823龚灿，测试成功
	//输入分别为：市，区域
	public static boolean addCity(String cit, String reg) throws SQLException {
		//统一格式
    	if(cit.charAt(cit.length()-1)=='市')
    		cit = cit.substring(0, cit.length()-1);
    	char r = reg.charAt(reg.length()-1);
    	if(r=='县'||r=='区'||r=='市') {
    		reg = reg.substring(0, reg.length()-1);
    		if(reg.length()-2>=0 && reg.substring(reg.length()-2).compareTo("自治")==0)
    			reg = reg.substring(0, reg.length()-2);
    	}
		Connection connection = DriverManager.getConnection(sqlurl);
    	Statement statement = connection.createStatement();
    	String sql = "SELECT count(code) from cityCode where city=N'"+cit+"' and region=N'"+reg+"'";
    	ResultSet rs = statement.executeQuery(sql);
    	rs.next();
    	//判断是否已存在该城市区域
    	if(rs.getInt(1)!=0) {
    		statement.close();
        	connection.close();
    		return false;
    	}
    	sql = "INSERT into cityCode (city,region) VALUES (N'"+cit+"',N'"+reg+"')";
    	statement.executeUpdate(sql);
    	statement.close();
    	connection.close();
    	return true;
	}
	
	//该函数用于在数据库cityCode中删除记录，20190823龚灿，测试成功
	//输入为：市或区域
	public static void deleteCity(String pos) throws SQLException {
		Connection connection = DriverManager.getConnection(sqlurl);
    	Statement statement = connection.createStatement();
    	String sql = "DELETE from cityCode where city=N'"+pos+"'";
    	statement.executeUpdate(sql);
    	sql = "DELETE from cityCode where region=N'"+pos+"'";
    	statement.executeUpdate(sql);
    	statement.close();
    	connection.close();
	}
	
	//该函数用于向数据库cityCode中查询区域对应的区域编号，20190823龚灿，测试成功
	//输入为：区域
	private static int queryRegion(String reg) throws SQLException {
		char r = reg.charAt(reg.length()-1);
    	if(r=='县'||r=='区'||r=='市') {
    		reg = reg.substring(0, reg.length()-1);
    		if(reg.length()-2>=0 && reg.substring(reg.length()-2).compareTo("自治")==0)
    			reg = reg.substring(0, reg.length()-2);
    	}
    	Connection connection = DriverManager.getConnection(sqlurl);
    	Statement statement = connection.createStatement();
    	String sql = "SELECT code from cityCode where region=N'"+reg+"'";
    	ResultSet rs = statement.executeQuery(sql);
    	int result = 0;
    	if(rs.next())
    		result = rs.getInt("code");
    	statement.close();
    	connection.close();
    	return result;//结果为0说明该区域不存在
	}
	
	//该函数用于向数据库buildCode中插入记录，20190827龚灿，测试成功
	//输入分别为：区域，楼盘，图片路径，具体地址
	public static boolean addBuilding(String reg, String bui, String photo, String add) throws SQLException {
		int code = queryRegion(reg);
		//该区域不存在
		if(code==0) {
			//将新楼盘暂存于哈希表中供管理员管理
			if(!newBuild.containsKey(bui))
				newBuild.put(bui, reg);
			return false;
		}
		Connection connection = DriverManager.getConnection(sqlurl);
    	Statement statement = connection.createStatement();
    	String sql = "SELECT count(bCode) from buildCode where code="+code+" and building=N'"+bui+"'";
    	ResultSet rs = statement.executeQuery(sql);
    	rs.next();
    	//判断是否已存在该楼盘
    	if(rs.getInt(1)!=0) {
    		statement.close();
        	connection.close();
    		return false;
    	}
    	sql = "INSERT into buildCode (code,building,photo,address) VALUES ("+code+",N'"+bui+"','"+photo+"',N'"+add+"')";
    	statement.executeUpdate(sql);//url中不能包含 ; '
    	statement.close();
    	connection.close();
    	if(newBuild.containsKey(bui))
    		newBuild.remove(bui);
    	return true;
	}
	
	//该函数用于在数据库buildingCode中删除记录，20190902龚灿，测试成功
	//输入为：楼盘编号
	public static boolean deleteBuilding(int bCo) throws SQLException {
		Connection connection = DriverManager.getConnection(sqlurl);
    	Statement statement = connection.createStatement();
    	String sql = "SELECT count(id) from house where bCode="+bCo;
    	ResultSet rs = statement.executeQuery(sql);
    	rs.next();
    	if(rs.getInt(1)!=0) {
    		statement.close();
        	connection.close();
    		return false;
    	}
    	sql = "DELETE from buildCode where bCode="+bCo;
    	statement.executeUpdate(sql);
    	statement.close();
    	connection.close();
    	return true;
	}
	
	//该函数用于向数据库buildCode中查询楼盘对应的楼盘编号，20190828龚灿，测试成功
	//输入为：区域，楼盘
	private static int queryBuilding(String reg, String bui) throws SQLException {
		int code = queryRegion(reg);
		//该区域不存在
		if(code==0)
			return 0;
		Connection connection = DriverManager.getConnection(sqlurl);
    	Statement statement = connection.createStatement();
    	String sql = "SELECT bCode from buildCode where code="+code+" and building=N'"+bui+"'";
    	ResultSet rs = statement.executeQuery(sql);
    	int result = 0;
    	if(rs.next())
    		result = rs.getInt("bCode");
    	statement.close();
    	connection.close();
    	return result;//结果为0说明该区域不存在
	}
	
	//该函数用于辅助生成静态页面，20190906龚灿，测试成功
	public static int queryMaxBCode() throws SQLException {
		Connection connection = DriverManager.getConnection(sqlurl);
    	Statement statement = connection.createStatement();
    	String sql = "SELECT max(bCode) as bCode from buildCode";
    	ResultSet rs = statement.executeQuery(sql);
    	rs.next();
    	int max = rs.getInt("bCode");
    	statement.close();
    	connection.close();
    	return max;
	}
	
	//该函数用于管理员查询新楼盘，20190904龚灿，测试成功
	public static HashMap<String, String> queryNewBuild() {
		return newBuild;
	}
	
	//该函数用于添加楼盘对应的静态网址，20190904龚灿，测试成功
	//输入为楼盘编号，网址
	public static void addUrl(int bCo, String url) throws SQLException {
		Connection connection = DriverManager.getConnection(sqlurl);
    	Statement statement = connection.createStatement();
    	String sql = "UPDATE buildCode SET url='"+url+"' where bCode="+bCo;
    	statement.executeUpdate(sql);
    	statement.close();
    	connection.close();
	}
	
	//该函数用于在数据库表buildCode中查询某楼盘的静态网址，20190904龚灿
	//输入为楼盘编号
	public static String queryUrl(int bCo) throws SQLException {
		Connection connection = DriverManager.getConnection(sqlurl);
    	Statement statement = connection.createStatement();
    	String sql = "SELECT url from buildCode where bCode="+bCo;
    	ResultSet rs = statement.executeQuery(sql);
    	String urlString = new String();
    	if(rs.next())
    		urlString = rs.getString("url");
    	statement.close();
    	connection.close();
    	return urlString;//结果为null说明该区域不存在
	}
	
	//该函数用于向数据库house中插入一整行城市区域小区房价信息，20190827龚灿，测试成功
	//输入分别为：区域，楼盘，户型，时间，房价，面积，图片路径
    public static boolean addHouse(String reg, String bui, String typ, String tim, 
    		int pri, int are, String url) throws SQLException, ParseException{
    	int bCode = queryBuilding(reg, bui);//查询楼盘编号并插入到house的bCode字段
    	if(bCode==0) {
			if(!newBuild.containsKey(bui))
				newBuild.put(bui, reg);
			return false;
		}
    	//将String时间转为java.sql.Date类型的"yyyy-MM-dd HH:mm:ss"格式
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Date current = formatter.parse(tim);
    	java.sql.Date date = new java.sql.Date(current.getTime());
    	Connection connection = DriverManager.getConnection(sqlurl);
    	Statement statement = connection.createStatement();
    	String sql = "INSERT into house (bCode,type,time,price,area,photo) VALUES ("+bCode
    			+",N'"+typ+"','"+date.toString()+"',"+pri+","+are+",'"+url+"')";
    	statement.executeUpdate(sql);
    	statement.close();
    	connection.close();
    	return true;
    }
    
    //该函数用于删除具体楼盘户型的所有相关数据，20190904龚灿
    //输入为时间(String yyyy-MM-dd)
    public static boolean deleteHouse(String tim) throws SQLException {
    	if(tim.compareTo(DateOperation.getPastFirst(11))!=-1)
    		return false;
    	Connection connection = DriverManager.getConnection(sqlurl);
    	Statement statement = connection.createStatement();
    	String sql = "DELETE from house where time='"+tim+"'";
    	statement.executeUpdate(sql);
    	statement.close();
    	connection.close();
    	return true;
    }
    
    //该函数用于查house表，按时间排序，20190906龚灿
    public static ArrayList<String> tableTime() throws SQLException {
    	ArrayList<String> time = new ArrayList<String>();
    	Connection connection = DriverManager.getConnection(sqlurl);
    	Statement statement = connection.createStatement();
    	String sql = "SELECT distinct(time) as time from house order by time";
    	ResultSet rs = statement.executeQuery(sql);
    	while(rs.next())
    		time.add(DateOperation.getStringDate(rs.getDate("time")));
    	statement.close();
    	connection.close();
		return time;
    }
    
    //该函数判断用户输入的pos具体是什么位置信息，并以不同方式调用queryCity，20190824龚灿
    //输入为：市或区域或楼盘
    public static DataPacket[] queryAll(String pos) throws SQLException, ParseException{
    	String snt = new String(pos);
    	Connection connection = DriverManager.getConnection(sqlurl);
    	Statement statement = connection.createStatement();
    	if(snt.charAt(snt.length()-1)=='市')
    		snt = snt.substring(0, snt.length()-1);
    	String sql = "SELECT code from cityCode where city=N'"+snt+"'";
    	ResultSet rs = statement.executeQuery(sql);
    	//pos为城市名
    	if(rs.next()) {
    		System.out.println("城市");
    		DataPacket[] packet = new DataPacket[1];
    		packet[0] = queryCity(snt);
    		statement.close();
        	connection.close();
    		return packet;
    	}
    	else {
    		char r = snt.charAt(snt.length()-1);
        	if(r=='县'||r=='区'||r=='市') {
        		snt = snt.substring(0, snt.length()-1);
        		if(snt.substring(snt.length()-2).compareTo("自治")==0)
        			snt = snt.substring(0, snt.length()-2);
        	}
    		sql = "SELECT city from cityCode where region=N'"+snt+"'";
    		rs = statement.executeQuery(sql);
    		//pos为区域名
    		if(rs.next()) {
    			System.out.println("区域");
    			DataPacket[] packet = new DataPacket[1];
        		packet[0] = queryCity(rs.getString("city"));
        		statement.close();
            	connection.close();
        		return packet;
    		}
    		else {
    			sql = "SELECT bCode from buildCode where building=N'"+snt+"'";
        		rs = statement.executeQuery(sql);
        		if(!rs.next()) {
        			System.out.println("错误");
        			statement.close();
        	    	connection.close();
        			return null;
        		}
        		//pos为楼盘名
        		else {
        			System.out.println("楼盘");
        			ArrayList<Integer> bCodes = new ArrayList<Integer>();
        			bCodes.add(rs.getInt("bCode"));
        			while(rs.next())
        				bCodes.add(rs.getInt("bCode"));
        			DataPacket[] packet = new DataPacket[bCodes.size()];
        			for(int i=0;i<packet.length;i++)
        				packet[i] = queryBuild(bCodes.get(i));
        			statement.close();
        	    	connection.close();
        			return packet;
        		}
    		}
    	}
    }

    //该函数用于获取特定城市的所有房价信息，20190827龚灿，测试成功
    //输入为城市名
	public static DataPacket queryCity(String cit) throws SQLException, ParseException {
		// TODO Auto-generated method stub
		DataPacket dataPacket = new DataPacket();
		dataPacket.name = cit;
		//记录该城市下所有<区域名，区域编号>
		HashMap<String,Integer> cityCodes = new HashMap<String,Integer>();
		Connection connection = DriverManager.getConnection(sqlurl);
    	Statement statement = connection.createStatement();
    	String sql = "SELECT code,region from cityCode where city=N'"+cit+"'";
    	ResultSet rs = statement.executeQuery(sql);
    	while(rs.next())
    		cityCodes.put(rs.getString("region"), rs.getInt("code"));
    	int rnumber = 1;
    	//对于每一个区域
    	for(Map.Entry<String,Integer> m : cityCodes.entrySet()) {
    		Region region = new Region();
    		region.name = m.getKey();
    		region.id = rnumber;
    		//记录该区域下的所有楼盘编号
    		ArrayList<Integer> buildCodes = new ArrayList<Integer>();
    		sql = "SELECT bCode from buildCode where code="+m.getValue();
    		rs = statement.executeQuery(sql);
    		while(rs.next())
    			buildCodes.add(rs.getInt("bCode"));
    		int rtotal = 0, rcount = 0;
    		int bnumber = 1;
    		//对于每一个楼盘
    		for(int n : buildCodes) {
    			Building build = new Building();
    			sql = "SELECT building,photo,address,url from buildCode where bCode="+n;
    			rs = statement.executeQuery(sql);
    			rs.next();
    			build.name = rs.getString("building");
    			build.photo = rs.getString("photo");
    			build.addr = rs.getString("address");
    			build.url = rs.getString("url");
    			build.id = bnumber;
        		bnumber++;
    			//取最近更新数据的时间
    			sql = "SELECT top 1 time from house where bCode="+n+" order by time desc";
        		rs = statement.executeQuery(sql);
        		//没有该楼盘的具体数据则跳过
        		if(!rs.next())
        			continue;
        		String time = DateOperation.getStringDate(rs.getDate("time"));
        		//获取最近更新的平均价格
        		sql = "SELECT avg(price) as price from house where bCode="+n+" and time='"+time+"'";
        		rs = statement.executeQuery(sql);
        		rs.next();
        		build.value = rs.getInt("price");
        		rtotal+=rs.getInt("price");
        		//记录一个楼盘变量
        		region.type.add(build);
        		rcount++;
    		}
    		if(rcount!=0)
    			region.value = rtotal/rcount;
    		//记录一个区域
    		dataPacket.type.add(region);
    		rnumber++;
    	}
    	statement.close();
    	connection.close();
		return dataPacket;
	}
	
	//该函数用于获取特定楼盘的所有房价信息，20190905龚灿，测试成功
    //输入为楼盘编号
	public static DataPacket queryBuild(int bCo) throws SQLException {
		DataPacket dataPacket = new DataPacket();
		Region region = new Region();
		Building build = new Building();
		Connection connection = DriverManager.getConnection(sqlurl);
    	Statement statement = connection.createStatement();
		String sql = "SELECT * from buildCode where bCode="+bCo;
		ResultSet rs = statement.executeQuery(sql);
		rs.next();
		int code = rs.getInt("code");//记录下城市区域编号供后面使用
		build.name = rs.getString("building");
		build.photo = rs.getString("photo");
		build.addr = rs.getString("address");
		build.url = rs.getString("url");
		//取最近更新数据的时间
		sql = "SELECT top 1 time from house where bCode="+bCo+" order by time desc";
		rs = statement.executeQuery(sql);
		//没有该楼盘的具体数据则将其均值记为0
		if(rs.next()) {
			String time = DateOperation.getStringDate(rs.getDate("time"));
			//获取最近更新的平均价格
			sql = "SELECT avg(price) as price from house where bCode="+bCo+" and time='"+time+"'";
			rs = statement.executeQuery(sql);
			rs.next();
			build.value = rs.getInt("price");
		}
		region.type.add(build);
    	sql = "SELECT * from cityCode where code="+code;
    	rs = statement.executeQuery(sql);
    	rs.next();
    	//记录下该楼盘的具体城市区域
    	region.name = rs.getString("region");
    	dataPacket.type.add(region);
    	dataPacket.name = rs.getString("city");
    	statement.close();
    	connection.close();
    	return dataPacket;
	}
	
	//该函数用于预测楼盘房价并返回楼盘所有具体信息，20190905龚灿，测试成功
	//输入为楼盘编号
	public static Building foreseeBuild(int bCo) throws SQLException, ParseException {
		Building build = new Building();
		Connection connection = DriverManager.getConnection(sqlurl);
    	Statement statement = connection.createStatement();
		String sql = "SELECT code,building,photo,address,url from buildCode where bCode="+bCo;
		ResultSet rs = statement.executeQuery(sql);
		rs.next();
		int code = rs.getInt("code");
		build.name = rs.getString("building");
		build.photo = rs.getString("photo");
		build.addr = rs.getString("address");
		build.url = rs.getString("url");
		build.firstTime = DateOperation.getDate(DateOperation.getPastFirst(11));
		sql = "SELECT * from cityCode where code="+code;
		rs = statement.executeQuery(sql);
		rs.next();
		build.addr = rs.getString("city")+rs.getString("region")+build.addr;
		//取最近更新数据的时间
		sql = "SELECT top 1 time from house where bCode="+bCo+" order by time desc";
		rs = statement.executeQuery(sql);
		if(!rs.next()) {
			for(int i=0;i<14;i++)
				build.values[i] = 0;
			statement.close();
	    	connection.close();
	    	return build;
		}
		String time = DateOperation.getStringDate(rs.getDate("time"));
		//获取最近更新的平均价格
		sql = "SELECT type,price,area,photo from house where bCode="+bCo+" and time='"+time+"'";
		rs = statement.executeQuery(sql);
		int btotal = 0, bnumber = 0;
		int tnumber = 1;
		//对于每一个户型
		while(rs.next()) {
			BuildingType type = new BuildingType();
			type.name = rs.getString("type");
			type.value = rs.getInt("price");
			btotal+=rs.getInt("price");
			type.area = rs.getInt("area");
			type.url = rs.getString("photo");
			type.id = tnumber;
			build.type.add(type);//添加户型
    		tnumber++;
    		bnumber++;
		}
		if(bnumber!=0)
			build.value = btotal/bnumber;
		build.values = getValues(bCo);
		statement.close();
    	connection.close();
    	return build;
	}
	
	//该函数用于填充Building对象的values数组，20190827龚灿，测试成功
	//输入为楼盘编号
	private static int[] getValues(int bCo) throws SQLException {
		int[] values = new int[14];
		for(int i=0;i<14;i++)
			values[i] = 0;
		Connection connection = DriverManager.getConnection(sqlurl);
    	Statement statement = connection.createStatement();
    	String sql = new String();
    	ResultSet rs;
		for(int i=11;i>0;--i) {
			sql = "SELECT avg(price) from house where bCode="+bCo+" and time>='"+DateOperation.getPastFirst(i)
				+"' and time<'"+DateOperation.getPastFirst(i-1)+"'";
			rs = statement.executeQuery(sql);
			if(rs.next())
				values[11-i] = rs.getInt(1);
		}
		sql = "SELECT avg(price) from house where bCode="+bCo+" and time>='"+DateOperation.getPastFirst(0)
			+"' and time<'"+DateOperation.getNextFirst()+"'";
		rs = statement.executeQuery(sql);
		if(rs.next())
			values[11] = rs.getInt(1);
		Foresee.foresee(values);
		return values;
	}

	//该函数用于房价对比功能，寻找到价格区间内的楼盘并将其相关信息返回，20190906龚灿，测试成功
	//输入为价格区间
	public static ArrayList<Building> compareBuild(int min, int max) throws SQLException {
		ArrayList<Building> buildings = new ArrayList<Building>();
		Connection connection = DriverManager.getConnection(sqlurl);
	   	Statement statement = connection.createStatement();
	   	String sql = "SELECT top 1 time from house order by time desc";
	   	ResultSet rs = statement.executeQuery(sql);
	   	rs.next();
	   	String time = DateOperation.getStringDate(rs.getDate("time"));
	   	sql = "SELECT distinct(bCode) as bCode from house where time='"+time+"' and price>="+min+" and price<="+max;
	   	rs = statement.executeQuery(sql);
	   	ArrayList<Integer> bCodes = new ArrayList<Integer>();
	   	while(rs.next())
	   		bCodes.add(rs.getInt("bCode"));
		for(int bCode : bCodes) {
			Building build = new Building();
			sql = "SELECT * from buildCode where bCode="+bCode;
			rs = statement.executeQuery(sql);
			rs.next();
			int code = rs.getInt("code");//记录下城市区域编号供后面使用
			build.name = rs.getString("building");
			build.photo = rs.getString("photo");
			build.addr = rs.getString("address");
			build.url = rs.getString("url");
			//获取最近更新的平均价格
			sql = "SELECT avg(price) as price from house where bCode="+bCode+" and time='"+time+"'";
			rs = statement.executeQuery(sql);
			rs.next();
			build.value = rs.getInt("price");
	    	sql = "SELECT * from cityCode where code="+code;
	    	rs = statement.executeQuery(sql);
	    	rs.next();
	    	//记录下该楼盘的具体城市区域
	    	build.addr = rs.getString("city")+rs.getString("region")+build.addr;
	    	buildings.add(build);
		}
		statement.close();
		connection.close();
	   	return buildings;
	}
	
	//该函数为登录函数，20190903龚灿，测试成功
	//输入为用户名，密码，IP地址
	public static int login(String user, String pass) throws SQLException {
		if(!isSuitable(user, pass))
			return 2;//2代表用户名或密码格式错误
		Connection connection = DriverManager.getConnection(sqlurl);
	   	Statement statement = connection.createStatement();
	   	String sql = "SELECT password,statue from account where username='"+user+"'";
	   	ResultSet rs = statement.executeQuery(sql);
	   	if(!rs.next()) {
	   		statement.close();
	       	connection.close();
	   		return 3;//3代表该用户名不存在
	   	}
	   	String password = rs.getString("password");
	   	if(password.compareTo(pass)!=0) {
	   		statement.close();
	   		connection.close();
	   		return 4;//4代表密码错误
	   	}
	   	int statue = rs.getInt("statue");
    	statement.close();
    	connection.close();
    	return statue;//0为普通用户，1为管理员登录
	}
	
	//该函数为注册函数，20190902龚灿，测试成功
	//输入为用户名，密码
	public static int register(String user, String pass) throws SQLException {
		if(!isSuitable(user, pass))
			return 1;//1代表用户名或密码格式错误
		Connection connection = DriverManager.getConnection(sqlurl);
	   	Statement statement = connection.createStatement();
	   	String sql = "SELECT count(username) from account where username='"+user+"'";
	   	ResultSet rs = statement.executeQuery(sql);
	   	rs.next();
    	int exist = rs.getInt(1);
	   	if(exist!=0) {
	   		statement.close();
	       	connection.close();
	       	return 2;//2代表用户名已存在
	   	}
	   	//默认昵称为用户名
	   	sql = "INSERT into account (username,password,statue,sickname) VALUES ('"+user+"','"+pass+"',0,N'"+user+"')";
    	statement.executeUpdate(sql);
	   	statement.close();
    	connection.close();
    	return 0;//0代表注册成功
	}
	
	//该函数为更改密码函数，20190902龚灿，测试成功
	//输入为用户名，旧密码，新密码，确认密码
	public static int changePassword(String user, String oldPass, String newPass, String again) throws SQLException {
		if(!isSuitable(newPass, again))
			return 1;//1代表新密码格式错误
		if(newPass.compareTo(again)!=0)
	   		return 2;//2代表确认密码与新密码不同
		Connection connection = DriverManager.getConnection(sqlurl);
	   	Statement statement = connection.createStatement();
	   	String sql = "SELECT password from account where username='"+user+"'";
	   	ResultSet rs = statement.executeQuery(sql);
	   	rs.next();
	   	String password = rs.getString("password");
	   	if(password.compareTo(oldPass)!=0) {
	   		statement.close();
	   		connection.close();
	   		return 3;//3代表旧密码错误
	   	}
	   	sql = "UPDATE account SET password='"+newPass+"' where username='"+user+"'";
	   	statement.executeUpdate(sql);
	   	statement.close();
	   	connection.close();
	   	return 0;//0代表更改密码成功
	}

	//该函数为更改昵称函数，20190902龚灿，测试成功
	//输入为用户名，新昵称
	public static boolean changeSickname(String user, String newSick) throws SQLException {
		//昵称最长为16位且不能为空，昵称只能包含字母，数字，汉字
		if(newSick.length()>16 || newSick.length()==0)
			return false;
		String sickString = newSick.replaceAll("[^A-Za-z0-9\\u4E00-\\u9FA5]", "");
		if(sickString.compareTo(newSick)!=0)
			return false;
		Connection connection = DriverManager.getConnection(sqlurl);
	   	Statement statement = connection.createStatement();
	   	String sql = "UPDATE account SET sickname=N'"+newSick+"' where username='"+user+"'";
	   	statement.executeUpdate(sql);
	   	statement.close();
	   	connection.close();
	   	return true;
	}
	
	//该函数为添加收藏函数，20190902龚灿，测试成功
	//输入为用户名，待收藏网址
	public static boolean addCollect(String user, String coll) throws SQLException {
		Connection connection = DriverManager.getConnection(sqlurl);
	   	Statement statement = connection.createStatement();
	   	String sql = "SELECT count(id) from collection where username='"+user+"' and collectUrl='"+coll+"'";
	   	ResultSet rs = statement.executeQuery(sql);
	   	rs.next();
	   	//若该用户已收藏该网址则报错
	   	if(rs.getInt(1)!=0) {
	   		statement.close();
	   		connection.close();
	   		return false;
	   	}
	   	sql = "INSERT into collection (username,collectUrl) VALUES ('"+user+"','"+coll+"')";
	   	statement.executeUpdate(sql);
	   	statement.close();
		connection.close();
		return true;
	}

	//该函数为添加收藏函数，20190902龚灿，测试成功
	//输入为用户名，已收藏网址
	public static void deleteCollect(String user, String coll) throws SQLException {
		Connection connection = DriverManager.getConnection(sqlurl);
	   	Statement statement = connection.createStatement();
	   	String sql = "DELETE from collection where username='"+user+"' and collectUrl='"+coll+"'";
    	statement.executeUpdate(sql);
    	statement.close();
	   	connection.close();
	}
	
	//该函数为查询收藏函数，20190903龚灿，测试成功
	//输入为用户名
	public static ArrayList<String> queryCollect(String user) throws SQLException {
		ArrayList<String> collections = new ArrayList<String>();
		Connection connection = DriverManager.getConnection(sqlurl);
	   	Statement statement = connection.createStatement();
	   	String sql = "SELECT collectUrl from collection where username='"+user+"'";
    	ResultSet rs = statement.executeQuery(sql);
    	while(rs.next())
    		collections.add(rs.getString("collectUrl"));
    	statement.close();
	   	connection.close();
		return collections;
	}
	
	//该函数为验证用户名与密码是否符合格式要求，20190902龚灿，测试成功
	//输入为用户名，密码
	private static boolean isSuitable(String user, String pass) {
		//用户名和密码均为6-16位，只能包含数字与字母
		if(user.length()>16 || user.length()<6)
			return false;
		if(pass.length()>16 || pass.length()<6)
			return false;
		String username = user.replaceAll("[^A-Za-z0-9]", "");
		String passname = pass.replaceAll("[^A-Za-z0-9]", "");
		if(username.compareTo(user)!=0)
			return false;
		if(passname.compareTo(pass)!=0)
			return false;
		return true;
	}
}
