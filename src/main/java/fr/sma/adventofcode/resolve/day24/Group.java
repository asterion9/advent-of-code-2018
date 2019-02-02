package fr.sma.adventofcode.resolve.day24;

import one.util.streamex.StreamEx;

import java.util.Comparator;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class Group implements Cloneable {
	public static final Comparator<Group> targetChooseComparator = Comparator.comparingInt(Group::getEffectivePower).reversed().thenComparing(Comparator.comparingInt(Group::getIni).reversed());
	public final Comparator<Group> targetAttackComparator = Comparator.comparing(this::getDammageTo).thenComparing(Group::getEffectivePower).thenComparing(Group::getIni);
	
	private static Pattern WEAK_PATTERN = Pattern.compile("weak to ([\\w ,]+)");
	private static Pattern IMMUNE_PATTERN = Pattern.compile("immune to ([\\w ,]+)");
	
	private int nb;
	private int hp;
	private int ap;
	private String attackType;
	private Set<String> weaknesses;
	private Set<String> immunities;
	private int ini;
	private final TYPE type;
	private final String id;
	
	public static Group build(MatchResult gl, TYPE type, int num) {
		int nb = Integer.parseInt(gl.group(1));
		int hp = Integer.parseInt(gl.group(2));
		Set<String> weaknesses = Set.of();
		Matcher weaknessMatcher = WEAK_PATTERN.matcher(gl.group(3));
		if (weaknessMatcher.find())
			weaknesses = StreamEx.split(weaknessMatcher.group(1), ", ").collect(Collectors.toSet());
		Set<String> immunities = Set.of();
		Matcher immunityMatcher = IMMUNE_PATTERN.matcher(gl.group(3));
		if (immunityMatcher.find())
			immunities = StreamEx.split(immunityMatcher.group(1), ", ").collect(Collectors.toSet());
		int ap = Integer.parseInt(gl.group(4));
		String attackType = gl.group(5);
		int ini = Integer.parseInt(gl.group(6));
		return new Group(nb, hp, ap, attackType, weaknesses, immunities, ini, type, num);
	}
	
	protected Group clone() {
		try {
			return (Group) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
	
	public void setAp(int ap) {
		this.ap = ap;
	}
	
	public int getAp() {
		return ap;
	}
	
	public Group(int nb, int hp, int ap, String attackType, Set<String> weaknesses, Set<String> immunities, int ini, TYPE type, int id) {
		this.id = type.name() + " " + id;
		this.nb = nb;
		this.hp = hp;
		this.ap = ap;
		this.attackType = attackType;
		this.weaknesses = weaknesses;
		this.immunities = immunities;
		this.ini = ini;
		this.type = type;
	}
	
	public TYPE getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return id + " (nb="+nb+", ep=" + getEffectivePower() + ", ini=" +ini+")";
	}
	
	public String getId() {
		return id;
	}
	
	public int getIni() {
		return ini;
	}
	
	public int getNb() {
		return nb;
	}
	
	public int getHp() {
		return hp;
	}
	
	public int getEffectivePower() {
		return ap * nb;
	}
	
	public int getDammageTo(Group target) {
		if(target.immunities.contains(attackType)) {
			return 0;
		}
		return getEffectivePower() * (target.weaknesses.contains(attackType) ? 2 : 1);
	}
	
	public int attack(Group target) {
		int dammageTo = getDammageTo(target);
		if(dammageTo <= 0) {
			return 0;
		}
		int nbdead = dammageTo / target.hp;
		target.nb -= nbdead;
		if(target.nb < 0) {
			target.nb = 0;
		}
		
		return nbdead;
	}
	public enum TYPE {
		IMMUNITY,
		INFECTION
	}
	
}
