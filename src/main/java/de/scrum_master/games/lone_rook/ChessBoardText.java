package de.scrum_master.games.lone_rook;

// $Id: ChessBoardText.java,v 1.1 2006/01/13 03:30:19 Robin Exp $

import java.awt.Font;

import javax.swing.*;

public final class ChessBoardText
{
	private static final char START_SYMBOL = 8855;
	private static final char HORIZONTAL = 9472;
	private static final char VERTICAL = 9474;
	private static final char UP_RIGHT = 9484;
	private static final char LEFT_DOWN = 9484;
	private static final char UP_LEFT = 9488;
	private static final char RIGHT_DOWN = 9488;
	private static final char DOWN_RIGHT = 9492;
	private static final char LEFT_UP = 9492;
	private static final char DOWN_LEFT = 9496;
	private static final char RIGHT_UP = 9496;

	private static final String BOARD_TEMPLATE =
		"0\t1\t2\t3\t4\t5\t6\t7\n0\t1\t2\t3\t4\t5\t6\t7\n" +
		"0\t1\t2\t3\t4\t5\t6\t7\n0\t1\t2\t3\t4\t5\t6\t7\n" +
		"0\t1\t2\t3\t4\t5\t6\t7\n0\t1\t2\t3\t4\t5\t6\t7\n" +
		"0\t1\t2\t3\t4\t5\t6\t7\n0\t1\t2\t3\t4\t5\t6\t7\n";

	private static int pos2Index(int x, int y)
	{
		return (7 - y) * 16 + x * 2;
	}

	private static String path2Text(int[][] path)
	{
		char[] board = BOARD_TEMPLATE.toCharArray();
		for (int move = 1; move < path.length; move++) {
			char nextChar = '?';
			if (move == 1)
				nextChar = START_SYMBOL;
			else {
				if (path[move - 2][0] < path[move][0]) {
					if (path[move - 2][1] < path[move][1]) {
						if (path[move - 2][1] == path[move - 1][1])
							nextChar = RIGHT_UP;
						else
							nextChar = UP_RIGHT;
					}
					else {
						if (path[move - 2][1] == path[move - 1][1])
							nextChar = RIGHT_DOWN;
						else
							nextChar = DOWN_RIGHT;
					}
				}
				else {
					if (path[move - 2][1] < path[move][1]) {
						if (path[move - 2][1] == path[move - 1][1])
							nextChar = LEFT_UP;
						else
							nextChar = UP_LEFT;
					}
					else {
						if (path[move - 2][1] == path[move - 1][1])
							nextChar = LEFT_DOWN;
						else
							nextChar = DOWN_LEFT;
					}
				}
			}
			board[pos2Index(path[move - 1][0], path[move - 1][1])] = nextChar;

			int min, max;
			if (path[move - 1][0] == path[move][0]) {
				min = Math.min(path[move - 1][1], path[move][1]) + 1;
				max = Math.max(path[move - 1][1], path[move][1]) - 1;
				for (int y = min; y <= max; y++)
					board[pos2Index(path[move][0], y)] = VERTICAL;
			}
			else {
				min = Math.min(path[move - 1][0], path[move][0]) + 1;
				max = Math.max(path[move - 1][0], path[move][0]) - 1;
				for (int x = min; x <= max; x++)
					board[pos2Index(x, path[move][1])] = HORIZONTAL;
			}
		}
		return new String(board);
	}

	public static void showPath(int[][] path)
	{
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		JFrame win = new JFrame("Chess Board (plain Unicode)");
		win.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JTextArea textArea = new JTextArea(12, 18);
		Font font = Font.decode("Courier");
		font = font.deriveFont(40F);
		textArea.setFont(font);
		textArea.setTabSize(2);
		textArea.setText(path2Text(path));
		textArea.setEditable(false);
		win.getContentPane().add(textArea, "Center");
		win.pack();
		// Let system window manager determine position
		win.setLocationByPlatform(true);
		win.setVisible(true);
	}

	public static void main(String[] args)
	{
		showPath(new int[][] {
			{3, 4}, {7, 4}, {7, 7}, {0, 7}, {0, 0}, {7, 0}, {7, 1}, {1, 1},
			{1, 6}, {6, 6}, {6, 5}, {2, 5}, {2, 2}, {7, 2}, {7, 3}, {3, 3},
			{3, 4}
		});
		showPath(new int[][] {
			{3, 4}, {7, 4}, {7, 5}, {2, 5}, {2, 2}, {6, 2}, {6, 1}, {1, 1},
			{1, 6}, {7, 6}, {7, 7}, {0, 7}, {0, 0}, {7, 0}, {7, 3}, {3, 3},
			{3, 4}
		});
		showPath(new int[][] {
			{3, 4}, {6, 4}, {6, 1}, {1, 1}, {1, 6}, {7, 6}, {7, 7}, {0, 7},
			{0, 0}, {7, 0}, {7, 5}, {2, 5}, {2, 2}, {5, 2}, {5, 3}, {3, 3},
			{3, 4}
		});
		showPath(new int[][] {
			{3, 4}, {5, 4}, {5, 5}, {2, 5}, {2, 2}, {7, 2}, {7, 7}, {0, 7},
			{0, 0}, {7, 0}, {7, 1}, {1, 1}, {1, 6}, {6, 6}, {6, 3}, {3, 3},
			{3, 4}
		});
		showPath(new int[][] {
			{3, 4}, {4, 4}, {4, 0}, {7, 0}, {7, 7}, {0, 7}, {0, 0}, {1, 0},
			{1, 6}, {6, 6}, {6, 1}, {5, 1}, {5, 5}, {2, 5}, {2, 0}, {3, 0},
			{3, 4}
		});
		showPath(new int[][] {
			{3, 4}, {4, 4}, {4, 0}, {5, 0}, {5, 5}, {2, 5}, {2, 1}, {1, 1},
			{1, 6}, {6, 6}, {6, 0}, {7, 0}, {7, 7}, {0, 7}, {0, 0}, {3, 0},
			{3, 4}
		});
		showPath(new int[][] {
			{3, 4}, {4, 4}, {4, 1}, {1, 1}, {1, 6}, {6, 6}, {6, 0}, {7, 0},
			{7, 7}, {0, 7}, {0, 0}, {5, 0}, {5, 5}, {2, 5}, {2, 2}, {3, 2},
			{3, 4}
		});
		showPath(new int[][] {
			{3, 4}, {4, 4}, {4, 2}, {5, 2}, {5, 5}, {2, 5}, {2, 0}, {7, 0},
			{7, 7}, {0, 7}, {0, 0}, {1, 0}, {1, 6}, {6, 6}, {6, 1}, {3, 1},
			{3, 4}
		});
	}
}
