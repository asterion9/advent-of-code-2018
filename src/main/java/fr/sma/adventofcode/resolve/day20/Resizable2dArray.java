package fr.sma.adventofcode.resolve.day20;

import com.esotericsoftware.kryo.util.IntMap;

import java.util.Objects;

public class Resizable2dArray<T> {
	private IntMap<IntMap<T>> array = new IntMap<>();
	private int xMin = Integer.MAX_VALUE;
	private int yMin = Integer.MAX_VALUE;
	private int xMax = Integer.MIN_VALUE;
	private int yMax = Integer.MIN_VALUE;
	
	public T computeIfAbsent(int x, int y, MappingFunction<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		T v;
		if ((v = get(x, y)) == null) {
			T newValue;
			if ((newValue = mappingFunction.apply(x, y)) != null) {
				put(x, y, newValue);
				return newValue;
			}
		}
		return v;
	}
	
	public T computeIfPresent(int x, int y, RemappingFunction<T> remappingFunction) {
		Objects.requireNonNull(remappingFunction);
		T oldValue;
		if ((oldValue = get(x, y)) != null) {
			T newValue = remappingFunction.apply(x, y, oldValue);
			if (newValue != null) {
				put(x, y, newValue);
				return newValue;
			} else {
				remove(x, y);
				return null;
			}
		} else {
			return null;
		}
	}
	
	public T remove(int x, int y) {
		return put(x, y, null);
	}
	
	public T get(int x, int y) {
		IntMap<T> yMap = array.get(x);
		if(yMap != null) {
			return yMap.get(y);
		}
		return null;
	}
	
	public int getxMin() {
		return xMin;
	}
	
	public int getyMin() {
		return yMin;
	}
	
	public int getxMax() {
		return xMax;
	}
	
	public int getyMax() {
		return yMax;
	}
	
	public T put(int x, int y, T value) {
		if(x > xMax)
			xMax = x;
		if(x < xMin)
			xMin = x;
		if(y > yMax)
			yMax = y;
		if(y < yMin)
			yMin = y;
		IntMap<T> yMap = array.get(x);
		if(yMap == null) {
			yMap = new IntMap<>();
			array.put(x, yMap);
		}
		yMap.put(y, value);
		return value;
	}
	
	public interface RemappingFunction<V> {
		V apply(int x, int y, V value);
	}
	
	public interface MappingFunction<V> {
		V apply(int x, int y);
	}
}
