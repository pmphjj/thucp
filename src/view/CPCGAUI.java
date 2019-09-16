package view;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import org.netlib.util.intW;
import org.openrdf.console.Show;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.CPCGA.CPStage;
import model.CPCGA.Chromosome;
import model.CPCGA.Patient;
import model.CPCGA.MainCPCGA;
import model.CPCGA.DynamicDataWindow;
import model.CPMRM.ClinicalOrderReduction;
import model.CPMRM.DBUtil;
import model.CPMRM.ExcelUtil;
import model.CPMRM.ImportCPUtil;
import model.CPMRM.StandardCPStage;

public class CPCGAUI {
	Stage stage;
	Scene scene;
	MenuItem menuItem;
	ToolBar toolBar;
	TabPane tabPane;
	Label stateLabel;

	File id2itemFile;
	File pditemFile;
	ArrayList<String[]> id2itemArray;
	ArrayList<String[]> pditemArray;
	String clinicalOrder[][];
	HashMap<Integer, String> i2aHashMap;
	HashMap<Integer, String> i2cHashMap;

	int pop_size = 100;
	int max_iter_num = 300;
	int max_epoch_num = 10;
	double cross_rate = 0.8;
	double mutate_rate = 0.1;

	public CPCGAUI(Stage stage, MenuItem menuItem, TabPane tabPane) {
		this.stage = stage;
		this.menuItem = menuItem;
		this.tabPane = tabPane;
		this.i2aHashMap = new HashMap<Integer,String>(); //临床路径表单项 map
		this.i2cHashMap = new HashMap<Integer,String>(); //收费项 map

		menuItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					new FuncParamInputDataSetDialog().setParamInputDataSet();

					System.out.println(FrameworkMain.paramInputDataSetOfFunc.size());

					if(FrameworkMain.paramInputDataSetOfFunc.size() > 0){
						Iterator<Entry<String,InputData>> iter = FrameworkMain.paramInputDataSetOfFunc.entrySet().iterator();
						StandardCPStage[] CPS = null;
						ArrayList<Patient> patientsData = new ArrayList<Patient>();
						ArrayList<CPStage> cpStages = new ArrayList<CPStage>();
						String file_name = "";

						ArrayList<String> deleteDetail = new ArrayList<String>();

				        while(iter.hasNext()){
				            Entry<String, InputData> entry = iter.next();
				            String key = entry.getKey();
				            InputData value = entry.getValue();
				            if(value.getType().equals("stdClinicalPathway")) {
				            	file_name = value.getName();
				            	CPS = value.getDataForCP();
								cpStages = transformStages(CPS);

				            }
				            else if (value.getType().equals("orders")) {
				            	patientsData = value.getDataForPatients();
				            	i2cHashMap = value.cpcgaIO.getIToCMap();
				            	deleteDetail = value.cpcgaIO.getdeleteDetail();
							}
				            else
				            {
								System.out.println("没有可用类型的文件");
							}
				        }
				        //展示国家标准临床路径
						importStandardCP(CPS);
						//设置参数
						setArgs();
						//执行遗传算法
						MainCPCGA mainCPCGA = GA(cpStages,patientsData,file_name);
						//展示本地化临床路径的结果
						show(mainCPCGA,cpStages,file_name,deleteDetail);

//						orderReduction();
					}
					else {
						Alert alert = new Alert(AlertType.ERROR);
                		alert.setTitle("错误");
                		alert.setContentText("请选择输入数据");
                		alert.showAndWait();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				importDBData(b5);
			}

			private void setArgs() {

				Stage window = new Stage();
			    window.setTitle("配置参数");
			    //modality要使用Modality.APPLICATION_MODEL
			    window.initModality(Modality.APPLICATION_MODAL);
			    window.setMinWidth(400);
			    window.setMinHeight(400);


			    //填写连接数据库信息区域
			    GridPane topGrid = new GridPane();
			    topGrid.setAlignment(Pos.CENTER);
			    topGrid.setHgap(10);
			    topGrid.setVgap(10);
			    topGrid.setPadding(new Insets(25, 25, 25, 25));

			    Text scenetitle = new Text("设置遗传算法参数信息");
			    scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
			    topGrid.add(scenetitle, 0, 0, 2, 1);


			    Label pop_lab = new Label("种群大小:");
			    topGrid.add(pop_lab, 0, 1);
			    TextField pop_TextField = new TextField("100");
			    topGrid.add(pop_TextField, 1, 1);

			    Label iter_lab = new Label("每纪元迭代次数:");
			    topGrid.add(iter_lab, 0, 2);
			    TextField iter_TextField = new TextField("300");
			    topGrid.add(iter_TextField, 1, 2);

			    Label epoch_lab = new Label("纪元次数:");
			    topGrid.add(epoch_lab, 0, 3);
			    TextField epoch_TextField = new TextField("10");
			    topGrid.add(epoch_TextField, 1, 3);

			    Label cross_lab = new Label("交叉概率:");
			    topGrid.add(cross_lab, 0, 4);
			    TextField cross_TextField = new TextField("0.8");
			    topGrid.add(cross_TextField, 1, 4);

			    Label mutate_lab = new Label("变异概率:");
			    topGrid.add(mutate_lab, 0, 5);
			    TextField mutate_TextField = new TextField("0.1");
			    topGrid.add(mutate_TextField, 1, 5);

			    Button btn = new Button("完成");
			    HBox hbBtn = new HBox(10);
			    hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
			    hbBtn.getChildren().add(btn);
			    topGrid.add(hbBtn, 1, 7);



			    btn.setOnAction(new EventHandler<ActionEvent>() {

			        @Override
			        public void handle(ActionEvent e) {
			        	pop_size = Integer.valueOf(pop_TextField.getText());
			        	max_iter_num = Integer.valueOf(iter_TextField.getText());
			        	max_epoch_num = Integer.valueOf(epoch_TextField.getText());
			        	cross_rate = Double.valueOf(cross_TextField.getText());
			        	mutate_rate = Double.valueOf(mutate_TextField.getText());
			        	window.close();


			        }
			    });


			    Scene scene = new Scene(topGrid);
			    window.setScene(scene);
			    //使用showAndWait()先处理这个窗口，而如果不处理，main中的那个窗口不能响应
			    window.showAndWait();

			    Tab tab1 = null;
				for (int i = 0; i < tabPane.getTabs().size(); i++) {
					String title = tabPane.getTabs().get(i).getText();
					if (title.equals("参数设置")) {
//						tab2 = tabPane.getTabs().get(i);
						tabPane.getTabs().remove(i);
					}
				}

				if (tab1 == null) {
					tab1 = new Tab("参数设置");
					String res = "";
					res = res + "种群大小："+ pop_size + "\n";
					res = res + "每纪元迭代次数："+ max_iter_num + "\n";
					res = res + "纪元次数："+ max_epoch_num + "\n";
					res = res + "交叉概率："+ String.valueOf(cross_rate) + "\n";
					res = res + "变异概率："+ String.valueOf(mutate_rate) + "\n";
					Text resulText = new Text(1000,1000,"\n"+res);
					tab1.setContent(resulText);
					tabPane.getTabs().add(tab1);
				}
				tabPane.getSelectionModel().select(tab1);
			}

			private void show(MainCPCGA mainCPCGA,ArrayList<CPStage> cpStages,String cpName, ArrayList<String> deleteDetail){
		        Chromosome bestChro = mainCPCGA.getBestChromosome();
				ArrayList<Integer>[] actList = new ArrayList[i2aHashMap.size()];
				for (int i = 0; i < actList.length; i++) {
					actList[i] = new ArrayList<Integer>();
				}
				for (int i = 0; i < bestChro.gene.length; i++) {
					actList[bestChro.gene[i]].add(i);
				}
				String deleteStringInfo = "";
				for (int i = 0; i < deleteDetail.size(); i++) {
					deleteStringInfo += deleteDetail.get(i);
					deleteStringInfo += "\n";
				}

				String newcpInfo = "";
				for (int i = 0; i < cpStages.size(); i++) {
					newcpInfo += "阶段 "+(i+1)+"  ******************************\n";

					for(Integer activityId:cpStages.get(i).activityIDSet){
						newcpInfo = newcpInfo + i2aHashMap.get(activityId)+":   ";
						for (int j = 0; j < actList[activityId].size(); j++) {
							int orderId = actList[activityId].get(j);
							newcpInfo += i2cHashMap.get(orderId);
							if (j != actList[activityId].size()-1) {
								newcpInfo += ", ";
							}
						}
						newcpInfo += "\n\n";
					}
				}


				Tab tab2 = null;
				for (int i = 0; i < tabPane.getTabs().size(); i++) {
					String title = tabPane.getTabs().get(i).getText();
					if (title.equals("输出结果")) {
//						tab2 = tabPane.getTabs().get(i);
						tabPane.getTabs().remove(i);
					}
				}

				if (tab2 == null) {
					tab2 = new Tab("输出结果");
					Text resulText = new Text("\n"+newcpInfo);
					ScrollPane s1 = new ScrollPane();
					s1.setContent(resulText);
					tab2.setContent(s1);
					tabPane.getTabs().add(tab2);
				}
				tabPane.getSelectionModel().select(tab2);
			}
			private ArrayList<CPStage> transformStages(StandardCPStage[] cps) {
				ArrayList<CPStage> cpStages = new ArrayList<CPStage>();
				int index = 0;
				HashMap<String, Integer> a2iHashMap = new HashMap<String,Integer>();
				for (int i = 0; i < cps.length; i++) {
					CPStage tempStage = new CPStage();
					for (int j = 0; j < cps[i].coreOrdersLong.size(); j++) {
						String temp = cps[i].coreOrdersLong.get(j);
						if (!a2iHashMap.containsKey(temp)) {
							a2iHashMap.put(temp, index);
							i2aHashMap.put(index, temp);
							index++;
						}
						tempStage.activityNameSet.add(temp);
						tempStage.activityIDSet.add(a2iHashMap.get(temp));
					}
					for (int j = 0; j < cps[i].coreOrdersCur.size(); j++) {
						String temp = cps[i].coreOrdersCur.get(j);
						if (!a2iHashMap.containsKey(temp)) {
							a2iHashMap.put(temp, index);
							i2aHashMap.put(index, temp);
							index++;
						}
						tempStage.activityNameSet.add(temp);
						tempStage.activityIDSet.add(a2iHashMap.get(temp));
					}
					for (int j = 0; j < cps[i].coreOrdersOut.size(); j++) {
						String temp = cps[i].coreOrdersOut.get(j);
						if (!a2iHashMap.containsKey(temp)) {
							a2iHashMap.put(temp, index);
							i2aHashMap.put(index, temp);
							index++;
						}
						tempStage.activityNameSet.add(temp);
						tempStage.activityIDSet.add(a2iHashMap.get(temp));
					}
					cpStages.add(tempStage);
				}
				return cpStages;
			}

			private MainCPCGA GA(ArrayList<CPStage> cpStages, ArrayList<Patient> patientsData, String file_name) throws IOException {
				HashSet<Integer> actSets = new HashSet<Integer>();
				HashSet<Integer> ordersSets = new HashSet<Integer>();
				for (int i = 0; i < cpStages.size(); i++) {
					for (Integer gene : cpStages.get(i).activityIDSet) {
						actSets.add(gene);
					}
				}
				for (int i = 0; i < patientsData.size(); i++) {
					for (Integer order : patientsData.get(i).orderList) {
						if (order >= 0) {
							ordersSets.add(order);
						}
					}
				}

				int genesize = ordersSets.size();
				int actsSize = actSets.size();
				MainCPCGA test = new MainCPCGA(genesize,actsSize,file_name,pop_size,max_iter_num,max_epoch_num,cross_rate,mutate_rate);
				test.setPatients(patientsData);
				test.setCPStages(cpStages);
				test.setDdWindow(new DynamicDataWindow("基于遗传算法的临床路径挖掘"+"---"+file_name+"——适应度曲线"));
				test.setDdWindow1(new DynamicDataWindow("基于遗传算法的临床路径挖掘"+"---"+file_name+"——入径率曲线"));
				test.setDdWindow2(new DynamicDataWindow("基于遗传算法的临床路径挖掘"+"---"+file_name+"——变异率曲线"));
		        test.caculte();
		        return test;

			}

			private void orderReduction() {
				// TODO Auto-generated method stub

				ClinicalOrderReduction crd = new ClinicalOrderReduction();
				clinicalOrder = crd.textReduction(clinicalOrder,0.80);
				clinicalOrder = crd.semanticReduction(clinicalOrder);

				Tab tab2 = null;
				for (int i = 0; i < tabPane.getTabs().size(); i++) {
					String title = tabPane.getTabs().get(i).getText();
					if (title.equals("医嘱消解结果")) {
//						tab2 = tabPane.getTabs().get(i);
						tabPane.getTabs().remove(i);
					}
				}

				if (tab2 == null) {
					tab2 = new Tab("医嘱消解结果");
					Text resulText = new Text(1000,1000,"\n"+crd.getResultBuffer());
					tab2.setContent(resulText);
					tabPane.getTabs().add(tab2);
				}
				tabPane.getSelectionModel().select(tab2);
			}


			private void importStandardCP(StandardCPStage[] CPS) {
				// TODO Auto-generated method stub
//				FileChooser fileChooser = new FileChooser();
//				fileChooser.setTitle("选择要导入的国家标准临床路径 (Json文件)");
//				File file = fileChooser.showOpenDialog(stage);
//
//				ImportCPUtil imcp = new ImportCPUtil();
				try {
//					CPS = imcp.readJsonInput(file.getAbsolutePath());

					final String[] itemHeader = { "时间", "主要诊疗工作","重点医嘱","主要护理工作"};

					TableView<ObservableList<String>> itemTV = new TableView<>();
					ObservableList<ObservableList<String>> itemData = FXCollections.observableArrayList();
					int i ;
					for ( i = 0;i<CPS.length;++i)
					{
						itemData.add(FXCollections.observableArrayList(CPS[i].getAllContent()));
					}
					itemTV.setItems(itemData);
					for ( i = 0; i < itemHeader.length; i++) {
						final int curCol = i;
						final TableColumn<ObservableList<String>, String> column = new TableColumn<>(itemHeader[curCol]);
						column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(curCol)));

						column.setSortable(false);
						itemTV.getColumns().add(column);
					}

//					Tab tab0 = new Tab("国家标准临床路径");
//                  tab0.setContent(itemTV);
//                  tabPane.getTabs().add(tab0);
//                  tabPane.getSelectionModel().select(tab0);

					Tab tab0 = null;
					for (i = 0; i < tabPane.getTabs().size(); i++) {
						String title = tabPane.getTabs().get(i).getText();
						if (title.equals("国家标准临床路径")) {
//							tab0 = tabPane.getTabs().get(i);
							tabPane.getTabs().remove(i);
						}
					}
					if (tab0 == null) {
						tab0 = new Tab("国家标准临床路径");
						tab0.setContent(itemTV);
						tabPane.getTabs().add(tab0);
					}
					tabPane.getSelectionModel().select(tab0);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
	}



	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
