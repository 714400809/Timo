package com.db;

//此类专用于生成静态页面时根据楼盘名缩写生成对应网址，测试成功
//创建人：龚灿	创建时间：20190828	最近更新时间：20190829
public class Matrix {
	double[][] matrix;//矩阵
	int xsize;//行数
	int ysize;//列数
	
	public Matrix(int xs, int ys) {
		if(xs>0 && ys>0) {
			xsize = xs;
			ysize = ys;
			matrix = new double[xsize+1][ysize+1];
			for(int i=0;i<=xsize;i++)
				for(int j=0;j<=ysize;j++)
					matrix[i][j] = 0;
		}
		else matrix=null;
	}

	//该函数用于计算方阵的逆矩阵，测试成功
	//创建人：龚灿	创建时间：20190828	最近更新时间：20190828
	//无需输入，输出为逆矩阵
	Matrix getInverseMatrix() {
		if(xsize!=ysize)
			return null;
		double div = getDeterminant();
		if(div==0)
			return null;
		Matrix inverserMatrix = new Matrix(xsize, ysize);
		for(int i=1;i<=xsize;i++)
			for(int j=1;j<=ysize;j++)
				inverserMatrix.matrix[i][j] = getCofactor(j, i)/div;
		return inverserMatrix;
	}
	
	//该函数为矩阵乘法函数，测试成功
	//创建人：龚灿	创建时间：20190828	最近更新时间：20190828
	//输入为另一个相乘矩阵，输出为相乘的积矩阵
	Matrix multMatrix(Matrix other) {
		if(ysize!=other.xsize)
			return null;
		Matrix multResult = new Matrix(xsize, other.ysize);
		for(int i=1;i<=multResult.xsize;i++)
			for(int j=1;j<=multResult.ysize;j++)
				for(int k=1;k<=ysize;k++)
					multResult.matrix[i][j] += matrix[i][k]*other.matrix[k][j];
		return multResult;
	}
	
	//该函数用于计算矩阵对应的行列式的值（仅适用于行列相等），测试成功
	//创建人：龚灿	创建时间：20190828	最近更新时间：20190829
	//无需输入，输出为行列式的值
	private double getDeterminant() {
		if(xsize!=ysize)
			return 0;
		Matrix data = new Matrix(xsize, ysize);
		for(int i=1;i<=xsize;i++)
			for(int j=1;j<=ysize;j++)
				data.matrix[i][j] = matrix[i][j];
        //标记是否可以提前确定行列式为0
        boolean zero = false;
        //记录交换行后的正负号
        double sign = 1;
        //主体
        for (int i=1; i<=xsize; i++){
            //如果第i行第i个元素已经是0的话，就去找一行非零的行交换
            if (data.matrix[i][i] == 0) {
                boolean flag = false;
                for (int j = i + 1; j <= xsize; j++){
                    if (data.matrix[j][i] != 0){
                        flag = true;
                        for (int a = i; a<=xsize; a++){
                            double temp = data.matrix[i][a];
                            data.matrix[i][a] = data.matrix[j][a];
                            data.matrix[j][a] = temp;
                        }
                        sign *= -1;
                        break;
                    }
                }
                //如果一列全为0，那么行列式为0
                if (!flag)
                {
                    zero = true;
                    break;
                }
            }
            for (int j = i + 1; j <= xsize; j++){
                double snt;
                if (data.matrix[j][i] == 0)
                    continue;
                snt = data.matrix[j][i] / data.matrix[i][i];
                for (int k = i; k <= ysize; k++){
                    data.matrix[j][k] += (-snt * data.matrix[i][k]);
                }
            }
        }
        double result;
        //整理答案并返回
        if (zero)
            result = 0;
        else{
            result = sign;
            for (int i = 1; i <= ysize; i++){
                result *= data.matrix[i][i];
            }
        }
        return result;
	}
	
	//该函数用于计算代数余子式，测试成功
	//创建人：龚灿	创建时间：20190828	最近更新时间：20190829
	//输入为删除的第行数与列数，输出为代数余子式的值
	private double getCofactor(int i, int j) {
		Matrix cofactor = new Matrix(xsize-1,ysize-1);
		for(int a=1;a<i;a++) {
			for(int b=1;b<j;b++)
				cofactor.matrix[a][b] = matrix[a][b];
			for(int b=j+1;b<=ysize;b++)
				cofactor.matrix[a][b-1] = matrix[a][b];
		}
		for(int a=i+1;a<=xsize;a++) {
			for(int b=1;b<j;b++)
				cofactor.matrix[a-1][b] = matrix[a][b];
			for(int b=j+1;b<=ysize;b++)
				cofactor.matrix[a-1][b-1] = matrix[a][b];
		}
		double result = 1;
		if((i+j)%2==1)
			result = -1;
		result *= cofactor.getDeterminant();
		return result;
	}
}
