package de.scrum_master.games.lone_rook;

// $Id: LoneRook.java,v 1.2 2006/01/13 03:33:18 Robin Exp $

import java.io.PrintStream;
import java.util.Vector;

import vhs.Timer;

/**
 *
 * @author Alexander Kriegisch
 * <p>
 * Bild der Wissenschaft<br>
 * "Cogito"-Monatsrätsel - Mai 2004<br>
 * Der einsame Turm
 * <p>
 * "Hilfst du mir beim Abwasch?", fragte mich meine Frau und meinte dann, ohne
 * auf meine Antwort zu warten: "Du kannst schon einmal anfangen. Ich muss
 * vorher noch kurz Gertrud anrufen." Ich stöhnte innerlich auf, aber sagte
 * ergeben: "Ja." Wenn meine Frau mit ihrer Freundin telefoniert, dauert das
 * immer mindestens eine Stunde. Den Abwasch würde ich also alleine machen
 * müssen. Ich hatte gerade heißes Wasser ins Spülbecken gelassen, als mir
 * einfiel, dass meine Tochter mir eigentlich helfen könnte. Christina, die im
 * Wohnzimmer saß, erwiderte auf meine Bitte nur: "Stör mich nicht. Du siehst
 * doch, ich spiele Schach."
 * <p>
 * Vor meiner Tochter auf dem Tisch lag ein Schachbrett, auf dem nur ein
 * einzelner weißer Turm stand. Sonst war keine Figur zu sehen. Konzentriert
 * starrte Christina auf das Brett. Ich beobachtete sie eine Zeit lang und sagte
 * dann: "Ich glaube, du willst mich auf den Arm nehmen und dich nur vor dem
 * Abwasch drücken. Viel verstehe ich ja nicht von Schach, aber soviel weiß ich
 * doch, dass ein einzelner Turm keine sinnvolle Schachstellung ergeben kann."
 * Genervt wandte sich Christina von dem Spiel ab und mir zu. "Eigentlich hat es
 * keinen Zweck, dir dieses Schachproblem zu erklären. Du wirst es doch nicht
 * lösen können. Aber wenn du darauf bestehst, werde ich es versuchen."
 * Beleidigt erwiderte ich: "Du unterschätzt mich."
 * <p>
 * Christina verdrehte die Augen und sagte: "Also, pass auf. Der Turm steht auf
 * dem Feld d5 des Schachbretts. Er soll mit möglichst wenigen Zügen einmal über
 * alle Felder des Bretts geführt werden und zum Schluss wieder zum Ausgangsfeld
 * zurückgelangen. Dabei darf der Turm kein Feld mehrmals betreten. Eine
 * Ausnahme ist natürlich das Startfeld, das ja gleichzeitig das Zielfeld ist,
 * und darum insgesamt zweimal betreten werden muss. Der Turm darf
 * selbstverständlich nur die beim Schach üblichen Züge machen. Ein Feld gilt
 * auch dann als betreten, wenn der Turm bei einem Zug nur über dieses Feld
 * läuft. Zieht also beispielsweise der Turm von a1 nach a4, so hat er dabei
 * auch die Felder a2 und a3 betreten. Wie viele Züge sind mindestens für diese
 * Rundtour notwendig?"
 * <p>
 * "Das kann doch nicht so schwer sein", dachte ich und probierte einige Minuten
 * mit dem Turm herum. Mein bestes Ergebnis waren 22 Züge. Christina lächelte
 * überlegen und meinte, sie hätte doch Recht gehabt, ich sei mit dem Problem
 * überfordert.
 * <p>
 * Wissen Sie, was die kleinste Anzahl von Zügen ist, mit der der Turm seinen
 * Rundweg schaffen kann?
 *
 */

public class LoneRookClassic
{
	enum Heading
	{
		NORTH, SOUTH, EAST, WEST
	}

	private class Path implements Cloneable
	{
		int[][] path = new int[64][2];
		int steps;
		int moves;

		public void push(int x, int y)
		{
			steps++;
			if (steps == 1 || steps == 2 && startPos[0] != x
				&& startPos[1] != y || steps > 2 && path[steps - 3][0] != x
				&& path[steps - 3][1] != y)
				moves++;
			path[steps - 1][0] = x;
			path[steps - 1][1] = y;
		}

		public int[] pop()
		{
			assert steps > 0;
			int x = path[steps - 1][0];
			int y = path[steps - 1][1];

			if (steps == 1 || steps == 2 && startPos[0] != x
				&& startPos[1] != y || steps > 2 && path[steps - 3][0] != x
				&& path[steps - 3][1] != y)
				moves--;
			steps--;
			return path[steps];
		}

		public int[] peek()
		{
			return path[steps - 1];
		}

		public int getSteps()
		{
			return steps;
		}

		public int getMoves()
		{
			return moves;
		}

		protected Path clone() throws CloneNotSupportedException
		{
			Path clonedPath = new Path();
			clonedPath.steps = steps;
			clonedPath.moves = moves;
			for (int i = 0; i < path.length; i++) {
				clonedPath.path[i][0] = path[i][0];
				clonedPath.path[i][1] = path[i][1];
			}
			return clonedPath;
		}

		int[][] toOptimisedArray()
		{
			int[][] shortPath = new int[moves + 1][2];
			shortPath[0][0] = startPos[0];
			shortPath[0][1] = startPos[1];
			shortPath[moves][0] = startPos[0];
			shortPath[moves][1] = startPos[1];

			//shortPath[0] = shortPath[moves] = startPos;
			//System.arraycopy(startPos, 0, shortPath[0], 0, 2);
			//System.arraycopy(startPos, 0, shortPath[moves], 0, 2);
			int move = 1;
			if (path[1][0] != startPos[0] && path[1][1] != startPos[1])
			{
				//System.arraycopy(path[0], 0, shortPath[move++], 0, 2);
				shortPath[move][0] = path[0][0];
				shortPath[move][1] = path[0][1];
				move++;
			}
			for (int i = 2; i < steps; i++)
				if (path[i][0] != path[i - 2][0] && path[i][1] != path[i - 2][1])
				{
					//System.arraycopy(path[i - 1], 0, shortPath[move++], 0, 2);
					shortPath[move][0] = path[i - 1][0];
					shortPath[move][1] = path[i - 1][1];
					move++;
				}
			return shortPath;
		}

		public String toString()
		{
			int[][] optiPath = toOptimisedArray();
			StringBuilder result = new StringBuilder(
				getClass().getName() + "(" + moves + " / ");
			for (int i = 0; i <= moves; i++) {
				if (i > 0)
					result.append('-');
				result
					.append((char) (optiPath[i][0] + 97))
					.append((char) (optiPath[i][1] + 49));
			}
			return result.append(')').toString();
		}
	}

	boolean[][] board = new boolean[8][8];
	Path currentPath = new Path();
	final Vector<Path> optimalPaths = new Vector<Path>();
	final int[] startPos;
	int minMoves = 64;
	Heading heading;

	final private static boolean[][] floodFillArray = new boolean[8][8];
	private static int floodFillCount;

	private static Timer time;
	private static PrintStream out = System.out;
	private static boolean textMode = true;

	public LoneRookClassic(int startX, int startY, Heading startHeading)
	{
		startPos = new int[] {
			startX, startY
		};
		heading = startHeading;
	}

	private void turnLeft()
	{
		/*
		 * Weird detail: Don't use qualified name (e.g. Heading.NORTH) in case
		 * label - otherwise there will be an error: "The enum constant
		 * LoneRook_OO.Heading.NORTH reference cannot be qualified in a case
		 * label". But do use it in the case action block below, because there
		 * you need it again. Obviously the label inherits the context from the
		 * object in the switch statement.
		 */

		switch (heading) {
			case NORTH:
				heading = Heading.WEST;
				break;
			case WEST:
				heading = Heading.SOUTH;
				break;
			case SOUTH:
				heading = Heading.EAST;
				break;
			case EAST:
				heading = Heading.NORTH;
				break;
		}
	}

	private void turnAround()
	{
		// Weird detail concerning case label: cf. comment in turnLeft().

		switch (heading) {
			case NORTH:
				heading = Heading.SOUTH;
				break;
			case WEST:
				heading = Heading.EAST;
				break;
			case SOUTH:
				heading = Heading.NORTH;
				break;
			case EAST:
				heading = Heading.WEST;
				break;
		}
	}

	private static void floodFill_N(int x, int y)
	{
		if (++y > 7 || floodFillArray[x][y])
			return;
		floodFillArray[x][y] = true;
		floodFillCount++;
		floodFill_N(x, y);
		floodFill_E(x, y);
		floodFill_W(x, y);
	}

	private static void floodFill_S(int x, int y)
	{
		if (--y < 0 || floodFillArray[x][y])
			return;
		floodFillArray[x][y] = true;
		floodFillCount++;
		floodFill_S(x, y);
		floodFill_E(x, y);
		floodFill_W(x, y);
	}

	private static void floodFill_E(int x, int y)
	{
		if (++x > 7 || floodFillArray[x][y])
			return;
		floodFillArray[x][y] = true;
		floodFillCount++;
		floodFill_N(x, y);
		floodFill_S(x, y);
		floodFill_E(x, y);
	}

	private static void floodFill_W(int x, int y)
	{
		if (--x < 0 || floodFillArray[x][y])
			return;
		floodFillArray[x][y] = true;
		floodFillCount++;
		floodFill_N(x, y);
		floodFill_S(x, y);
		floodFill_W(x, y);
	}

	private static void floodFill(int x, int y)
	{
		if (x < 0 || x > 7 || y < 0 || y > 7 || floodFillArray[x][y])
			return;
		floodFillArray[x][y] = true;
		floodFillCount++;
		floodFill_N(x, y);
		floodFill_S(x, y);
		floodFill_E(x, y);
		floodFill_W(x, y);
	}

	private boolean goAhead()
	{
		int newX = 0, newY = 0;
		int[] currentSquare = (currentPath.getSteps() == 0)
			? startPos
			: currentPath.peek();
		switch (heading) {
			case NORTH:
				if (currentSquare[1] == 7
					|| board[newX = currentSquare[0]][newY = currentSquare[1] + 1])
					return false;
				break;
			case WEST:
				if (currentSquare[0] == 0
					|| board[newX = currentSquare[0] - 1][newY = currentSquare[1]])
					return false;
				break;
			case SOUTH:
				if (currentSquare[1] == 0
					|| board[newX = currentSquare[0]][newY = currentSquare[1] - 1])
					return false;
				break;
			case EAST:
				if (currentSquare[0] == 7
					|| board[newX = currentSquare[0] + 1][newY = currentSquare[1]])
					return false;
				break;
		}

		/*
		 * Check if push makes sense via flood-fill (ff), but only use ff after
		 * at least 8 squares on the board are blocked, because otherwise it is
		 * impossible to split the board into two separate parts - at least not
		 * when starting from d5.
		 */
		if ((floodFillCount = currentPath.getSteps() + 1) >= 8) {
			for (int i = 0; i < 8; i++) {
				System.arraycopy(board[i], 0, floodFillArray[i], 0, 8);
			}

			floodFillArray[newX][newY] = true;
			floodFill(startPos[0], startPos[1]);
			assert floodFillCount > 0 && floodFillCount < 65;
			if (floodFillCount < 64)
				return false;
		}

		currentPath.push(newX, newY);
		if (currentPath.getMoves() > minMoves || currentPath.getSteps() == 64
			&& (newX != startPos[0] || newY != startPos[1])
			|| currentPath.getSteps() < 64 && newX == startPos[0]
			&& newY == startPos[1]) {
			currentPath.pop();
			return false;
		}
		assert !board[newX][newY];
		board[newX][newY] = true;
		return true;
	}

	private void goBack()
	{
		int[] s = currentPath.pop();
		assert board[s[0]][s[1]];
		board[s[0]][s[1]] = false;
	}

	public void findOptimalPath() throws CloneNotSupportedException
	{
		assert currentPath.getMoves() <= minMoves;
		if (currentPath.getSteps() == 64) {
			if (currentPath.getMoves() < minMoves) {
				minMoves = currentPath.getMoves();
				out.println("\nNew optimal path length: " + minMoves + " moves");
				optimalPaths.clear();
			}
			optimalPaths.add(currentPath.clone());
			out.println(time + " - " + currentPath);
			/*
			if (textMode)
				ChessBoardText.showPath(currentPath.toOptimisedArray());
			else
				ChessBoard2D.showPath(currentPath.toOptimisedArray());
			*/
			return;
		}
		if (goAhead()) {
			findOptimalPath();
			goBack();
		}
		turnLeft();
		if (goAhead()) {
			findOptimalPath();
			goBack();
		}
		turnAround();
		if (goAhead()) {
			findOptimalPath();
			goBack();
		}
		turnLeft();
	}

	static void solve(Heading heading) throws CloneNotSupportedException
	{
		time = new Timer();
		// Put rook on d5 and look for optimal solution
		LoneRookClassic rook = new LoneRookClassic(3, 4, heading);
		/*
		 * Take first step manually in order to avoid looking left and right
		 * from starting point. For symmetry reasons - north = west and south =
		 * east, if you start at d5 - the first step should go straight ahead
		 * without looking left and right.
		 */
		out.println(time + " - 1st half going " + rook.heading.name());
		rook.goAhead();
		rook.findOptimalPath();
		rook.goBack();
		// Turn towards opposite direction and search on
		rook.turnAround();
		// Again, take first step manually (see above).
		out.println("\n" + time + " - 2nd half going " + rook.heading.name());
		rook.goAhead();
		rook.findOptimalPath();
		rook.goBack();
		out.println("\n" + time + " - completed");
		// TODO: check rook.optimalPaths for symmetric solutions
		for (Path path : rook.optimalPaths)
			if (textMode)
				ChessBoardText.showPath(path.toOptimisedArray());
			else
				ChessBoard2D.showPath(path.toOptimisedArray());
	}

	public static void main(String[] args) throws CloneNotSupportedException
	{
		solve(Heading.EAST);
	}
}
