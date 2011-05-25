import javax.swing.JFrame;
import javax.swing.JButton;
public class FirstSwingDemo
{
	public static final int WIDTH=300;
	public static final int HEIGHT=200;
    public static void main(String[] args){
		JFrame firstwindows = new JFrame("this is a test");
		firstwindows.setSize(WIDTH,HEIGHT);
		firstwindows.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		JButton endButton = new JButton("click to end process");
		EndingListener buttonEar = new EndingListener();
		endButton.addActionListener(buttonEar);

		firstwindows.add(endButton);
		firstwindows.setVisible(true);
    }
}

