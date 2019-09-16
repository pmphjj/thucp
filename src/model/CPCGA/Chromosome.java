package model.CPCGA;
import java.util.*;
import java.lang.*;

public class Chromosome {
	public int[] gene;//基因序列
    public int score;//对应的函数得分
    public double wScore;//score * res[0] , 通过的收费项目数量*入径率
    public double res[] = new double[2];

    public Chromosome() {
		// TODO Auto-generated constructor stub
	}

    public Chromosome(int size, int colorSize) {  // size:序列长度——医疗项目数目， colorSize:颜色数量——国家标准临床路径子活动数目
        if (size <= 0) {
            return;
        }
        initGeneSize(size);
        for (int i = 0; i < size; i++) {
            gene[i] =(int)(Math.random()*colorSize);
        }
    }

    private void initGeneSize(int size) {
        if (size <= 0) {
            return;
        }
        gene = new int[size];
    }

    /**
     * @return 返回该染色体的适应度值
     */
    public int getScore() {
        return score;
//    	return wScore;
    }



    /**
     * @param c
     * @return
     * @Description: 克隆基因
     */
    public static Chromosome clone(final Chromosome c) {
        if (c == null || c.gene == null) {
            return null;
        }
        Chromosome copy = new Chromosome();
        copy.initGeneSize(c.gene.length);
        for (int i = 0; i < c.gene.length; i++) {
            copy.gene[i] = c.gene[i];
        }
        return copy;
    }


    /**
     * @param c1
     * @param c2
     * @Description: 遗传产生下一代
     */
    public static List<Chromosome> genetic(Chromosome p1, Chromosome p2) {
        if (p1 == null || p2 == null) { //染色体有一个为空，不产生下一代
            return null;
        }
        if (p1.gene == null || p2.gene == null) { //染色体有一个没有基因序列，不产生下一代
            return null;
        }
        if (p1.gene.length != p2.gene.length) { //染色体基因序列长度不同，不产生下一代
            return null;
        }
        Chromosome c1 = clone(p1);
        Chromosome c2 = clone(p2);
        //随机产生交叉互换位置
        int size = c1.gene.length;
        int a = ((int) (Math.random() * size)) % size;
        int b = ((int) (Math.random() * size)) % size;
        int min = a > b ? b : a;
        int max = a > b ? a : b;
        //对位置上的基因进行交叉互换
        int t;
        for (int i = min; i <= max; i++) {
            t = c1.gene[i];
            c1.gene[i] = c2.gene[i];
            c2.gene[i] = t;
        }
        List<Chromosome> list = new ArrayList<Chromosome>();
        list.add(c1);
        list.add(c2);
        return list;
    }

    /**
     * @param num
     * @Description: 基因num个位置发生变异
     */
    public void mutation(int num, int colorSize) {
        //允许变异
        int size = gene.length;
        for (int i = 0; i < num; i++) {
            //寻找变异位置
            int at = ((int)(Math.random() * size)) % size;
            //变异后的值
            gene[at] = (int)(Math.random() * colorSize);
        }
    }

    /**
     * @param y
     * @Description:设置该染色体的适应度值
     */
    public void setScore(int y) {
    	// TODO Auto-generated method stub
    	this.score = y;
    }

    public static void main(String[] args) {
    	Chromosome chr = new Chromosome(10, 5);
    	for(int i = 0; i < chr.gene.length; i++) {
    		System.out.println(chr.gene[i]);
    	}


    }

	public void setRes(double[] res2) {
		// TODO Auto-generated method stub
		for (int i = 0; i < res2.length; i++) {
			this.res[i] = res2[i];
		}
	}

	public double getInRate() {
		// TODO Auto-generated method stub
		return this.res[0];
	}

	public double getWScore() {
		// TODO Auto-generated method stub
		return this.wScore;
	}

	public void setWScore(double d) {
		// TODO Auto-generated method stub
		this.wScore = d;
	}

}
