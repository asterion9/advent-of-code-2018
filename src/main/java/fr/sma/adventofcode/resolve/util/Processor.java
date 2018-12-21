package fr.sma.adventofcode.resolve.util;

import fr.sma.adventofcode.resolve.day16.InstructionLambda;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import one.util.streamex.StreamEx;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;
import static org.springframework.asm.Opcodes.ACC_PUBLIC;
import static org.springframework.asm.Opcodes.V11;

public class Processor {
	private static class DynamicClassLoader extends ClassLoader {
		public Class<?> defineClass(String name, byte[] b) {
			return defineClass(name, b, 0, b.length);
		}
	}
	
	public static final Pattern IP_LINE = Pattern.compile("#ip (\\d)");
	public static final Pattern OP_LINE = Pattern.compile("([a-z]{4}) (\\d+) (\\d+) (\\d+)");
	
	public static Processor build(String values) throws IOException {
		Matcher ipMatcher = IP_LINE.matcher(values.split("\n")[0]);
		ipMatcher.matches();
		int ip = Integer.parseInt(ipMatcher.group(1));
		
		List<int[]> instructions = StreamEx.split(values, "\n")
				.map(OP_LINE::matcher)
				.filter(Matcher::matches)
				.map(m -> new int[]{
						InstructionLambda.valueOf(m.group(1).toUpperCase()).ordinal(),
						Integer.parseInt(m.group(2)),
						Integer.parseInt(m.group(3)),
						Integer.parseInt(m.group(4))
				})
				.collect(Collectors.toList());
		
		ClassNode calcImpl = new ClassNode();
		calcImpl.version = V11;
		calcImpl.access = ACC_PUBLIC;
		calcImpl.interfaces = List.of("fr/sma/adventofcode/resolve/util/Calculator");
		calcImpl.name = "fr/sma/adventofcode/resolve/util/CalculatorImpl";
		
		MethodNode constructor = new MethodNode(ACC_PUBLIC,"<init>","()V",null,null);
		
		MethodNode calculator = new MethodNode(ACC_PUBLIC,"calculate","([I)I",null,null);
		InsnList insns = calculator.instructions;
		
		
	}
}
