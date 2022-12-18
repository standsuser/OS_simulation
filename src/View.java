import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class View extends JFrame{
	
	public JLabel queue = new JLabel("queue", JLabel.CENTER);

	public View() {
		// Dimension sc = Toolkit.getDefaultToolkit().getScreenSize();
		// setSize((int)(sc.getWidth() * 700/1980),(int)(sc.getWidth() * 400/1980));
		setSize(600,600);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);// centers the Frames
		this.setLayout(null);
		queue.setFont(new Font("", Font.BOLD, 20));
		setBounds(queue, 0, -50, 600, 600);
		add(queue);
		setResizable(false);

	}
	
	public void setBounds(JComponent object, int x, int y, int width, int height) {
		Dimension sc = Toolkit.getDefaultToolkit().getScreenSize();
		object.setBounds((int)(sc.getWidth() * x/1980), (int)(sc.getHeight() * y/1080), (int)(sc.getWidth() * width/1980), (int)(sc.getHeight() * height/1080));
	}
	
}
