package com.db;

//测试成功
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
	
	//该函数用于计算多项式特定x值下的y值，20190828龚灿，测试成功
	//输入为自变量x
	double getValue(double x) {
		double y = coefficients[power];
		for(int i=power-1;i>=0;--i) {
			y *= x;
			y += coefficients[i];
		}
		return y;
	}
}
