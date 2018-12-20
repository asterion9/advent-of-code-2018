package fr.sma.adventofcode.resolve.day15;

public class Unit implements Element {
	private int hp;
	private final Type type;
	private final int attackPoint;
	
	public Unit(Type type) {
		this.type = type;
		this.hp = 200;
		this.attackPoint = 3;
	}
	
	public Type getTargetType() {
		if (type == Type.ELF) {
			return Type.GOBELIN;
		}else {
			return Type.ELF;
		}
	}
	
	public int getHp() {
		return hp;
	}
	
	public void setHp(int hp) {
		this.hp = hp;
	}
	
	public int getAttackPoint() {
		return attackPoint;
	}
	
	@Override
	public Type getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return type + "(" + hp + ")";
	}
}
