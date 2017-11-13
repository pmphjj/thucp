package view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.DirectoryChooserBuilder;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import test.TableController;

public class InputFromFileUI {
	VBox inputVbox;
	MenuItem menuFile;
	Stage stage;
	FXMLLoader loader;
	TabPane inputTabPane;

	public InputFromFileUI(VBox inputVbox, MenuItem menuFile, Stage stage, FXMLLoader loader,TabPane inputTabPane) {
		this.inputVbox = inputVbox;
		this.menuFile = menuFile;
		this.stage = stage;
		this.loader = loader;
		this.inputTabPane = inputTabPane;
		setActions();
	}

	public void setActions() {
		SetMenuFileAction();
	}

	public void SetMenuFileAction() {
		menuFile.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("选择文件");
				File file = fileChooser.showOpenDialog(stage);
				// System.out.println(file.getAbsolutePath());
				try {
					InputData fileData = new InputData();
					FrameworkMain.indexInputData++;
					String key = FrameworkMain.indexInputData.toString() + "-" + file.getAbsolutePath();
					fileData.setKey(key);
					BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
					String line = br.readLine();
					String[] lines = line.split(",");
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					Map<String, Integer> columName2index = new HashMap<String, Integer>();
					for (int i = 0; i < lines.length; i++){
						columName2index.put(lines[i], i);
					}
//					for(Map.Entry<String, Integer> entry:columName2index.entrySet()){
//						System.out.println(entry.getKey()+":"+entry.getValue());
//					}
					while ((line = br.readLine()) != null) {
						lines = line.split(",");
						InputDataRowType tempRow = new InputDataRowType();
						if (columName2index.containsKey("visitId")){
//							System.out.println(lines[columName2index.get("visitId")]);
							tempRow.setVisitId(lines[columName2index.get("visitId")]);
						}

						if (columName2index.containsKey("event"))
							tempRow.setEvent(lines[columName2index.get("event")]);

						if (columName2index.containsKey("eventClass"))
							tempRow.setEventClass(lines[columName2index.get("eventClass")]);

						if (columName2index.containsKey("num"))
							tempRow.setNum(Double.parseDouble(lines[columName2index.get("num")]));

						if (columName2index.containsKey("price"))
							tempRow.setPrice(Double.parseDouble(lines[columName2index.get("price")]));

						if (columName2index.containsKey("time"))
							tempRow.setTime(df.parse(lines[columName2index.get("time")]));

						if (columName2index.containsKey("diagnoseId"))
							tempRow.setDiagnoseId(lines[columName2index.get("diagnoseId")]);

						if (columName2index.containsKey("hospitalId"))
							tempRow.setHospitalId(lines[columName2index.get("hospitalId")]);

						if (columName2index.containsKey("departmentId"))
							tempRow.setDepartmentId(lines[columName2index.get("departmentId")]);

						if (columName2index.containsKey("doctorId"))
							tempRow.setDoctorId(lines[columName2index.get("doctorId")]);

						fileData.dataForLog.add(tempRow);
					}
					fileData.type="log";
					FrameworkMain.inputDataSet.put(key, fileData);
					InputTableController temp = loader.getController();
					temp.setInputTabPane(inputTabPane);
					temp.showInputTable(temp.getInputDataSet());

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
}
