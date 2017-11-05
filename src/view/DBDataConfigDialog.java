package view;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.netlib.util.intW;

import ca.pfv.spmf.algorithms.classifiers.decisiontree.id3.Node;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DBDataConfigDialog {

	static List<String> prop = new ArrayList<String>();
	static{
		prop.add("病人编码");
		prop.add("医嘱名称");
		prop.add("医嘱类型");
		prop.add("日期");
	}

	public void display(Connection connection,String title , String message){
	    Stage window = new Stage();
	    window.setTitle(title);
	    //modality要使用Modality.APPLICATION_MODEL
	    window.initModality(Modality.APPLICATION_MODAL);
	    window.setMinWidth(400);
	    window.setMinHeight(400);


	    GridPane grid = new GridPane();

	    grid.setPadding(new Insets(10, 10, 10, 10));
	    grid.setGridLinesVisible(true);

	    Text category = new Text("\t标准输入数据属性\t");
	    category.setFont(Font.font("黑体", FontWeight.BOLD, 15));
	    grid.add(category, 0, 0);


	    Text chartTitle = new Text("\t数据库表及对应列选择\t");
	    chartTitle.setFont(Font.font("黑体", FontWeight.BOLD, 15));
	    grid.add(chartTitle, 1, 0);

	    BorderPane bp = new BorderPane();
	    //查询数据库表和列
	    try {
	      DatabaseMetaData dbmd;
		  dbmd = connection.getMetaData();
		  ResultSet rs = dbmd.getTables(null, "%", "%", new String[] { "TABLE"});
		  List<String> tableNameList = new ArrayList<String>();
		  while (rs.next()) {
		     tableNameList.add(rs.getString("TABLE_NAME"));
		   }
		  ObservableList<String>  tablesList = FXCollections.observableArrayList(tableNameList);


		  ResultSet rs1;
		  HashMap<String,ObservableList<String>> chooseColumn = new HashMap<String,ObservableList<String>>();
		  for (String str : tableNameList) {
			    rs1 = dbmd.getColumns(null, "%", str,"%");
			    ArrayList<String> columnNameList = new ArrayList<String>();
				while (rs1.next()) {
				   columnNameList.add(rs1.getString("COLUMN_NAME"));
				}
				chooseColumn.put(str, FXCollections.observableArrayList(columnNameList));
	       }

		  ArrayList<CorrespondChoose> correspondChoosesList = new ArrayList<>();
		  for (int i = 0; i < prop.size(); i++) {
		    	Text row = new Text("\t"+prop.get(i)+"\t");
		 	   // category.setFont(Font.font("黑体", FontWeight.NORMAL, 13));
		 	    grid.add(row, 0, i+1);

		 	    CorrespondChoose choose = new CorrespondChoose(tablesList,chooseColumn);
               correspondChoosesList.add(choose);
		 	    grid.add(choose.hBox, 1, i+1);

			}




		    bp.setPadding(new Insets(10, 20, 10, 20));
	        bp.setCenter(grid);

	        Button okButton = new Button("导入数据");
	        okButton.setOnAction(new EventHandler<ActionEvent>() {
	    		public void handle(ActionEvent event) {
	    			HashSet<String> selectTableSet = new HashSet<String>();
	    			StringBuffer selectString = new StringBuffer();
	    			for (int i = 0; i < correspondChoosesList.size(); i++) {

	    				selectTableSet.add(correspondChoosesList.get(i).tableCombo.getValue());
	    				if(i == 0)
	    				 selectString.append(correspondChoosesList.get(i).tableCombo.getValue()+"."+correspondChoosesList.get(i).columnCombo.getValue());
	    				else
	    					selectString.append(","+correspondChoosesList.get(i).tableCombo.getValue()+"."+correspondChoosesList.get(i).columnCombo.getValue());
					}
	    			StringBuffer fromString = new StringBuffer();
	    			int j = 0;
	    			for(Iterator it=selectTableSet.iterator();it.hasNext();++j)
	    			{
	    			  if(j == 0)
	    			      fromString.append(it.next());
	    			  else
	    				  fromString.append(","+it.next());
	    			}
                    for (String p:prop) {
                    	System.out.print(p);
					}
                    System.out.println();
                   //拼出SQL
	    			String sql = "select "+selectString.toString()+" from "+fromString.toString();    //要执行的SQL
	    			System.out.println(sql);
	    			Statement stmt;
					try {
						stmt = connection.createStatement();
	                     ResultSet rs = stmt.executeQuery(sql);//创建数据对象
	                    while (rs.next()){
	                        System.out.print(rs.getString(1) + "\t");
	                        System.out.print(rs.getString(2) + "\t");
	                        System.out.print(rs.getString(3) + "\t");
	                        System.out.print(rs.getString(4) + "\t");
	                        System.out.println();
	                    }
					}catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

	    		}
	    	});

	        bp.setBottom(okButton);

	    }
	    catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}






	    Scene scene = new Scene(bp);
	    window.setScene(scene);
	    //使用showAndWait()先处理这个窗口，而如果不处理，main中的那个窗口不能响应
	    window.showAndWait();
	}

}
class CorrespondChoose {

	 HBox hBox = new HBox();
	 ComboBox<String> tableCombo;
	 ComboBox<String> columnCombo;

	 public CorrespondChoose( ObservableList<String> tables, HashMap<String,ObservableList<String>> columns)
	 {

		 tableCombo = new ComboBox<String>(tables);
		 columnCombo = new  ComboBox<String>();
		 tableCombo.setOnAction(ev -> {
	         columnCombo.setItems(columns.get(tableCombo.getValue()));
	        });
		 hBox.getChildren().addAll(tableCombo,columnCombo);
	 }
}


