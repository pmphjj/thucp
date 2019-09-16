package model.CPCGA;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.netlib.util.doubleW;
import org.netlib.util.intW;


public abstract class GeneticAlgorithm {
    private List<Chromosome> population = new ArrayList<Chromosome>();
    private HashMap<Integer,Integer> errorOdMap = new HashMap<Integer,Integer>();
    private ArrayList<Patient> patients;
    private ArrayList<CPStage> cpStages;
    /**疾病名称*/
    private String diagName = "";
    /**种群数量*/
    private int POP_SIZE = 100;
    /**染色体最大长度*/
    private int geneSize;
    /**基因颜色数量*/
    private int colorSize;
    /**最大迭代次数*/
    private int MAX_ITER_NUM = 300;
    /**最大纪元次数*/
    private int MAX_EPOCH_ITET_NUM = 10;
    /**基因变异的概率*/
    private double MUTATION_RATE = 0.1;
    /**基因交叉的概率*/
    private double CROSS_RATE = 0.8;
    /**最大变异步长*/
    private int MAX_MUTATION_NUM = 10;
    /**当前遗传到第几代*/
    private int generation = 1;
    /**程序开始运行时间*/
    private long START_TIME;

    private int bestScore;//最好得分
    private int worstScore = Integer.MAX_VALUE;//最坏得分
    private int totalScore;//总得分
    private double totalInRate;//总入径率
    private double totalWScore;//总加权适应度
    private int TOTAL_ITEM_SIZE;//收费项目总数量
    private int SCORE_MAX;//得分上限
    private double averageScore;//平均得分
    private double averageInRate;//平均入径率
    private double averageWScore;//平均加权适应度
    private double[] acRes = new double[2];

    private Chromosome x; //记录历史种群中最好的染色体
    private int geneI;//x y所在代数

    private DynamicDataWindow ddWindow;
    private DynamicDataWindow ddWindow1;
    private DynamicDataWindow ddWindow2;
    private long tp;

    public GeneticAlgorithm(int genes, int colors, String name, int pop_size, int max_iter_num, int max_epoch_num, double cross_rate, double mutate_rate) {
        this.geneSize = genes;
        this.colorSize = colors;
        this.diagName = name;

        this.POP_SIZE = pop_size;
        this.MAX_ITER_NUM = max_iter_num;
        this.MAX_EPOCH_ITET_NUM = max_epoch_num;
        this.CROSS_RATE = cross_rate;
        this.MUTATION_RATE = mutate_rate;
    }

    /**
     * @throws IOException
     * @Description: 启动遗传算法
     */
    public void caculte() throws IOException {
		//1.初始化种群
		init();
    	for(int epoch = 0; epoch < MAX_EPOCH_ITET_NUM; epoch++) {
    		System.out.println("Epoch:"+epoch);
            x = population.get(0);
            for(generation = 1; generation < MAX_ITER_NUM; generation++) {
            	//2.计算种群适应度
            	long millis=System.currentTimeMillis();
            	caculteScore();
            	System.out.println("3>验证阈值...");
                //4.种群遗传
            	long millis1=System.currentTimeMillis();
                evolve();
                //5.基因突变
                long millis2=System.currentTimeMillis();
                mutation();
                long millis3=System.currentTimeMillis();
                print();
                long millis4=System.currentTimeMillis();
                System.out.println((double)(millis1-millis)/1000+","+(double)(millis2-millis1)/1000+","+(double)(millis3-millis2)/1000+","+(double)(millis4-millis3)/1000);
                if(bestScore == SCORE_MAX) break;
            }
            if(bestScore == SCORE_MAX) break;
            //灾变
            disaster();
    	}
        showStatistics();

    }

    private void showStatistics() throws IOException {
		// TODO Auto-generated method stub
		String address = "D:\\hjj\\清华\\实验室工作\\临床诊疗过程符合性判定\\CPLGA\\新res2\\迭代结果统计\\";
		BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(address+"统计.csv"),true)));

		long millis=System.currentTimeMillis();
        wr.write(diagName+","+patients.size()+","+geneSize+","+SCORE_MAX+","+cpStages.size() +","+colorSize +","+generation
        		+","+(double)bestScore/SCORE_MAX +","+ acRes[0]+","+ acRes[1]+","+String.valueOf((double)(millis-START_TIME)/1000));
        wr.newLine();
		wr.close();
	}

	/**
     * @Description: 解码最佳染色体，并展示
     */
    public abstract void show(HashMap<Integer, String> i2cHashMap, HashMap<Integer, String> i2aHashMap, ArrayList<CPStage> cpStages);

    /**
     * 计算准确率指标
     * @return 返回res[0] = 入径率， res[1] = 变异率
     */
    public double[] getAccuracy() {
    	double[] res = new double[2];
    	int pAC = 0, pF = 0;

    	for (int i = 0; i < patients.size(); i++) {
        	ArrayList<Integer> list = patients.get(i).orderList;
        	int day = 0, stage = 0, acOrder = 0, patiIndex = -1, acOrder1 = 0;
        	boolean patiKey = true;
        	for (int j = 0; j < list.size(); j++) {
        		if(list.get(j) == -2) {
        			break;
        		}
				if(list.get(j) == -1) {
					day++;
					continue;
				}
				int odID = list.get(j);
				int activityID = x.gene[odID];
				if (cpStages.get(stage).activityIDSet.contains(activityID)) {
					acOrder++;
					continue;
				}
				int sign = 0, searchStage = stage;
				while(searchStage < cpStages.size() && sign < 2 && !cpStages.get(searchStage).activityIDSet.contains(activityID)) {
					sign++;
					searchStage++;
				}
				if(sign == 1 && searchStage < cpStages.size()) {
					stage = searchStage;
					acOrder++;
					if(patiKey && stage > 0) {
						acOrder1 = acOrder;
						patiIndex = j;
						patiKey = false;
					}
					continue;
				}

			}
        	if(patiIndex != -1 && (double)acOrder1/(patiIndex+1) >= 0.8) {
        		pAC++;
        		if((double)acOrder/list.size() < 0.8) pF++;
        	}
        }

    	res[0] = (double)pAC / patients.size();
    	res[1] = (double)pF / pAC;
    	return res;
    }

    public void getFileForPM(String inputFile, String outputFile, HashMap<String, Integer> c2iHashMap, HashMap<Integer, String> i2aHashMap) throws IOException {
    	BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(inputFile))));
    	BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outputFile))));
    	String line = null;
		line = br.readLine();
		int indexOfPID = -1, indexOfOdName = -1, indexOfTime = -1, indexOfType = -1;
		String[] words = line.split(",");
		for(int i = 0; i < words.length; i++) {
			if(words[i].equals("GRBM")) indexOfPID = i;
			if(words[i].equals("F_MC")) indexOfOdName = i;
			if(words[i].equals("F_RQ")) indexOfTime = i;
			if(words[i].equals("F_LBBM")) indexOfType = i;

		}
		String PID = "", ODNAME = "", TIME = "";
		wr.write("case,event,time");
		wr.newLine();
		while ((line = br.readLine()) != null) {
			words = line.split(",");
			if (indexOfType != -1 && (words[indexOfType].equals("1") || words[indexOfType].equals("4") || words[indexOfType].equals("20")) ) continue;
			PID = words[indexOfPID];
			ODNAME = words[indexOfOdName];
			TIME = words[indexOfTime];
			wr.write(PID+","+i2aHashMap.get(x.gene[c2iHashMap.get(ODNAME)])+","+TIME);
			wr.newLine();
		}

		br.close();
		wr.close();
    }

    public void showErrorOD(HashMap<Integer, String> i2cHashMap) {
		// TODO Auto-generated method stub
    	System.out.println("**************  异常项目列表及出现次数 ****************");
		for (Integer odId:errorOdMap.keySet()) {
			System.out.println(i2cHashMap.get(odId)+":"+errorOdMap.get(odId));
		}
	}

    /**
     * @Description: 输出当前迭代结果
     */
    private void print() {
        System.out.println("--------------------------------");
        System.out.println("迭代次数:" + (generation+1));
        System.out.println("适应度上限:" + SCORE_MAX);
        System.out.println("最佳染色体适应度:" + bestScore);
        System.out.println("最差染色体适应度:" + worstScore);
        System.out.println("当前种群平均适应度:" + averageScore);
//        System.out.println("the total fitness is:" + totalScore);
        System.out.println("收费项目数量（基因长度）:" + geneSize);
        System.out.println("收费记录数量:" + SCORE_MAX);
        System.out.println("国家标准临床路径阶段数量:" + cpStages.size());
        System.out.println("临床路径子活动数量:" + colorSize);
        System.out.println("病人数量:" + patients.size());

        long millis=System.currentTimeMillis();
        System.out.println("运行时间："+ String.valueOf(millis-START_TIME));
        acRes = getAccuracy();
        System.out.println("入径率为："+acRes[0]+",变异率为："+acRes[1]);
        if (millis-tp>300) {
        	tp=millis;
//        	ddWindow.setyMaxMin(y-10);
     		ddWindow.addData(millis, (double)x.getScore()/SCORE_MAX);
     		ddWindow1.addData(millis, acRes[0]);
     		ddWindow2.addData(millis, acRes[1]);
		}

		try {
			Thread.sleep(10L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }


    /**
     * @Description: 初始化种群
     */
    private void init() {
    	System.out.println("1>生成初始种群...");
    	ddWindow.setVisible(true);
    	ddWindow1.setVisible(true);
    	ddWindow2.setVisible(true);

    	population = new ArrayList<Chromosome>();
        for (int i = 0; i < POP_SIZE; i++) {
            Chromosome chro = new Chromosome(geneSize,colorSize);
            population.add(chro);
        }
        int y = 0;
        for(int i = 0; i < patients.size(); i++) {
        	ArrayList<Integer> list = patients.get(i).orderList;
        	for (int j = 0; j < list.size(); j++) {
        		if(list.get(j) == -2) {
        			break;
        		}
				if(list.get(j) == -1) {
					continue;
				}
				y++;
			}
        }
        TOTAL_ITEM_SIZE = y;
        SCORE_MAX = TOTAL_ITEM_SIZE * 1;
        START_TIME = System.currentTimeMillis();
    }

    /**
     * @Description:种群灾变
     */
    private void disaster() {
    	population = new ArrayList<Chromosome>();
    	population.add(x);
        for (int i = 1; i < POP_SIZE; i++) {
            Chromosome chro = new Chromosome(geneSize,colorSize);
            population.add(chro);
        }
    }

    /**
     * @Description:种群进行遗传
     */
    private void evolve() {
        List<Chromosome> childPopulation = new ArrayList<Chromosome>();
        //生成下一代种群
        while (childPopulation.size() < POP_SIZE) {
        	//选择操作
            Chromosome parent1 = getParentChromosome();
            Chromosome parent2 = getParentChromosome();
            //动态设置交叉概率
            int max_parent_score = Math.max(parent1.getScore(), parent2.getScore());
//            if ( max_parent_score < averageScore) {
//				CROSS_RATE = 0.8;
//			}
//            else {
//				CROSS_RATE = 0.7 * (bestScore - max_parent_score) / (double)(bestScore - averageScore);
//			}

            if (Math.random() <= CROSS_RATE) {
            	//交叉操作
            	List<Chromosome> children = Chromosome.genetic(parent1, parent2);
                if (children != null) {
                    for (Chromosome chro : children) {
                    	if(childPopulation.size() < POP_SIZE){
                    		childPopulation.add(chro);
                    	}
                    }
                }
            }
        }
        System.out.println("4.2>产生子代种群...");
        //新种群替换旧种群
        population.clear();
        population = childPopulation;
//        caculteScore();
    }

    /**
     * @return
     * @Description: 轮盘赌法选择可以遗传下一代的染色体
     */
    private Chromosome getParentChromosome (){
//    	System.out.println("4.1>筛选父代种群一次...");

    	if(totalScore == 0) {
    		return population.get((int)(Math.random()*population.size()));
    	}
    	while (true) {
    		double slice = Math.random() * totalScore;
    		double sliceIn = Math.random() * totalInRate;
    		double sliceW = Math.random() * totalWScore;

            int sum = 0;
            double sumInRate = 0.0;
            double sumW = 0.0;
            for (Chromosome chro : population) {
                sum += chro.getScore();
                sumInRate += chro.getInRate();
                sumW += chro.getWScore();
//                System.out.println("父代轮次："+ generation+"  测试：sum="+sum+"  chro.getScore()="+chro.getScore());
                if (totalWScore == 0 && sum >= slice && chro.getScore() >= averageScore) {
//                	if (sliceIn > 0.0) {
//						if (chro.getInRate() >= averageInRate) {
//							return chro;
//						}
//					}
                	return chro;
                }
                else if (totalWScore != 0 && sumW >= sliceW && chro.getWScore() >= averageWScore) {
					return chro;
				}
            }
		}
    }

    /**
     * @Description: 计算种群适应度
     */
    private void caculteScore() {
    	System.out.println("2>计算种群适应度...");
    	bestScore = population.get(0).getScore();
    	worstScore = Math.min(worstScore, population.get(0).getScore());
        totalScore = 0;
        totalInRate = 0.0;
        totalWScore = 0.0;
        for (Chromosome chro : population) {
            chro = setChromosomeScore(chro);
            if (chro.getScore() > bestScore) { //设置最好基因值
                bestScore = chro.getScore();
                x = chro;
                geneI = generation;
            }
            if (chro.getScore() < worstScore) { //设置最坏基因值
                worstScore = chro.getScore();
            }
            totalScore += chro.getScore();
            totalInRate += chro.getInRate();
            totalWScore += chro.getWScore();
        }
        averageScore = totalScore / POP_SIZE;
        averageInRate = totalInRate / POP_SIZE;
        averageWScore = totalWScore / POP_SIZE;
        //因为精度问题导致的平均值大于最好值，将平均值设置成最好值
        averageScore = averageScore > bestScore ? bestScore : averageScore;
    }

    /**
     * 基因突变
     */
    private void mutation() {
    	System.out.println("5>基因突变...");
        for (Chromosome chro : population) {
        	//设置动态的变异概率
//        	if(chro.getScore() >= averageScore){
//        		MUTATION_RATE = 0.1*(bestScore-chro.getScore())/(double)(bestScore-averageScore);
//        	}
//        	else {
//        		MUTATION_RATE = 0.2;
//			}
            if (Math.random() < MUTATION_RATE) { //发生基因突变
                int mutationNum = (int) (Math.random() * MAX_MUTATION_NUM);
                chro.mutation(mutationNum,colorSize);
            }
        }
    }

    /**
     * @param chro
     * @Description: 计算并设置染色体适应度
     */
    private Chromosome setChromosomeScore(Chromosome chro) {

        if (chro == null) {
            return null;
        }
        double[] res = new double[2];
    	int pAC = 0, pF = 0;// pAC 入径病人数，pF 变异病人数
        int y = 0;
        for(int i = 0; i < patients.size(); i++) {
        	ArrayList<Integer> list = patients.get(i).orderList;
        	int day = 0, stage = 0, acOrder = 0, patiIndex = -1, acOrder1 = 0;
        	boolean patiKey = true;
        	for (int j = 0; j < list.size(); j++) {
        		if(list.get(j) == -2) {
        			break;
        		}
				if(list.get(j) == -1) {
					day++;
					continue;
				}
				int odID = list.get(j);
				int activityID = chro.gene[odID];
				if (cpStages.get(stage).activityIDSet.contains(activityID)) {
					y++;
					acOrder++;
					continue;
				}
				int sign = 0, searchStage = stage;
				while(searchStage < cpStages.size() && sign < 2 && !cpStages.get(searchStage).activityIDSet.contains(activityID)) {
					sign++;
					searchStage++;
				}
				if(sign == 1 && searchStage < cpStages.size()) {
					stage = searchStage;
					y++;
					acOrder++;
					if(patiKey && stage > 0) {
						acOrder1 = acOrder;
						patiIndex = j;
						patiKey = false;
					}

					continue;
				}
				else {
					if (errorOdMap.containsKey(odID)) {
						errorOdMap.put(odID, errorOdMap.get(odID)+1);
					}
					else {
						errorOdMap.put(odID, 1);
					}
				}
			}
        	if(patiIndex != -1 && (double)acOrder1/(patiIndex+1) >= 0.8) {
        		pAC++;
        		if((double)acOrder/list.size() < 0.8) pF++;
        	}
        }

        res[0] = (double)pAC / patients.size();
    	res[1] = (double)pF / pAC;
        chro.setScore(y);
        chro.setRes(res);
        chro.setWScore((double)y*res[0]);
        return chro;

    }
    //
    private void setChromosomeScore1(Chromosome chro) {
        if (chro == null) {
            return;
        }
        int y = 0;
        for(int i = 0; i < patients.size(); i++) {
        	ArrayList<Integer> list = patients.get(i).orderList;
        	int day = 0, stage = 0;
        	for (int j = 0; j < list.size(); j++) {
        		if(list.get(j) == -2) {
        			y++;
        			break;
        		}
				if(list.get(j) == -1) {
					day++;
					continue;
				}
				int activityID = chro.gene[list.get(j)];
				while(stage<cpStages.size() && !cpStages.get(stage).activityIDSet.contains(activityID) ) {
					stage++;
				}
				if(stage >= cpStages.size()) break;

			}
        }
        chro.setScore(y);

    }

    /**
     * @param chro
     * @return
     * @Description: 将二进制转化为对应的X
     */
//    public abstract int changeX(Chromosome chro);


    /**
     * @param x
     * @return
     * @Description: 根据X计算Y值 Y=F(X)
     */
//    public abstract double caculateY(int x1, int x2);

    public void setPopulation(List<Chromosome> population) {
        this.population = population;
    }

    public void setPopSize(int popSize) {
        this.POP_SIZE = popSize;
    }

    public void setGeneSize(int geneSize) {
        this.geneSize = geneSize;
    }

    public void setMaxIterNum(int maxIterNum) {
        this.MAX_ITER_NUM = maxIterNum;
    }

    public void setMutationRate(double mutationRate) {
        this.MUTATION_RATE = mutationRate;
    }

    public void setMaxMutationNum(int maxMutationNum) {
        this.MAX_MUTATION_NUM = maxMutationNum;
    }

    public double getBestScore() {
        return bestScore;
    }

    public double getWorstScore() {
        return worstScore;
    }

    public double getTotalScore() {
        return totalScore;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public double getWTotalScore() {
        return totalWScore;
    }

    public double getAverageWScore() {
        return averageWScore;
    }

    public Chromosome getX() {
        return x;
    }

    public DynamicDataWindow getDdWindow() {
		return ddWindow;
	}
    public DynamicDataWindow getDdWindow1() {
    	return ddWindow1;
    }
    public DynamicDataWindow getDdWindow2() {
    	return ddWindow2;
    }

    public void setDdWindow(DynamicDataWindow ddWindow) {
		this.ddWindow = ddWindow;
	}
    public void setDdWindow1(DynamicDataWindow ddWindow1) {
    	this.ddWindow1 = ddWindow1;
    }
    public void setDdWindow2(DynamicDataWindow ddWindow2) {
    	this.ddWindow2 = ddWindow2;
    }

	public void setPatients(ArrayList<Patient> patients2) {
		// TODO Auto-generated method stub
		this.patients = patients2;
	}

	public void setCPStages(ArrayList<CPStage> cpStages2) {
		// TODO Auto-generated method stub
		this.cpStages = cpStages2;
	}

	public Chromosome getBestChromosome() {
		// TODO Auto-generated method stub
		return x;
	}
}
