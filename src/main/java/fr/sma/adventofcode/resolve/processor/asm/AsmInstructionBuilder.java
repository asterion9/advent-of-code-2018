package fr.sma.adventofcode.resolve.processor.asm;

import fr.sma.adventofcode.resolve.processor.BaseOperation;
import fr.sma.adventofcode.resolve.processor.InstructionLine;
import one.util.streamex.StreamEx;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.util.Map;

import static fr.sma.adventofcode.resolve.processor.BaseOperation.ADDI;
import static fr.sma.adventofcode.resolve.processor.BaseOperation.ADDR;
import static fr.sma.adventofcode.resolve.processor.BaseOperation.BANI;
import static fr.sma.adventofcode.resolve.processor.BaseOperation.BANR;
import static fr.sma.adventofcode.resolve.processor.BaseOperation.BORI;
import static fr.sma.adventofcode.resolve.processor.BaseOperation.BORR;
import static fr.sma.adventofcode.resolve.processor.BaseOperation.EQIR;
import static fr.sma.adventofcode.resolve.processor.BaseOperation.EQRI;
import static fr.sma.adventofcode.resolve.processor.BaseOperation.EQRR;
import static fr.sma.adventofcode.resolve.processor.BaseOperation.GTIR;
import static fr.sma.adventofcode.resolve.processor.BaseOperation.GTRI;
import static fr.sma.adventofcode.resolve.processor.BaseOperation.GTRR;
import static fr.sma.adventofcode.resolve.processor.BaseOperation.MULI;
import static fr.sma.adventofcode.resolve.processor.BaseOperation.MULR;
import static fr.sma.adventofcode.resolve.processor.BaseOperation.SETI;
import static fr.sma.adventofcode.resolve.processor.BaseOperation.SETR;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.BIPUSH;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.IADD;
import static org.objectweb.asm.Opcodes.IALOAD;
import static org.objectweb.asm.Opcodes.IAND;
import static org.objectweb.asm.Opcodes.IASTORE;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.ICONST_1;
import static org.objectweb.asm.Opcodes.IF_ICMPEQ;
import static org.objectweb.asm.Opcodes.IF_ICMPGT;
import static org.objectweb.asm.Opcodes.IMUL;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.IOR;
import static org.objectweb.asm.Opcodes.SWAP;

public class AsmInstructionBuilder {
	
	public static Map<BaseOperation, AsmInstructionProvider> instructionBuilders =
			StreamEx.of(
					Map.<BaseOperation, AsmInstructionProvider>entry(ADDR, (i, p, ints) -> build(load(i, p, ints[0]).append(load(i, p, ints[1])).append(new InsnNode(IADD)).append(store(ints[2])))),
					Map.<BaseOperation, AsmInstructionProvider>entry(ADDI, (i, p, ints) -> build(load(i, p, ints[0]).append(new IntInsnNode(BIPUSH, ints[1])).append(new InsnNode(IADD)).append(store(ints[2])))),
					Map.<BaseOperation, AsmInstructionProvider>entry(MULR, (i, p, ints) -> build(load(i, p, ints[0]).append(load(i, p, ints[1])).append(new InsnNode(IMUL)).append(store(ints[2])))),
					Map.<BaseOperation, AsmInstructionProvider>entry(MULI, (i, p, ints) -> build(load(i, p, ints[0]).append(new IntInsnNode(BIPUSH, ints[1])).append(new InsnNode(IMUL)).append(store(ints[2])))),
					Map.<BaseOperation, AsmInstructionProvider>entry(BANR, (i, p, ints) -> build(load(i, p, ints[0]).append(load(i, p, ints[1])).append(new InsnNode(IAND)).append(store(ints[2])))),
					Map.<BaseOperation, AsmInstructionProvider>entry(BANI, (i, p, ints) -> build(load(i, p, ints[0]).append(new IntInsnNode(BIPUSH, ints[1])).append(new InsnNode(IAND)).append(store(ints[2])))),
					Map.<BaseOperation, AsmInstructionProvider>entry(BORR, (i, p, ints) -> build(load(i, p, ints[0]).append(load(i, p, ints[1])).append(new InsnNode(IOR)).append(store(ints[2])))),
					Map.<BaseOperation, AsmInstructionProvider>entry(BORI, (i, p, ints) -> build(load(i, p, ints[0]).append(new IntInsnNode(BIPUSH, ints[1])).append(new InsnNode(IOR)).append(store(ints[2])))),
					Map.<BaseOperation, AsmInstructionProvider>entry(SETR, (i, p, ints) -> build(load(i, p, ints[0]).append(store(ints[2])))),
					Map.<BaseOperation, AsmInstructionProvider>entry(SETI, (i, p, ints) -> build(StreamEx.<AbstractInsnNode>of(new IntInsnNode(BIPUSH, ints[0])).append(store(ints[2])))),
					Map.<BaseOperation, AsmInstructionProvider>entry(GTIR, (i, p, ints) -> build(StreamEx.<AbstractInsnNode>of(new IntInsnNode(BIPUSH, ints[0])).append(load(i, p, ints[1])).append(buildIfElse(ints[2], IF_ICMPGT)))),
					Map.<BaseOperation, AsmInstructionProvider>entry(GTRI, (i, p, ints) -> build(load(i, p, ints[0]).append(new IntInsnNode(BIPUSH, ints[1])).append(buildIfElse(ints[2], IF_ICMPGT)))),
					Map.<BaseOperation, AsmInstructionProvider>entry(GTRR, (i, p, ints) -> build(load(i, p, ints[0]).append(load(i, p, ints[1])).append(buildIfElse(ints[2], IF_ICMPGT)))),
					Map.<BaseOperation, AsmInstructionProvider>entry(EQIR, (i, p, ints) -> build(StreamEx.<AbstractInsnNode>of(new IntInsnNode(BIPUSH, ints[0])).append(load(i, p, ints[1])).append(buildIfElse(ints[2], IF_ICMPEQ)))),
					Map.<BaseOperation, AsmInstructionProvider>entry(EQRI, (i, p, ints) -> build(load(i, p, ints[0]).append(new IntInsnNode(BIPUSH, ints[1])).append(buildIfElse(ints[2], IF_ICMPEQ)))),
					Map.<BaseOperation, AsmInstructionProvider>entry(EQRR, (i, p, ints) -> build(load(i, p, ints[0]).append(load(i, p, ints[1])).append(buildIfElse(ints[2], IF_ICMPEQ))))
			).mapToEntry(Map.Entry::getKey, Map.Entry::getValue).toMap();
	
	
	public static InsnList compileInstruction(int pointerLoc, InstructionLine iline) {
		return instructionBuilders.get(iline.getOperation()).build(iline.getPosition(), pointerLoc, iline.getInputs());
	}
	
	private static StreamEx<AbstractInsnNode> buildIfElse(int c, int ifOpCode) {
		LabelNode skip = new LabelNode();
		LabelNode end = new LabelNode();
		return StreamEx.of(
				new JumpInsnNode(ifOpCode, skip),
				new InsnNode(ICONST_0),
				new JumpInsnNode(GOTO, end),
				skip,
				new InsnNode(ICONST_1),
				end
		).append(store(c));
	}
	
	public static StreamEx<AbstractInsnNode> load(int i, int p, int a) {
		if (a == p) {
			return StreamEx.of(new IntInsnNode(BIPUSH, i));
		} else {
			return StreamEx.<AbstractInsnNode>of(new VarInsnNode(ALOAD, 1))
					.append(new IntInsnNode(BIPUSH, a))
					.append(new InsnNode(IALOAD));
		}
	}
	
	public static StreamEx<AbstractInsnNode> store(int a) {
		return StreamEx.<AbstractInsnNode>of(new VarInsnNode(ALOAD, 1)) // load array ref
				.append(new InsnNode(SWAP)) // push it under the value that was on the stack
				.append(new IntInsnNode(BIPUSH, a)) // push the array index where to store the value
				.append(new InsnNode(SWAP))// push it under the value that was on the stack
				.append(new InsnNode(IASTORE)); // store the value into the array
	}
	
	public static InsnList build(StreamEx<AbstractInsnNode> nodes) {
		InsnList list = new InsnList();
		nodes.forEach(list::add);
		return list;
	}
	
	public static InsnList println(int regIndex) {
		return build(
				StreamEx.<AbstractInsnNode>of(new FieldInsnNode(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"))
				.append(load(-1, -1, regIndex))
				.append(new MethodInsnNode(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false))
		);
	}
	
	interface AsmInstructionProvider {
		InsnList build(int i, int p, int[] inputs);
	}
}
