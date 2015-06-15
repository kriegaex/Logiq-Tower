package de.scrum_master.games.lone_rook;

// $Id$

import java.awt.Font;
import javax.swing.*;

public final class CharsetDump
{
	public static void main(String args[]) throws Exception
	{
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		JFrame win = new JFrame("Unicode frame drawing characters");
		JPanel root = new JPanel();
		win.getContentPane().add(root);
		win.setDefaultCloseOperation(2);
		JTextArea textArea = new JTextArea(15, 15);
		Font courierFont = Font.getFont("Couriew New", textArea.getFont());
		courierFont = courierFont.deriveFont(40F);
		textArea.setFont(courierFont);
		System.err.println(textArea.getFont());
		String txt = "";
		for (int i = 32; i <= 19532; i++)
			if (courierFont.canDisplay((char)i))
				txt = (new StringBuilder()).append(txt).append(i).append("  -  ").append((char)i).append("\n").toString();
		textArea.setText(txt);
		root.add(new JScrollPane(textArea));
		win.pack();
		win.setVisible(true);
	}
}
