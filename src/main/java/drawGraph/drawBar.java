package drawGraph;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

import javax.swing.JTextField;
import javax.swing.JSeparator;
import javax.swing.JButton;

public class drawBar {

	private JFrame frame;
	public JTextField textField_ngayBatDau;
	public JTextField textField_ngayKetThuc;
	public JTextField textField_file;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					drawBar window = new drawBar();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public drawBar() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 969, 468);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		JLabel lblNgayBatDau = new JLabel("Ngày bắt đầu");
		lblNgayBatDau.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNgayBatDau.setBounds(227, 145, 157, 41);
		lblNgayBatDau.setHorizontalAlignment(SwingConstants.TRAILING);
		panel.add(lblNgayBatDau);

		JLabel lblNgayKetThuc = new JLabel("Ngày kết thúc");
		lblNgayKetThuc.setHorizontalAlignment(SwingConstants.TRAILING);
		lblNgayKetThuc.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNgayKetThuc.setBounds(227, 217, 157, 41);
		panel.add(lblNgayKetThuc);

		textField_ngayBatDau = new JTextField();
		textField_ngayBatDau.setBounds(488, 152, 157, 33);
		panel.add(textField_ngayBatDau);
		textField_ngayBatDau.setColumns(10);

		textField_ngayKetThuc = new JTextField();
		textField_ngayKetThuc.setBounds(488, 224, 157, 33);
		panel.add(textField_ngayKetThuc);
		textField_ngayKetThuc.setColumns(10);

		JSeparator separator = new JSeparator();
		separator.setBounds(10, 122, 935, 2);
		panel.add(separator);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(20, 352, 935, 2);
		panel.add(separator_1);

		JLabel lblNewLabel = new JLabel("EXPORT FILE JSON THEO NGÀY");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 21));
		lblNewLabel.setBounds(269, 68, 368, 33);
		panel.add(lblNewLabel);

		ActionListener actionListener = new drawListener(this);

		JButton btnNewButton = new JButton("Export");
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnNewButton.setBounds(700, 294, 157, 41);
		panel.add(btnNewButton);
		btnNewButton.addActionListener(actionListener);

		JLabel lblFile = new JLabel("File");
		lblFile.setHorizontalAlignment(SwingConstants.TRAILING);
		lblFile.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblFile.setBounds(227, 301, 157, 41);
		panel.add(lblFile);

		textField_file = new JTextField();
		textField_file.setColumns(10);
		textField_file.setBounds(488, 301, 157, 33);
		panel.add(textField_file);

	}

	public void draw(String fileName, String startDate, String endDate) {
		frame = new JFrame();
		frame.setBounds(100, 100, 969, 468);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);

		String fileName_temp = "C:/Users/ADMIN/OneDrive/Desktop/" + fileName;
		ObjectMapper objectMapper = new ObjectMapper();

		String startDateList[] = startDate.split("/");
		int day = Integer.parseInt(startDateList[0]);
		int month = Integer.parseInt(startDateList[1]);
		int year = Integer.parseInt(startDateList[2]);
		LocalDate startDate_date = LocalDate.of(year, month, day);

		String endDateList[] = endDate.split("/");
		int day_2 = Integer.parseInt(endDateList[0]);
		int month_2 = Integer.parseInt(endDateList[1]);
		int year_2 = Integer.parseInt(endDateList[2]);
		LocalDate endDate_date = LocalDate.of(year_2, month_2, day_2);

		

		try {
			JsonNode jsonNode = objectMapper.readTree(new File(fileName_temp));
			// Create a dataset
			DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();
			for (JsonNode jsn : jsonNode) {
				String dateJson = jsn.get("date").asText();
				String dateJson_new[] = dateJson.split("-");

				int day_json = Integer.parseInt(dateJson_new[2]);
				int month_json = Integer.parseInt(dateJson_new[1]);
				int year_json = Integer.parseInt(dateJson_new[0]);
				LocalDate dateJson_date = LocalDate.of(year_json, month_json, day_json);

				

				if ((dateJson_date.isAfter(startDate_date) && dateJson_date.isBefore(endDate_date))
						|| (dateJson.equals(endDate_date) && dateJson_date.equals(startDate_date))
						|| (dateJson_date.equals(endDate_date) && dateJson_date.isAfter(startDate_date))
						|| (dateJson_date.isBefore(endDate_date) && dateJson_date.equals(startDate_date))) {
					String title = jsn.get("title").asText();
					int view = jsn.get("views").asInt();
					categoryDataset.setValue(view, title, title);
				}
			}

			// Create a chart
			JFreeChart chart = ChartFactory.createBarChart("THỐNG KẾ VIEW CỦA TỪNG VIDEO", "TITLE", "VIEW",
					categoryDataset, PlotOrientation.VERTICAL, true, true, false);

			// Create a chart panel
			ChartPanel chartPanel = new ChartPanel(chart);
			chartPanel.setPreferredSize(new Dimension(800, 600));
			frame.setContentPane(chartPanel);
			frame.setVisible(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	public static void main(String[] args) {
//		SwingUtilities.invokeLater(() -> {
//			draw example = new draw("videosComments.json");
//			example.setVisible(true);
//		});
//	}
}
