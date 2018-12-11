package fr.sma.adventofcode.resolve.day10;

import com.google.common.math.PairedStatsAccumulator;

import java.util.List;

class Sky {
	private final List<Star> starList;
	
	public Sky(List<Star> starList) {
		this.starList = starList;
	}
	
	public double calculateStarArea(int t) {
		PairedStatsAccumulator stat = new PairedStatsAccumulator();
		starList.forEach(s -> stat.add(s.getX(t), s.getY(t)));
		return (stat.xStats().max() - stat.xStats().min()) * (stat.yStats().max() - stat.yStats().min());
	}
	
	public String printSky(int t) {
		PairedStatsAccumulator statsAccumulator = new PairedStatsAccumulator();
		starList.forEach(s -> statsAccumulator.add(s.getX(t), s.getY(t)));
		
		int xmin = (int) statsAccumulator.xStats().min();
		int ymin = (int) statsAccumulator.yStats().min();
		int xmax = (int) statsAccumulator.xStats().max();
		int ymax = (int) statsAccumulator.yStats().max();
		
		char[][] sky = new char[ymax-ymin +1][xmax-xmin +1];
		starList.forEach(s -> sky[s.getY(t) - ymin][s.getX(t) - xmin] = '#');
		
		StringBuilder s = new StringBuilder();
		for (char[] line : sky) {
			for (char c : line) {
				s.append(c == '#' ? c : ' ');
			}
			s.append('\n');
		}
		
		return s.toString();
	}
}
