package com.db;

//测试中
//多项式曲线拟合预测未来两个月房价
public class Foresee {
	//该函数用于预测未来两个月的房价，并将预测结果储存在values中，20190828龚灿，测试成功
	//输入为values数组，values[0]到values[11]是过去一年的数据
	static void foresee(int[] values) {
		Polynomial[] polynomials = new Polynomial[10];
		//从一次函数预测到十次函数
		for(int i=0;i<10;i++)
			polynomials[i] = curveFitting(i+1,values);
		Polynomial bestPoly = polynomials[0];
		//选取误差值最小的多项式拟合曲线
		for(int i=1;i<10;i++)
			if(bestPoly.errorValue > polynomials[i].errorValue)
				bestPoly = polynomials[i];
		values[12] = (int)bestPoly.getValue(12);
		values[13] = (int)bestPoly.getValue(13);
	}
	
	//该函数用于进行多项式曲线拟合，并将多项式返回，20190829龚灿，测试成功
	//输入为多项式最高次n和values数组，values[0]到values[11]是过去一年的数据
	static Polynomial curveFitting(int n, int[] values) {
		Matrix leftMatrix = new Matrix(n+1, n+1);
		Matrix rightMatrix = new Matrix(n+1, 1);
		for(int i=1;i<=leftMatrix.xsize;i++) {
			for(int j=1;j<=leftMatrix.ysize;j++) {
				int ijVal = 0;
				for(int k=0;k<12;k++) {
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
			for(int k=0;k<12;k++) {
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
		for(int i=0;i<=n;i++)
			poly.coefficients[i] = coefficientMatrix.matrix[i+1][1];
	    double errorValue = 0.0;
	    for(int i=0;i<12;i++)
	    	errorValue += (poly.getValue(i)-values[i])*(poly.getValue(i)-values[i]);
	    poly.errorValue = errorValue/12;
		return poly;
	}
}
