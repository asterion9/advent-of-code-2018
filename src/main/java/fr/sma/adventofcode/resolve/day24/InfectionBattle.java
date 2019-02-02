package fr.sma.adventofcode.resolve.day24;

import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class InfectionBattle {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	private static Pattern GROUP_PATTERN = Pattern.compile("(\\d+) units each with (\\d+) hit points( .*)with an attack that does (\\d+) (\\w+) damage at initiative (\\d+)");
	private Set<Group> immuneSystemGroups;
	private Set<Group> infectionGroups;
	
	public InfectionBattle(Set<Group> immuneSystemGroups, Set<Group> infectionGroups) {
		this.immuneSystemGroups = immuneSystemGroups;
		this.infectionGroups = infectionGroups;
	}
	
	public static InfectionBattle build(String inputs) {
		//init groups
		String[] armies = inputs.split("\n\n");
		
		AtomicInteger count = new AtomicInteger(1);
		Matcher groupMatcher = GROUP_PATTERN.matcher(armies[0]);
		groupMatcher.matches();
		Set<Group> immuneSystemGroups = groupMatcher.results()
				.map((MatchResult gl) -> Group.build(gl, Group.TYPE.IMMUNITY, count.getAndIncrement()))
				.collect(Collectors.toSet());
		
		count.set(1);
		groupMatcher = GROUP_PATTERN.matcher(armies[1]);
		groupMatcher.matches();
		Set<Group> infectionGroups = groupMatcher.results()
				.map((MatchResult gl) -> Group.build(gl, Group.TYPE.INFECTION, count.getAndIncrement()))
				.collect(Collectors.toSet());
		
		return new InfectionBattle(immuneSystemGroups, infectionGroups);
	}
	
	public Set<Group> doBattle(int immuneSystemBoost) {
		Set<Group> immuneSystemGroupsCopy = StreamEx.of(this.immuneSystemGroups).map(Group::clone).peek(ig -> ig.setAp(ig.getAp() + immuneSystemBoost)).toSet();
		Set<Group> infectionGroupsCopy =StreamEx.of(this.infectionGroups).map(Group::clone).toSet();
		
		while(immuneSystemGroupsCopy.size() > 0 && infectionGroupsCopy.size() > 0) {
			//logger.debug("immune system has " + StreamEx.of(immuneSystemGroupsCopy).mapToInt(Group::getNb).sum() + " unit left");
			//logger.debug("infection has " + StreamEx.of(infectionGroupsCopy).mapToInt(Group::getNb).sum() + " unit left");
			
			//  build attack list
			List<Group> remainingInfect = new ArrayList<>(infectionGroupsCopy);
			List<Group> remainingImmuneSys = new ArrayList<>(immuneSystemGroupsCopy);
			
			Map<Group, Group> immuneAttackList = buildAttackList(immuneSystemGroupsCopy, remainingInfect);
			
			Map<Group, Group> infectedAttackList = buildAttackList(infectionGroupsCopy, remainingImmuneSys);
			
			//  run attacks
			int nbDead = EntryStream.of(immuneAttackList)
					.append(infectedAttackList)
					.reverseSorted(Comparator.comparing(e -> e.getKey().getIni()))
					.peekKeyValue((a,  t) -> logger.debug(a.toString() + " attacks " + t.toString() + " for " + a.getDammageTo(t) + " killings " + Math.min(t.getNb(), a.getDammageTo(t)/t.getHp()) + " units"))
					.mapKeyValue(Group::attack)
					.mapToInt(i -> i)
					.sum();
			
			if(nbDead == 0) {
				break;
			}
			
			immuneSystemGroupsCopy.removeIf(g -> g.getNb() <= 0);
			infectionGroupsCopy.removeIf(g -> g.getNb() <= 0);
		}
		logger.debug("immune system has " + StreamEx.of(immuneSystemGroupsCopy).mapToInt(Group::getNb).sum() + " unit left");
		logger.debug("infection has " + StreamEx.of(infectionGroupsCopy).mapToInt(Group::getNb).sum() + " unit left");
		
		if(infectionGroupsCopy.isEmpty()) {
			return immuneSystemGroupsCopy;
		} else {
			return infectionGroupsCopy;
		}
	}
	
	private Map<Group, Group> buildAttackList(Set<Group> attackingGroups, List<Group> remainingDefenders) {
		return StreamEx.of(attackingGroups)
				.sorted(Group.targetChooseComparator)
				.mapToEntry(attacker -> StreamEx.of(remainingDefenders).max(attacker.targetAttackComparator))
				.flatMapValues(Optional::stream)
				.filterKeyValue((a, t) -> a.getDammageTo(t) > 0)
				//.peekKeyValue((a, t) -> logger.debug("attacker " + a.toString() + " would deal " + a.getDammageTo(t) + " damage to group " + t.toString()))
				.peekValues(remainingDefenders::remove)
				.toMap();
	}
}
