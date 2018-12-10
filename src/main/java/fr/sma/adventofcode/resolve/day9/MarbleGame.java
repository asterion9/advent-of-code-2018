package fr.sma.adventofcode.resolve.day9;

import java.util.Arrays;

public class MarbleGame {
	private long[] scoreTable;
	
	private int curMarble = 1;
	
	private CircularDoublyLinkedList marbleRing = new CircularDoublyLinkedList(0L);
	
	public long[] getScore() {
		return Arrays.copyOf(scoreTable, scoreTable.length);
	}
	
	public MarbleGame(int nbPlayer) {
		scoreTable = new long[nbPlayer];
	}
	
	public void run(int nbTurn) {
		int stopTurn = curMarble + nbTurn;
		for (; curMarble < stopTurn; curMarble++) {
			int player = (curMarble-1)%scoreTable.length;
			if(curMarble%23 == 0) {
				int marbleWorth=curMarble;
				marbleRing.move(-7);
				marbleWorth += marbleRing.remove();
				scoreTable[player] += marbleWorth;
			} else {
				marbleRing.move(1);
				marbleRing.insertAfter((long) curMarble);
				marbleRing.move(1);
			}
		}
	}
}
