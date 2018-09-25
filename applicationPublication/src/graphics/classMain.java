package graphics;

import javax.swing.UIManager;

import jade.Boot;

public class classMain {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
		}

		try {
			String[] arg = { "", "" };
			Boot.main(arg);
		} catch (Exception e) {
			e.printStackTrace();
		}

		new graphicalInterface();
	}
}
