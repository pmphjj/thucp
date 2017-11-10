package view;

import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import javafx.scene.text.Text;

public class InputDataStatics {
	public Text computeStatics(String inputDataKey){
		InputData data=FrameworkMain.inputDataSet.get(inputDataKey);
		ArrayList<InputDataRowType> rows=data.getData();
		HashSet<String> visits=new HashSet<String>();
		HashSet<String> items=new HashSet<String>();
		for(InputDataRowType it:rows){
			visits.add(it.getVisitId());
			items.add(it.getEvent());
		}
		Integer size=data.getData().size();
		Text re=new Text();
		re.setText("\n"+"trace数目: "+visits.size()+"\n\n"+"event总数:"+items.size());
		return re;
	}
}
