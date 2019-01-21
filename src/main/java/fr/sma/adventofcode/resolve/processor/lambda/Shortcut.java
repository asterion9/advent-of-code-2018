package fr.sma.adventofcode.resolve.processor.lambda;

import one.util.streamex.EntryStream;
import one.util.streamex.IntStreamEx;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

public class Shortcut implements Instruction, Comparable<Shortcut> {
	private StateMatcher startState;
	private StateMatcher endState;
	private int depth;
	
	public Shortcut(int[] startState, int[] endState) {
		this.startState = new StateMatcher(startState);
		this.endState = new StateMatcher(endState);
		this.depth = 1;
	}
	
	public Shortcut(int[] beforeState, Set<Integer> readIndexes, int[] after, Set<Integer> writeIndexes) {
		this.depth = 1;
		this.startState = new StateMatcher(IntStreamEx.of(readIndexes).boxed().mapToEntry(i -> beforeState[i]), beforeState.length);
		this.endState = new StateMatcher(IntStreamEx.of(writeIndexes).boxed().mapToEntry(i -> after[i]), beforeState.length);
	}
	
	public int getDepth() {
		return depth;
	}
	
	private Shortcut(StateMatcher newStartState, StateMatcher newEndState, int depth) {
		this.startState = newStartState;
		this.endState = newEndState;
		this.depth = depth;
	}
	
	public boolean appliesTo(int[] register) {
		return startState.matches(register);
	}
	
	public boolean canExpandInto(Shortcut nextShortcut) {
		return StateMatcher.matches(endState, nextShortcut.startState);
	}
	
	public void execute(int[] state) {
		endState.apply(state);
	}
	
	public int getStartStateSize() {
		int i=0;
		for (int j = 0; j < startState.state.length; j++) {
			if(startState.state[j] != -1) {
				i++;
			}
		}
		return i;
	}
	
	public static Shortcut expandShortcut(Shortcut firstShortcut, Shortcut nextShortcut) {
		StateMatcher newStartState = StateMatcher.merge(firstShortcut.startState, nextShortcut.startState);
		StateMatcher newEndState = StateMatcher.merge(nextShortcut.endState, firstShortcut.endState);
		return new Shortcut(newStartState, newEndState, firstShortcut.depth + nextShortcut.depth);
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Shortcut)) return false;
		Shortcut shortcut = (Shortcut) o;
		return Objects.equals(startState, shortcut.startState);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(startState);
	}
	
	@Override
	public int compareTo(Shortcut o) {
		return Arrays.compare(startState.state, o.startState.state);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < startState.state.length; i++) {
			sb.append("(").append(startState.state[i]).append("->").append(endState.state[i]).append(")");
		}
		return "[" + depth + " : " + sb + "]";
	}
	
	private static class StateMatcher {
		private int[] state;
		
		StateMatcher(EntryStream<Integer, Integer> matchingStates, int size) {
			state = new int[size];
			Arrays.fill(state, -1);
			matchingStates.forKeyValue((i, s) -> state[i] = s);
		}
		
		StateMatcher(int[] state) {
			this.state = Arrays.copyOf(state, state.length);
		}
		
		void apply(int[] state) {
			for (int i = 0; i < this.state.length; i++) {
				if(this.state[i] != -1) {
					state[i] = this.state[i];
				}
			}
		}
		
		boolean matches(int[] register) {
			for (int i = 0; i < state.length; i++) {
				if(state[i] != -1 && state[i] != register[i]) {
					return false;
				}
			}
			return true;
		}
		
		static boolean matches(StateMatcher first, StateMatcher then) {
			for (int i = 0; i < then.state.length; i++) {
				if(then.state[i] != -1 && first.state[i] != -1) { // elements must match
					if(then.state[i] != first.state[i]) {
						return false;
					}
				}
			}
			return true;
		}
		
		static StateMatcher merge(StateMatcher first, StateMatcher then) {
			int[] newState = new int[first.state.length];
			Arrays.fill(newState, -1);
			
			for (int i = 0; i < newState.length; i++) {
				if(first.state[i] != -1) {
					newState[i] = first.state[i];
				} else if (then.state[i] != -1) {
					newState[i] = then.state[i];
				}
			}
			return new StateMatcher(newState);
		}
		
		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof StateMatcher)) return false;
			StateMatcher that = (StateMatcher) o;
			return Arrays.equals(state, that.state);
		}
		
		@Override
		public int hashCode() {
			return Arrays.hashCode(state);
		}
	}
}
