package com.db;

//此类为多项式类，用于进行多项式的存储和计算，测试成功
//创建人：龚灿	创建时间：20190828	最近更新时间：20190828
public class Polynomial {
	double[] coefficients;//系数数组
	int power;//最高次
	double errorValue;//误差值
	
	public Polynomial(int n) {
		if(n>=0) {
			power = n;
			coefficients = new double[power+1];
		}
	}
	
	//该函数用于计算多项式特定x值下的y值，测试成功
	//创建人：龚灿	创建时间：20190828	最近更新时间：20190828
	//输入为自变量x，输出为对应得因变量y
	double getValue(double x) {
		double y = coefficients[power];
		for(int i=power-1;i>=0;--i) {
			y *= x;
			y += coefficients[i];
		}
		return y;
	}
}
