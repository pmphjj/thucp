package test;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SwingNodeExample extends Application {


    @Override
    public void start(Stage stage) {
        final SwingNode swingNode = new SwingNode();
        JPanel panel = new JPanel();
        panel.add(new JButton("Click me!"));
        swingNode.setContent(panel);
        
        GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
		grid.add(swingNode, 0, 0); // row 1
        
        TabPane funcTabPane = new TabPane();
        Tab t=new Tab("t");
        t.setContent(grid);
        funcTabPane.getTabs().add(t);
        
       
//        Pane pane = new Pane();
//        pane.getChildren().add(swingNode); // Adding swing node

        stage.setScene(new Scene(funcTabPane, 500, 500));
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}