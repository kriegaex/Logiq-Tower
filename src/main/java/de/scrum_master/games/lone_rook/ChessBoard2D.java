package de.scrum_master.games.lone_rook;

// $Id: ChessBoard2D.java,v 1.2 2006/01/12 00:07:24 Robin Exp $

import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JPanel;

public final class ChessBoard2D extends JPanel
{
	static final long serialVersionUID = -5634944224811437033L;
	private static final Color bg = Color.white;
	private static final Color fg = Color.black;
	private int[][] path;

	private ChessBoard2D(int[][] path)
	{
		this.path = path;
	}

	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		Dimension d = getSize();
		int length = Math.min(d.width, d.height) / 8;
		g2.setStroke(new BasicStroke(length / 20F, 1, 1));
		boolean isBlack = true;
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				g2.setPaint((isBlack = !isBlack) ? fg : bg);
				g2.fill(new Rectangle(col * length, row * length, length, length));
			}
			isBlack = !isBlack;
		}

		for (int i = 0; i < path.length - 1; i++) {
			int[] from = path[i];
			int[] to = path[i + 1];
			int fromX = from[0] * length + length / 2;
			int fromY = (7 - from[1]) * length + length / 2;
			int toX = to[0] * length + length / 2;
			int toY = (7 - to[1]) * length + length / 2;
			if (i == 0) {
				g2.setPaint(Color.blue);
				g2.draw(
					new java.awt.geom.Ellipse2D.Double(
						fromX - (length * 3) / 8,
						fromY - (length * 3) / 8,
						(length * 3) / 4,
						(length * 3) / 4
					)
				);
			}
			g2.setPaint(Color.red);
			g2.draw(new java.awt.geom.Line2D.Double(fromX, fromY, toX, toY));
			int headSize = length / 5;
			int head1X, head1Y, head2X, head2Y;
			if (to[0] == from[0]) {
				if (to[1] > from[1]) {
					head1X = toX - headSize;
					head1Y = toY + headSize;
					head2X = toX + headSize;
					head2Y = toY + headSize;
				}
				else {
					head1X = toX - headSize;
					head1Y = toY - headSize;
					head2X = toX + headSize;
					head2Y = toY - headSize;
				}
			}
			else if (to[0] > from[0]) {
				head1X = toX - headSize;
				head1Y = toY + headSize;
				head2X = toX - headSize;
				head2Y = toY - headSize;
			}
			else {
				head1X = toX + headSize;
				head1Y = toY + headSize;
				head2X = toX + headSize;
				head2Y = toY - headSize;
			}
			g2.draw(new java.awt.geom.Line2D.Double(head1X, head1Y, toX, toY));
			g2.draw(new java.awt.geom.Line2D.Double(head2X, head2Y, toX, toY));
		}

	}

	public static void showPath(int[][] path)
	{
		JFrame f = new JFrame("Chess Board");
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.getContentPane().add(new ChessBoard2D(path), "Center");
	    // No window title, borders or icons. In combination with maximisation,
		// full screen can be used for painting. Good for screenshots!
	    //f.setUndecorated(true);
		f.pack();
		f.setSize(new Dimension(320, 320));
		// Center on screen & maximise
	    //f.setLocationRelativeTo(null);
		//f.setExtendedState(JFrame.MAXIMIZED_BOTH);
		// Let system window manager determine position
		f.setLocationByPlatform(true);
		f.setVisible(true);
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
