package view;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.processmining.log.csvimport.exception.CSVConversionException;

import java.util.Map.Entry;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.DirectoryChooserBuilder;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import outlier.cluster.GetLabelForCluster;
import outlier.cluster.KmeansPlusPlus;
import outlier.cluster.Point;
import outlier.launch.AlignLaunch;
import outlier.launch.IMLaunch;
import outlier.topic.GetTopicBasedOnLDA;
import outlier.util.FilesOfFolderDelete;
import test.TestVisualAlign;

public class FuncOutlieDetectionUI {
	Stage stage;
	MenuItem menuwei1;
	TabPane funcTabPane;

	ArrayList<String[]> topicId2ItemsArray;
	ArrayList<String[]> doc2Topic;

	int KLDA = 13;
	int KKmeans;
	int KmeansRoundTimes;

	public FuncOutlieDetectionUI(Stage stage, MenuItem menuwei1, TabPane funcTabPane) {
		this.stage = stage;
		this.menuwei1 = menuwei1;
		this.funcTabPane = funcTabPane;
		setActions();
	}

	public void setActions() {
		SetMenuOutlierDetectionAction();
	}

	public void SetMenuOutlierDetectionAction() {
		menuwei1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					new FuncParamInputDataSetDialog().setParamInputDataSet();
					setParams();
					generateCorpusForLDA("data/OutlierDetection/corpus/");
					runLDA("data/OutlierDetection/corpus/");
					runKmeansPlusPlus();
					runAlign("data/OutlierDetection/cluster/paper_log.csv");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	public void setParams() {
		TextInputDialog dialog = new TextInputDialog("");
		dialog.setTitle("设置参数");
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField KofLDA = new TextField("13");
		// KTF.setPromptText("K");
		KofLDA.setPrefColumnCount(5);
		grid.add(new Label("K of LDA: "), 0, 0);
		grid.add(KofLDA, 1, 0);

		TextField KofKmeans = new TextField("14");
		KofKmeans.setPrefColumnCount(5);
		TextField KmeansRoundTimesParam = new TextField("100");
		KmeansRoundTimesParam.setPrefColumnCount(5);

		grid.add(new Label("K of Kmeans: "), 0, 1);
		grid.add(KofKmeans, 1, 1);
		grid.add(new Label("Kmeans运行次数: "), 0, 2);
		grid.add(KmeansRoundTimesParam, 1, 2);

		dialog.getDialogPane().setContent(grid);
		Platform.runLater(() -> KofKmeans.requestFocus());
		dialog.showAndWait().ifPresent(response -> {
			KLDA = Integer.parseInt(KofLDA.getText());
			KKmeans = Integer.parseInt(KofKmeans.getText());
			KmeansRoundTimes = Integer.parseInt(KmeansRoundTimesParam.getText());
		});
	}
	// public void runIM(String logPath) throws CSVConversionException {
	// IMLaunch.lauch(logPath);
	// Tab IMPan = null;
	// for (int i = 0; i < funcTabPane.getTabs().size(); i++) {
	// String title = funcTabPane.getTabs().get(i).getText();
	// if (title.equals("IM")) {
	// IMPan = funcTabPane.getTabs().get(i);
	// }
	// }
	// if (IMPan == null) {
	// IMPan = new Tab("IM");
	// }
	//
	// final SwingNode swingNode = new SwingNode();
	// JComponent jc=IMLaunch.resultJComponent;
	// swingNode.setContent(jc);
	//
	// IMPan.setContent(swingNode);
	//
	// funcTabPane.getTabs().add(IMPan);
	// funcTabPane.getSelectionModel().select(IMPan);
	// }

	public void runAlign(String logPath) throws Exception {
		AlignLaunch.lauch(logPath);

		Tab IMPan = null;
		for (int i = 0; i < funcTabPane.getTabs().size(); i++) {
			String title = funcTabPane.getTabs().get(i).getText();
			if (title.equals("IM")) {
				IMPan = funcTabPane.getTabs().get(i);
				final SwingNode swingNodeIM = new SwingNode();
				JComponent jc = AlignLaunch.resultIM;
				swingNodeIM.setContent(jc);
				IMPan.setContent(swingNodeIM);
			}
		}
		if (IMPan == null) {
			IMPan = new Tab("IM");
			final SwingNode swingNodeIM = new SwingNode();
			JComponent jc = AlignLaunch.resultIM;
			swingNodeIM.setContent(jc);
			IMPan.setContent(swingNodeIM);
			funcTabPane.getTabs().add(IMPan);
		}

		Tab alignPan = null;
		for (int i = 0; i < funcTabPane.getTabs().size(); i++) {
			String title = funcTabPane.getTabs().get(i).getText();
			if (title.equals("alignment")) {
				alignPan = funcTabPane.getTabs().get(i);
				JPanel pan = (JPanel) AlignLaunch.resultAlignment;
				final SwingNode swingNode = new SwingNode();
				swingNode.setContent(pan);
				alignPan.setContent(swingNode);
				funcTabPane.getSelectionModel().select(alignPan);
			}
		}

		if (alignPan == null) {
			alignPan = new Tab("alignment");
			JPanel pan = (JPanel) AlignLaunch.resultAlignment;
			final SwingNode swingNode = new SwingNode();
			swingNode.setContent(pan);
			alignPan.setContent(swingNode);
			funcTabPane.getTabs().add(alignPan);
			funcTabPane.getSelectionModel().select(alignPan);
		}
		
	}

	public void runKmeansPlusPlus() {

		// TextInputDialog dialog = new TextInputDialog("");
		// dialog.setTitle("Set the kmeans parameters");
		// GridPane grid = new GridPane();
		// grid.setHgap(10);
		// grid.setVgap(10);
		// grid.setPadding(new Insets(20, 150, 10, 10));
		//
		// TextField KofKmeans = new TextField("14");
		// KofKmeans.setPrefColumnCount(5);
		// TextField KmeansRoundTimesParam = new TextField("100");
		// KmeansRoundTimesParam.setPrefColumnCount(5);
		//
		// grid.add(new Label("K: "), 0, 0); // row 1
		// grid.add(KofKmeans, 1, 0);
		// grid.add(new Label("Kmeans运行次数: "), 0, 1); // row 1
		// grid.add(KmeansRoundTimesParam, 1, 1);
		// dialog.getDialogPane().setContent(grid);
		// Platform.runLater(() -> KofKmeans.requestFocus());
		// dialog.showAndWait().ifPresent(response -> {
		// KKmeans = Integer.parseInt(KofKmeans.getText());
		// KmeansRoundTimes = Integer.parseInt(KmeansRoundTimesParam.getText());
		// });

		String docToTopicFilePath = "data/OutlierDetection/topic/doc2topics/docToTopic.csv";
		String idFromCorpusForMapping = "data/OutlierDetection/corpus";
		String logFilePathForSave = "data/OutlierDetection/cluster/LogBasedOnKmeansPlusPlus-";
		String clusterToItemsFilePath = "data/OutlierDetection/cluster/clusterToItems.csv";
		String topic2itemsFilePah = "data/OutlierDetection/topic/topic2items";
		String logPath = "data/OutlierDetection/cluster/LogBasedOnKmeansPlusPlus-" + KKmeans + ".csv";
		String docToTopicPath = "data/OutlierDetection/topic/doc2topics/docToTopic.csv";
		String corpusFilePath = "data/OutlierDetection/corpus";
		try {
			Set<String> id = new HashSet<String>();
			double min = Double.MAX_VALUE;
			Map<Integer, List<Point>> result = null;
			int minIndex = -1;
			for (int j = 0; j < KmeansRoundTimes; j++) {
				int i = KKmeans;
				KmeansPlusPlus kmeansClustering = new KmeansPlusPlus(docToTopicFilePath, idFromCorpusForMapping);
				Map<Integer, List<Point>> tempResult = kmeansClustering.kcluster(i);
				System.out.println(kmeansClustering.evaluate);
				if (kmeansClustering.evaluate < min) {
					min = kmeansClustering.evaluate;
					result = tempResult;
					minIndex = i;
				} else
					continue;
			}
			File file_out = new File(logFilePathForSave + minIndex + ".csv");
			// BufferedWriter bw_out = new BufferedWriter(new
			// FileWriter(file_out, false));//
			BufferedWriter bw_out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file_out), "UTF-8"));//
			bw_out.write("\"BRBM\",\"XM\",\"RQ\"");
			bw_out.newLine();
			for (Entry<Integer, List<Point>> entry : result.entrySet()) {
				// System.out.println("===============聚簇中心为：" +
				// entry.getKey() +
				// "================");
				// System.out.println(entry.getValue().size());
				for (Point point : entry.getValue()) {
					String[] strs = point.id.split("#");
					String caseId = strs[0];
					id.add(caseId);
					String riqi = strs[1].split("\\.")[0];
					bw_out.write(caseId + "," + entry.getKey() + "," + riqi);
					bw_out.newLine();
				}
			}
			for (String it : id) {
				bw_out.write(it + "," + "s" + "," + "1900-1-1");
				bw_out.newLine();
				bw_out.write(it + "," + "e" + "," + "2018-1-1");
				bw_out.newLine();
			}
			bw_out.flush();
			bw_out.close();
			System.out.println("Kmeans++ completed!");

			// 保存各类簇的主题代表
			GetLabelForCluster ins = new GetLabelForCluster();
			ins.getClusterToItems(result, topic2itemsFilePah, clusterToItemsFilePath, 10, logPath, docToTopicPath,
					corpusFilePath, KLDA, KKmeans);// 每个topic，最多选10个代表词
			System.out.println("保存各类簇的主题代表 completed!");
			Text t = new Text();
			readFromClusterToItems(clusterToItemsFilePath, t);

			Tab tabCluster2items = null;
			for (int i = 0; i < funcTabPane.getTabs().size(); i++) {
				String title = funcTabPane.getTabs().get(i).getText();
				if (title.equals("cluster2items")) {
					tabCluster2items = funcTabPane.getTabs().get(i);
					ScrollPane sp = new ScrollPane();
					sp.setContent(t);
					tabCluster2items.setContent(sp);
					funcTabPane.getSelectionModel().select(tabCluster2items);
				}
			}

			if (tabCluster2items == null) {
				tabCluster2items = new Tab("cluster2items");
				ScrollPane sp = new ScrollPane();
				sp.setContent(t);
				tabCluster2items.setContent(sp);
				funcTabPane.getTabs().add(tabCluster2items);
				funcTabPane.getSelectionModel().select(tabCluster2items);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void readFromClusterToItems(String clusterToItemsFilePath, Text t) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(clusterToItemsFilePath)));
			String str = "";
			String line = "";
			while ((line = br.readLine()) != null) {
				str += line + "\n";
			}
			t.setText(str);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void generateCorpusForLDA(String path) throws IOException {
		FilesOfFolderDelete.delFolder(path);
		// String path="data/OutlierDetection/corpus/";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
		for (Map.Entry<String, InputData> entry : FrameworkMain.paramInputDataSetOfFunc.entrySet()) {
			String type=entry.getValue().type;
			if (type.equals("log")) {// 只处理日志文件
				InputData value = entry.getValue();
				for (InputDataRowType row : value.dataForLog) {
					String visitId = row.getVisitId();
					String date = df.format(row.getTime());
					String event = row.getEvent();
					if (map.containsKey(visitId + "#" + date)) {
						map.get(visitId + "#" + date).add(event);
					} else {
						ArrayList<String> temp = new ArrayList<String>();
						temp.add(event);
						map.put(visitId + "#" + date, temp);
					}
				}
			}
		}
		for (Map.Entry<String, ArrayList<String>> entry : map.entrySet()) {
			String key = entry.getKey();
			ArrayList<String> value = entry.getValue();
			File file_out = new File(path + key + ".txt");
			BufferedWriter bw_out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file_out), "UTF-8"));//
			for (int i = 0; i < value.size() - 1; i++) {
				bw_out.write(value.get(i) + " ");
			}
			bw_out.write(value.get(value.size() - 1));
			bw_out.newLine();
			bw_out.flush();
			bw_out.close();
		}
	}

	public void runLDA(String corpusPath) throws Exception {
		GetTopicBasedOnLDA ins = new GetTopicBasedOnLDA();

		// TextInputDialog dialog = new TextInputDialog("");
		// dialog.setTitle("Set the lda parameters");
		// GridPane grid = new GridPane();
		// grid.setHgap(10);
		// grid.setVgap(10);
		// grid.setPadding(new Insets(20, 150, 10, 10));
		//
		// TextField KofLDA = new TextField("13");
		// // KTF.setPromptText("K");
		// KofLDA.setPrefColumnCount(5);
		// grid.add(new Label("K: "), 0, 0); // row 1
		// grid.add(KofLDA, 1, 0);
		// dialog.getDialogPane().setContent(grid);
		// Platform.runLater(() -> KofLDA.requestFocus());
		// dialog.showAndWait().ifPresent(response -> {
		// KLDA = Integer.parseInt(KofLDA.getText());
		// });
		ins.lda(corpusPath, KLDA);
		System.out.println("LDA completed!");

		topicId2ItemsArray = new ArrayList<String[]>();
		loadTopic2Items(topicId2ItemsArray, "data/OutlierDetection/topic/topic2items/");
		int k = topicId2ItemsArray.get(0).length;
		final String[] tableHead = new String[k];
		for (Integer i = 0; i < k; i++) {
			tableHead[i] = "topic-" + i;
		}
		ObservableList<ObservableList<String>> topic2ItemsData = FXCollections.observableArrayList();
		for (String[] row : topicId2ItemsArray) {
			topic2ItemsData.add(FXCollections.observableArrayList(row));
		}
		TableView<ObservableList<String>> topic2ItemsTV = new TableView<>();
		topic2ItemsTV.setItems(topic2ItemsData);
		for (int i = 0; i < tableHead.length; i++) {
			final int curCol = i;
			final TableColumn<ObservableList<String>, String> column = new TableColumn<>(tableHead[curCol]);
			column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(curCol)));
			topic2ItemsTV.getColumns().add(column);
		}
		Tab tab1 = null;
		for (int i = 0; i < funcTabPane.getTabs().size(); i++) {
			String title = funcTabPane.getTabs().get(i).getText();
			if (title.equals("topic2items")) {
				tab1 = funcTabPane.getTabs().get(i);
				tab1.setContent(topic2ItemsTV);
				funcTabPane.getSelectionModel().select(tab1);
			}
		}

		if (tab1 == null) {
			tab1 = new Tab("topic2items");
			tab1.setContent(topic2ItemsTV);
			funcTabPane.getTabs().add(tab1);
			funcTabPane.getSelectionModel().select(tab1);
		}
		

		ArrayList<String[]> doc2Topic = new ArrayList<String[]>();
		String doc2TopicFilePath = "data/OutlierDetection/topic/doc2topics/docToTopic.csv";
		String idFromCorpusForMapping = "data/OutlierDetection/corpus";
		loadDoc2Topic(doc2Topic, doc2TopicFilePath, idFromCorpusForMapping);
		final String[] doc2TopicsTableHead = new String[k + 1];
		doc2TopicsTableHead[0] = "docId";
		for (int i = 1; i <= k; i++) {
			doc2TopicsTableHead[i] = "topic-" + (i - 1);
		}
		ObservableList<ObservableList<String>> doc2TopicsData = FXCollections.observableArrayList();
		for (String[] row : doc2Topic) {
			doc2TopicsData.add(FXCollections.observableArrayList(row));
		}
		TableView<ObservableList<String>> doc2TopicsDataTV = new TableView<>();
		doc2TopicsDataTV.setItems(doc2TopicsData);
		for (int i = 0; i < doc2TopicsTableHead.length; i++) {
			final int curCol = i;
			final TableColumn<ObservableList<String>, String> column = new TableColumn<>(doc2TopicsTableHead[curCol]);
			column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(curCol)));
			doc2TopicsDataTV.getColumns().add(column);
		}
		Tab tab2 = null;
		for (int i = 0; i < funcTabPane.getTabs().size(); i++) {
			String title = funcTabPane.getTabs().get(i).getText();
			if (title.equals("doc2Topics")) {
				tab2 = funcTabPane.getTabs().get(i);
				tab2.setContent(doc2TopicsDataTV);
				funcTabPane.getSelectionModel().select(tab1);
			}
		}

		if (tab2 == null) {
			tab2 = new Tab("doc2Topics");
			tab2.setContent(doc2TopicsDataTV);
			funcTabPane.getTabs().add(tab2);
			funcTabPane.getSelectionModel().select(tab1);
		}
		
	}

	public void loadTopic2Items(ArrayList<String[]> topicId2ItemsArray, String topicFilePath) {
		Map<String, ArrayList<String>> topic2Items = new HashMap<String, ArrayList<String>>();
		BufferedReader br;
		int k = 0;
		try {
			File folder = new File(topicFilePath);
			for (File file : folder.listFiles()) {
				k++;
				String topicNum = file.getName().split("\\.")[0];
				br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
				String currentLine;
				ArrayList<String> eachTopic2Items = new ArrayList<String>();
				while ((currentLine = br.readLine()) != null) {
					eachTopic2Items.add(currentLine);
				}
				topic2Items.put(topicNum, eachTopic2Items);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int itemSize = topic2Items.get("0").size();
		for (int i = 0; i < itemSize; i++) {
			String[] dataTemp = new String[k];
			for (Integer j = 0; j < k; j++) {
				dataTemp[j] = topic2Items.get(j.toString()).get(i);
			}
			topicId2ItemsArray.add(dataTemp);
		}
	}

	public void loadDoc2Topic(ArrayList<String[]> doc2Topic, String doc2TopicFilePath, String idFromCorpusForMapping) {
		DecimalFormat df = new java.text.DecimalFormat("#.000");
		ArrayList<String> id = new ArrayList<String>();
		ArrayList<ArrayList<Double>> data = new ArrayList<ArrayList<Double>>();
		try {
			File folder = new File(idFromCorpusForMapping);
			for (File file : folder.listFiles()) {
				id.add(file.getName());
			}
			BufferedReader reader;
			reader = new BufferedReader(new FileReader(doc2TopicFilePath));
			String line = null;
			while ((line = reader.readLine()) != null) {
				Map<String, Double> temp = new HashMap<String, Double>();
				String[] strs = line.split(",");
				for (int i = 0; i < strs.length; i++) {
					temp.put(strs[i].split("=")[0], Double.valueOf(df.format(Double.valueOf(strs[i].split("=")[1]))));
				}
				ArrayList<Double> arrayTemp = new ArrayList<Double>();
				for (Integer k = 0; k < temp.size(); k++)
					arrayTemp.add(temp.get(k.toString()));
				data.add(arrayTemp);
			}
			reader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int k = data.get(0).size();
		for (int i = 0; i < id.size(); i++) {
			String[] rowTemp = new String[k + 1];
			rowTemp[0] = id.get(i);
			for (int j = 1; j <= k; j++) {
				rowTemp[j] = data.get(i).get(j - 1).toString();
			}
			doc2Topic.add(rowTemp);
		}
	}
}
