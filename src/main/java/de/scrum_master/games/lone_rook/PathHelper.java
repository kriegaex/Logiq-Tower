package de.scrum_master.games.lone_rook;

import java.util.HashMap;
import java.util.Map;

public class PathHelper {
	/**
	 * Heuristic method which simply checks how often each start/end position in a chess move occurs
	 * in the list. There is absolutely no check for intersections in the path, duplicate moves,
	 * validity of moves (e.g. "a1-h2" is permitted), how many times a square is touched by the moves,
	 * consecutive order of moves and so forth. The heuristic assumes that all these factors were
	 * already considered when generating the list of moves.
	 *
	 * @param moves list of moves like "a4-a8", "h7-h1"
	 * @return 'true' if each start/end position in the path occurs exactly twice as it would in a
	 * closed path (start point equals end point), 'false' otherwise
	 */
	public static boolean isClosedPath(String... moves) {
		Map<String, Integer> positionCounts = new HashMap<>();
		for (String position : String.join("-", moves).split("-")) {
			positionCounts.put(
				position,
				1 + (positionCounts.containsKey(position) ? positionCounts.get(position) : 0)
			);
		}
		return positionCounts.values()
			.stream()
			.filter(count -> count != 2)
			.count() == 0;
	}

	public static void main(String[] args) {
		System.out.println(isClosedPath("a2-a4", "a4-h4", "h4-h8", "h8-f8", "f8-a2"));
		System.out.println(isClosedPath("a2-a4", "a4-h4", "h4-h8", "h8-f8", "f8-a3"));
		System.out.println(isClosedPath("a2-a4", "a2-a4"));
	}
}
