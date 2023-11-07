package drawGraph;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JOptionPane;

public class drawListener implements ActionListener{
	private drawBar drawBar;
	
	public drawListener(drawBar drawBar) {
		this.drawBar = drawBar;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("Export")) {
			 int option = JOptionPane.showConfirmDialog(null, "Bạn đã export file json");
			 if(option ==   JOptionPane.YES_OPTION) {
				 String ngayBatDau = this.drawBar.textField_ngayBatDau.getText();
				 String ngayKetThuc = this.drawBar.textField_ngayKetThuc.getText();
				 String file = this.drawBar.textField_file.getText();
				 
				 this.drawBar.draw(file, ngayBatDau, ngayKetThuc);
				
			 }
			 
		}
	}

}
