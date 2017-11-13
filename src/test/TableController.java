package test;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

public class TableController implements Initializable {
    @FXML
    private Label fxLabel;
    @FXML
    private TableView<Student> stuTable;
    @FXML
    private TableColumn<Student, String> idCol, fNameCol, lNameCol, delCol;
    @FXML
    private TableColumn<Student, Integer> ageCol;
    @FXML
    private TableColumn<Student, Boolean> isAdultCol;

    public ObservableList<Student> getStuData() {

        Student stu1 = new Student("赵", "哈", 16);
        Student stu2 = new Student("钱", "大", 26);
        Student stu3 = new Student("孙", "阿", 23);
        Student stu4 = new Student("李", "佛山", 17);
        Student stu5 = new Student("周", "阿萨德", 23);
        Student stu6 = new Student("吴", "更好", 12);
        Student stu7 = new Student("郑", "和", 28);
        Student stu8 = new Student("王", "费", 23);
        Student stu9 = new Student("刘", "的", 15);
        Student stu10 = new Student("关", "时是", 23);
        Student stu11 = new Student("张", "良好", 19);
        Student stu12 = new Student("诸葛", "列", 23);
        Student stu13 = new Student("司马", "咯跑", 20);

        ObservableList<Student> stuLists = FXCollections.observableArrayList(stu1, stu2, stu3, stu4, stu5, stu6, stu7,
                stu8, stu9, stu10, stu11, stu12, stu13);
        return stuLists;
    }

    /**
     * 显示学生表格
     * 
     * @param stuLists
     */
    public void showStuTable(ObservableList<Student> stuLists) {
        fNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        // ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));
        idCol.setCellFactory((col) -> {
            TableCell<Student, String> cell = new TableCell<Student, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);

                    if (!empty) {
                        int rowIndex = this.getIndex() + 1;
                        this.setText(String.valueOf(rowIndex));
                    }
                }
            };
            return cell;
        });

        ageCol.setCellFactory((col) -> {
            TableCell<Student, Integer> cell = new TableCell<Student, Integer>() {

                @Override
                public void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);

                    if (!empty) {
                        int age = this.getTableView().getItems().get(this.getIndex()).getAge();
                        this.setText(String.valueOf(age));
                        if (age < 18) {
                            this.getStyleClass().add("mark");
                        }
                    }
                }

            };
            return cell;
        });

        isAdultCol.setCellFactory((col) -> {
            TableCell<Student, Boolean> cell = new TableCell<Student, Boolean>() {

                @Override
                public void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);

                    if (!empty) {
                        CheckBox checkBox = new CheckBox();
                        this.setGraphic(checkBox);
                        checkBox.selectedProperty().addListener((obVal, oldVal, newVal) -> {
                            if (newVal) {
                                // 添加选中时执行的代码
                                System.out.println("第" + this.getIndex() + "行被选中！");
                                // 获取当前单元格的对象
                                // this.getItem();
                            }else{
                            	System.out.println("ok");
                            }

                        });
                    }
                }

            };
            return cell;
        });

        delCol.setCellFactory((col) -> {
            TableCell<Student, String> cell = new TableCell<Student, String>() {

                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);

                    if (!empty) {
//                        ImageView delICON = new ImageView(getClass().getResource("delete.png").toString());
                        Button delBtn = new Button("删除");
                        this.setGraphic(delBtn);
                        delBtn.setOnMouseClicked((me) -> {
                            Student clickedStu = this.getTableView().getItems().get(this.getIndex());
                            this.getTableView().getItems().remove(this.getIndex());
//                            System.out.println("删除 " + clickedStu.getFirstName() + clickedStu.getLastName() + " 的记录");
                        });
                    }
                }

            };
            return cell;
        });

        stuTable.setItems(stuLists);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        fxLabel.setWrapText(true);
        fxLabel.setText("        javafx_Label标签的换行测试，用于弹出框的提示信息测试，Label控件也可以换行。");

        this.showStuTable(this.getStuData());
    }

}