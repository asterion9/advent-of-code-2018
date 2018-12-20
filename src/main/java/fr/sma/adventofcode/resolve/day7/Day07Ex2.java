package fr.sma.adventofcode.resolve.day7;

import fr.sma.adventofcode.resolve.util.DataFetcher;
import fr.sma.adventofcode.resolve.ExSolution;

import java.util.Optional;
import java.util.PriorityQueue;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Day07Ex2 implements ExSolution {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final static Pattern LINE_PATTERN = Pattern.compile("Step ([A-Z]) must be finished before step ([A-Z]) can begin.");
	
	private final static int NB_WORKER = 5;
	
	@Autowired
	private DataFetcher dataFetcher;
	
	@Override
	public void run() throws Exception {
		System.out.println("Day07Ex2");
		
		String values = dataFetcher.fetch(7).trim();
		
		StepQueue<TimeStep> steps = new StepQueue<>(StreamEx.split(values, "\n")
				.map(LINE_PATTERN::matcher)
				.filter(Matcher::matches)
				.flatMap(matcher -> {
					TimeStep first = new TimeStep(matcher.group(1));
					TimeStep second = new TimeStep(matcher.group(2), first);
					return StreamEx.of(first, second);
				})
				.sorted()
				.collapse(TimeStep::equals, TimeStep::absorb)
				.collect(Collectors.toList()));
		
		WorkerPool workers = new WorkerPool();
		
		int time = 0;
		while (steps.hasNext()) {
			Optional<TimeStep> acquiredStep;
			if(workers.hasWorker() && (acquiredStep = steps.acquireStep()).isPresent()) {
				workers.addWork(acquiredStep.get(), time);
			} else {
				time = workers.completeNextWork(steps::releaseStep);
			}
		}
		
		System.out.println(time);
	}
	
	private static class WorkerPool {
		private final PriorityQueue<Worker> workers;
		
		public WorkerPool() {
			workers = new PriorityQueue<>(5);
		}
		
		public int completeNextWork(Consumer<TimeStep> callBack) {
			return workers.stream() //ordered stream
					.peek(w -> callBack.accept(w.getWork())) // end task
					.peek(workers::remove) // remove worker
					.mapToInt(Worker::getBlockUntil) // getIn its end time
					.findFirst() // for the first one
					.orElse(0);
		}
		
		public boolean hasWorker() {
			return workers.size() <= NB_WORKER;
		}
		
		public void addWork(TimeStep step, int time) {
			workers.add(new Worker(step, time));
		}
	}
	
	
	private static class Worker implements Comparable<Worker> {
		final int blockUntil;
		final TimeStep work;
		
		public Worker(TimeStep work, int time) {
			this.work = work;
			this.blockUntil = time+work.duration();
		}
		
		public Worker() {
			this.blockUntil = 0;
			this.work = null;
		}
		
		public int getBlockUntil() {
			return blockUntil;
		}
		
		public TimeStep getWork() {
			return work;
		}
		
		@Override
		public int compareTo(Worker o) {
			return Integer.compare(blockUntil, o.getBlockUntil());
		}
	}
}