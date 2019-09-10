package com.db;

import java.util.ArrayList;

//此类专用于预测房价，包含曲线拟合与误差对比两个功能，测试成功
//创建人：龚灿	创建时间：20190828	最近更新时间：20190906
public class Foresee {
	//该函数调用多项式曲线拟合，并取误差最小的函数进行预测，并将预测结果储存在values中，测试成功
	//创建人：龚灿	创建时间：20190828	最近更新时间：20190906
	//输入为values数组，values[0]到values[11]是过去一年的数据，无返回值
	static void foresee(int[] values) {
		ArrayList<Integer> nonZero = new ArrayList<Integer>();
		for(int i=0;i<12;i++)
			if(values[i]!=0)
				nonZero.add(i);
		if(nonZero.size()==0)
			return;
		int max = 1;//一个数用常数函数预测
		if(nonZero.size()>1)
			max = nonZero.size()-1;//n个数预测到n-2次
		Polynomial[] polynomials = new Polynomial[max];
		for(int i=0;i<max;i++)
			polynomials[i] = curveFitting(i,values);
		Polynomial bestPoly = polynomials[0];
		//选取误差值最小的多项式拟合曲线
		for(int i=1;i<max;i++)
			if(bestPoly.errorValue > polynomials[i].errorValue)
				bestPoly = polynomials[i];
		values[12] = (int)bestPoly.getValue(12);
		values[13] = (int)bestPoly.getValue(13);
	}
	
	//该函数用于进行多项式曲线拟合，并将多项式返回，测试成功
	//创建人：龚灿	创建时间：20190828	最近更新时间：20190905
	//输入为多项式最高次n和values数组，values[0]到values[11]是过去一年的数据，输出为多项式拟合函数
	//备注：n个点最多拟合到n-1次多项式，否则会出现冗余方程使得行列式为0
	static Polynomial curveFitting(int n, int[] values) {
		ArrayList<Integer> nonZero = new ArrayList<Integer>();
		for(int i=0;i<12;i++)
			if(values[i]!=0)
				nonZero.add(i);
		/*
		System.out.println(nonZero.size());
		for(int k : nonZero) {
			System.out.println(k);
			System.out.println(values[k]);
		}
		*/
		Matrix leftMatrix = new Matrix(n+1, n+1);
		Matrix rightMatrix = new Matrix(n+1, 1);
		for(int i=1;i<=leftMatrix.xsize;i++) {
			for(int j=1;j<=leftMatrix.ysize;j++) {
				int ijVal = 0;
				for(int k : nonZero) {
					int indexN = 1;
					//i+j-2即为index的幂次
					for(int power=0;power<i+j-2;power++) {
						indexN*=k;
					}
					ijVal+=indexN;
				}
				leftMatrix.matrix[i][j] = ijVal;
			}	
		}
		for(int i=1;i<=rightMatrix.xsize;i++) {
			int ijVal = 0;
			for(int k : nonZero) {
				int indexN = values[k];
				//i-1即为index的幂次
				for(int power=0;power<i-1;power++) {
					indexN*=k;
				}
				ijVal+=indexN;
			}
			rightMatrix.matrix[i][1] = ijVal;
		}
		Matrix coefficientMatrix = leftMatrix.getInverseMatrix().multMatrix(rightMatrix);
		Polynomial poly = new Polynomial(n);
		//存储拟合结果
		for(int i=0;i<=n;i++)
			poly.coefficients[i] = coefficientMatrix.matrix[i+1][1];
	    double errorValue = 0.0;
	    //计算误差
	    for(int k : nonZero)
	    	errorValue += (poly.getValue(k)-values[k])*(poly.getValue(k)-values[k]);
	    poly.errorValue = errorValue/nonZero.size();
		return poly;
	}
}
