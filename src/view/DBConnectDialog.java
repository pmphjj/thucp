package view;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.CPMRM.DBUtil;

public class DBConnectDialog {

	public static HashMap<String, String> dBDriver = new HashMap<String, String>();
	static{
		dBDriver.put("MySQL", "com.mysql.jdbc.Driver");
		dBDriver.put("SQL Sever", "Servercom.microsoft.sqlserver.jdbc.SQLServerDriver");
		dBDriver.put("Oracle", "oracle.jdbc.driver.OracleDriver");
	}

    public void display(String title , String message){
    Stage window = new Stage();
    window.setTitle(title);
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

    Text scenetitle = new Text("连接数据库信息");
    scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
    topGrid.add(scenetitle, 0, 0, 2, 1);

    Label type = new Label("数据库类型:");
    topGrid.add(type, 0, 1);
    ObservableList<String> options = FXCollections.observableArrayList(dBDriver.keySet());
    ComboBox<String> typeCombo = new ComboBox<>(options);
    typeCombo.setValue("MySQL");
    topGrid.add(typeCombo, 1, 1);


    Label ip = new Label("主机名或IP地址:");
    topGrid.add(ip, 0, 2);
    TextField ipTextField = new TextField("localhost");
    topGrid.add(ipTextField, 1, 2);
 
    Label port = new Label("端口:");
    topGrid.add(port, 0, 3);
    TextField portTextField = new TextField("3306");
    topGrid.add(portTextField, 1, 3);

    Label dbName = new Label("数据库名:");
    topGrid.add(dbName, 0, 4);
    TextField dbNameTextField = new TextField("uap");
    topGrid.add(dbNameTextField, 1, 4);

    Label userName = new Label("用户名:");
    topGrid.add(userName, 0, 5);
    TextField userTextField = new TextField("root");
    topGrid.add(userTextField, 1, 5);

    Label pw = new Label("密码:");
    topGrid.add(pw, 0, 6);
    PasswordField pwBox = new PasswordField();
    topGrid.add(pwBox, 1, 6);

    Button btn = new Button("连接数据库");
    HBox hbBtn = new HBox(10);
    hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
    hbBtn.getChildren().add(btn);
    topGrid.add(hbBtn, 1, 8);

    Text actiontarget = new Text();
    topGrid.add(actiontarget, 0, 10, 2, 1);



    btn.setOnAction(new EventHandler<ActionEvent>() {

        @Override
        public void handle(ActionEvent e) {

        	String url = null;
        	switch (typeCombo.getValue()) {
				case "MySQL":
					url = "jdbc:mysql://"+ipTextField.getText()+":"+portTextField.getText()+"/"+dbNameTextField.getText()+"?characterEncoding=utf-8";
					break;
				case "SQL Sever":
 					url = "jdbc.sqlserver://"+ipTextField.getText()+":"+portTextField.getText()+";DatabaseName="+dbNameTextField.getText();
					break;
				case "Oracle":
					url = "jdbc:oracle:thin:@"+ipTextField.getText()+":"+portTextField.getText()+":"+dbNameTextField.getText();
					break;

			}


			try {
				Connection connection = DBUtil.getConnection(dBDriver.get(typeCombo.getValue()),url,userTextField.getText(),pwBox.getText());



				actiontarget.setFill(Color.BLUE);
                actiontarget.setText("Success");

    			new DBDataConfigDialog().display(connection,"数据库导入数据", "message");

			} catch (Exception exception) {
				// TODO Auto-generated catch block
				actiontarget.setFill(Color.RED);
	            actiontarget.setText("Failed:  Please check your input message!");
	            exception.printStackTrace();
			}


        }
    });



//    BorderPane bp = new BorderPane();
//    bp.setCenter(topGrid);

    Scene scene = new Scene(topGrid);
    window.setScene(scene);
    //使用showAndWait()先处理这个窗口，而如果不处理，main中的那个窗口不能响应
    window.showAndWait();
    }
}