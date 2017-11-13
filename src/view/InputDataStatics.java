package view;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javafx.embed.swing.SwingNode;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.text.Text;

public class InputDataStatics {
	Comparator<InputDataRowType> com = new Comparator<InputDataRowType>() {

		@Override
		public int compare(InputDataRowType o1, InputDataRowType o2) {
			// TODO Auto-generated method stub
			return o1.time.compareTo(o2.time);
		}
	};

	public Text computeStatics(String inputDataKey) {
		InputData data = FrameworkMain.inputDataSet.get(inputDataKey);
		ArrayList<InputDataRowType> rows = data.getDataForLog();
		HashSet<String> visits = new HashSet<String>();
		HashSet<String> items = new HashSet<String>();
		for (InputDataRowType it : rows) {
			visits.add(it.getVisitId());
			items.add(it.getEvent());
		}
		Integer size = data.getDataForLog().size();
		Text re = new Text();
		re.setText("\n" + "trace数目: " + visits.size() + "\n\n" + "event总数:" + items.size());
		return re;
	}

	public SwingNode getStayDistribution(String inputDataKey) {
		Map<String, ArrayList<InputDataRowType>> case2events = new HashMap<String, ArrayList<InputDataRowType>>();
		InputData data = FrameworkMain.inputDataSet.get(inputDataKey);
		TreeMap<Long, Integer> day2casesSize = new TreeMap<Long, Integer>();
		ArrayList<InputDataRowType> rows = data.getDataForLog();
		for (InputDataRowType it : rows) {
			String key = it.getVisitId();
			if (case2events.containsKey(key)) {
				case2events.get(key).add(it);
			} else {
				ArrayList<InputDataRowType> array = new ArrayList<InputDataRowType>();
				array.add(it);
				case2events.put(key, array);
			}
		}
		for (Map.Entry<String, ArrayList<InputDataRowType>> entry : case2events.entrySet()) {
			Collections.sort(entry.getValue(), com);
		}
		for (Map.Entry<String, ArrayList<InputDataRowType>> entry : case2events.entrySet()) {
			ArrayList<InputDataRowType> value = entry.getValue();
			Date s = value.get(0).time;
			Date e = value.get(value.size() - 1).time;
			Long key = getDatePoor(e, s);
			if (day2casesSize.containsKey(key)) {
				day2casesSize.put(key, day2casesSize.get(key) + 1);
			} else {
				day2casesSize.put(key, 1);
			}
		}
//		Long min = Long.MAX_VALUE;
//		Long max = Long.MIN_VALUE;
//		for (Map.Entry<Long, Integer> entry : day2casesSize.entrySet()) {
//			if (entry.getKey().compareTo(max) > 0)
//				max = entry.getKey();
//			if (entry.getKey().compareTo(min) < 0)
//				min = entry.getKey();
//		}
//		System.out.println(min + ":" + max);
		
		DefaultCategoryDataset mDataset = new DefaultCategoryDataset();
		for (Map.Entry<Long, Integer> entry : day2casesSize.entrySet()) {
			mDataset.addValue(entry.getValue(), "住院天数", entry.getKey());
		}
		
		StandardChartTheme mChartTheme = new StandardChartTheme("CN");
		mChartTheme.setLargeFont(new Font("黑体", Font.BOLD, 20));
		mChartTheme.setExtraLargeFont(new Font("宋体", Font.PLAIN, 15));
		mChartTheme.setRegularFont(new Font("宋体", Font.PLAIN, 15));
		ChartFactory.setChartTheme(mChartTheme);
		JFreeChart mChart = ChartFactory.createLineChart("住院天数分布图", // 图名字
				"住院天数", // 横坐标
				"数量", // 纵坐标
				mDataset, // 数据集
				PlotOrientation.VERTICAL, false, // 显示图例
				true, // 采用标准生成器
				false);// 是否生成超链接

		CategoryPlot mPlot = (CategoryPlot) mChart.getPlot();
		mPlot.setBackgroundPaint(Color.LIGHT_GRAY);
		mPlot.setRangeGridlinePaint(Color.BLUE);// 背景底部横虚线
		mPlot.setOutlinePaint(Color.RED);// 边界线
		SwingNode node=new SwingNode();
		ChartPanel c=new ChartPanel(mChart);
		node.setContent(c);
		return node;
	}

	public static Long getDatePoor(Date endDate, Date nowDate) {

		long nd = 1000 * 24 * 60 * 60;
		long nh = 1000 * 60 * 60;
		long nm = 1000 * 60;
		// long ns = 1000;
		// 获得两个时间的毫秒时间差异
		long diff = endDate.getTime() - nowDate.getTime();
		// 计算差多少天
		long day = diff / nd;
		// 计算差多少小时
		long hour = diff % nd / nh;
		// 计算差多少分钟
		long min = diff % nd % nh / nm;
		// 计算差多少秒//输出结果
		// long sec = diff % nd % nh % nm / ns;
		return (day * 24 + hour) / 24;
	}

	public static void main(String[] args) {
		InputDataStatics ins = new InputDataStatics();
	}
}
