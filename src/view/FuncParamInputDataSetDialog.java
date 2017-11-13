package view;

import java.io.IOException;
import java.util.HashMap;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;

public class FuncParamInputDataSetDialog {
	public FuncParamInputDataSetDialog(){}
	public void setParamInputDataSet() throws IOException {
		FrameworkMain.paramInputDataSetOfFunc = new HashMap<String, InputData>();//清空输入集
		TextInputDialog dialog = new TextInputDialog("");
		dialog.setTitle("选择要处理的文件");
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("FuncParamInputDataSetTable.fxml"));
		loader.setController(new FuncParamInputDataSetController());
		AnchorPane funcParamInputDataSetPane = loader.load();
		dialog.getDialogPane().setContent(funcParamInputDataSetPane);
		dialog.showAndWait();
	}
}
