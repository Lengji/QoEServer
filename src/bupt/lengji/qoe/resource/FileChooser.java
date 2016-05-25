package bupt.lengji.qoe.resource;

import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import bupt.lengji.qoe.database.DBManager;
import bupt.lengji.qoe.server.Video;

public class FileChooser {

	private JFrame frame;
	private final String rootPath = "E:\\QoEResource";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FileChooser window = new FileChooser();
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
	public FileChooser() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 500, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);

		JLabel Label_View = new JLabel("请选择文件：");
		Label_View.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		Label_View.setBounds(10, 38, 81, 23);
		frame.getContentPane().add(Label_View);

		final JTextField Text_FilePath = new JTextField();
		Text_FilePath.setBounds(92, 38, 310, 23);
		frame.getContentPane().add(Text_FilePath);

		JButton Button_View = new JButton("浏览");
		Button_View.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		Button_View.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileDialog dialog = new FileDialog(new Frame(), "选择存放位置", FileDialog.LOAD);
				dialog.setVisible(true);
				Text_FilePath.setText(dialog.getDirectory() + dialog.getFile());
			}
		});
		Button_View.setBounds(408, 38, 76, 23);
		frame.getContentPane().add(Button_View);

		final JTextField Text_Title = new JTextField();
		Text_Title.setBounds(92, 142, 392, 23);
		frame.getContentPane().add(Text_Title);

		JLabel Label_Title = new JLabel("标         题：");
		Label_Title.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		Label_Title.setBounds(10, 142, 81, 23);
		frame.getContentPane().add(Label_Title);

		JLabel Label_Description = new JLabel("描         述：");
		Label_Description.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		Label_Description.setBounds(10, 198, 81, 23);
		frame.getContentPane().add(Label_Description);

		final JTextArea Text_Description = new JTextArea();
		Text_Description.setWrapStyleWord(true);
		Text_Description.setLineWrap(true);
		Text_Description.setBounds(92, 194, 392, 134);
		frame.getContentPane().add(Text_Description);

		JLabel Label_Type = new JLabel("类         型：");
		Label_Type.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		Label_Type.setBounds(10, 90, 81, 23);
		frame.getContentPane().add(Label_Type);

		final JComboBox<String> TypeBox = new JComboBox<String>();
		TypeBox.setFont(new Font("微软雅黑", Font.BOLD, 12));
		TypeBox.setMaximumRowCount(7);
		TypeBox.setModel(new DefaultComboBoxModel<String>(new String[] { "    电影", "    TV剧", "    音乐", "    动画",
				"    竞技", "    娱乐", "    其他" }));
		TypeBox.setBounds(92, 92, 81, 21);
		frame.getContentPane().add(TypeBox);

		final JButton Button_Reset = new JButton("重置");
		Button_Reset.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		Button_Reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (Text_FilePath.getText() != null) {
					Text_FilePath.setText(null);
				}
				if (Text_Title.getText() != null) {
					Text_Title.setText(null);
				}
				if (Text_Description.getText() != null) {
					Text_Description.setText(null);
				}
				TypeBox.setSelectedIndex(0);
			}
		});
		Button_Reset.setBounds(102, 338, 76, 23);
		frame.getContentPane().add(Button_Reset);

		JButton Button_YES = new JButton("确定");
		Button_YES.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		Button_YES.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String path = Text_FilePath.getText();
				String title = Text_Title.getText();
				if (path.equals(null) || title.equals(null)) {
					return;
				}
				String description = Text_Description.getText();
				int type = TypeBox.getSelectedIndex();
				String id = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
				String targetPath = rootPath + "\\" + Video.getTypeString(type) + "\\" + id;

				Wrapper wp_img = new Wrapper(path, targetPath + "\\cover.jpg", Wrapper.IMG);
				wp_img.execute(wp_img.ScreenShotCode());
				Wrapper wp_sd = new Wrapper(path, targetPath + "\\sd.mp4", Wrapper.SD);
				wp_sd.execute(wp_sd.TransCode());
				Wrapper wp_hd = new Wrapper(path, targetPath + "\\hd.mp4", Wrapper.HD);
				wp_hd.execute(wp_hd.TransCode());
				Wrapper wp_uhd = new Wrapper(path, targetPath + "\\uhd.mp4", Wrapper.UHD);
				wp_uhd.execute(wp_uhd.TransCode());

				/*--为了方便，假定转码操作成功。事实上，要考虑的因素很多：源视频的分辨率、转码失败、视频有坏段等。--TODO*/
				try {
					String sql = "insert into videos (id,type,title,description,uhd,hd,sd) values (\'" + id + "\',"
							+ type + ",\'" + title + "\',\'" + description + "\'," + "1" + "," + "1" + "," + "1" + ")";
					Connection conn = DBManager.getConn();
					Statement st = conn.createStatement();
					st.execute(sql);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}

				Button_Reset.doClick();
			}
		});
		Button_YES.setBounds(344, 338, 76, 23);
		frame.getContentPane().add(Button_YES);
	}
}
