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
import static org.objectweb.asm.Opcodes.BIPUSH;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.IADD;
import static org.objectweb.asm.Opcodes.IAND;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.ICONST_1;
import static org.objectweb.asm.Opcodes.IF_ICMPEQ;
import static org.objectweb.asm.Opcodes.IF_ICMPGT;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.IMUL;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.IOR;
import static org.objectweb.asm.Opcodes.ISTORE;

public class AsmInstructionBuilder {
	
	public static Map<BaseOperation, AsmInstructionProvider> instructionBuilders =
			StreamEx.of(
					Map.<BaseOperation, AsmInstructionProvider>entry(ADDR, (i, p, ints) ->
							build(
									load(i, p, ints[0]),
									load(i, p, ints[1]),
									new InsnNode(IADD),
									new VarInsnNode(ISTORE, ints[2] + 1))),
					Map.<BaseOperation, AsmInstructionProvider>entry(ADDI, (i, p, ints) ->
							build(load(i, p, ints[0]),
									new IntInsnNode(BIPUSH, ints[1]),
									new InsnNode(IADD),
									new VarInsnNode(ISTORE, ints[2] + 1))),
					Map.<BaseOperation, AsmInstructionProvider>entry(MULR,(i, p, ints) ->
							build(load(i, p, ints[0]),
									load(i, p, ints[1]),
									new InsnNode(IMUL),
									new VarInsnNode(ISTORE, ints[2]+1))),
					Map.<BaseOperation, AsmInstructionProvider>entry(MULI, (i, p, ints) ->
							build(load(i, p, ints[0]),
									new IntInsnNode(BIPUSH, ints[1]),
									new InsnNode(IMUL),
									new VarInsnNode(ISTORE, ints[2]+1))),
					Map.<BaseOperation, AsmInstructionProvider>entry(BANR, (i, p, ints) ->
							build(load(i, p, ints[0]),
									load(i, p, ints[1]),
									new InsnNode(IAND),
									new VarInsnNode(ISTORE, ints[2]+1))),
					Map.<BaseOperation, AsmInstructionProvider>entry(BANI, (i, p, ints) ->
							build(load(i, p, ints[0]),
									new IntInsnNode(BIPUSH, ints[1]),
									new InsnNode(IAND),
									new VarInsnNode(ISTORE, ints[2]+1))),
					Map.<BaseOperation, AsmInstructionProvider>entry(BORR, (i, p, ints) ->
							build(load(i, p, ints[0]),
									load(i, p, ints[1]),
									new InsnNode(IOR),
									new VarInsnNode(ISTORE, ints[2]+1))),
					Map.<BaseOperation, AsmInstructionProvider>entry(BORI, (i, p, ints) ->
							build(load(i, p, ints[0]),
									new IntInsnNode(BIPUSH, ints[1]),
									new InsnNode(IOR),
									new VarInsnNode(ISTORE, ints[2]+1))),
					Map.<BaseOperation, AsmInstructionProvider>entry(SETR, (i, p, ints) ->
							build(load(i, p, ints[0]),
									new VarInsnNode(ISTORE, ints[2]+1))),
					Map.<BaseOperation, AsmInstructionProvider>entry(SETI, (i, p, ints) ->
							build(new IntInsnNode(BIPUSH, ints[0]),
									new VarInsnNode(ISTORE, ints[2]+1))),
					Map.<BaseOperation, AsmInstructionProvider>entry(GTIR, (i, p, ints) -> {
						InsnList build = build(
								new IntInsnNode(BIPUSH, ints[0]),
								load(i, p, ints[1])
						);
						build.add(buildIfElse(ints[2], IF_ICMPGT));
						return build;
					}),
					Map.<BaseOperation, AsmInstructionProvider>entry(GTRI, (i, p, ints) -> {
						InsnList build = build(
								load(i, p, ints[0]),
								new IntInsnNode(BIPUSH, ints[1])
						);
						build.add(buildIfElse(ints[2], IF_ICMPGT));
						return build;
					}),
					Map.<BaseOperation, AsmInstructionProvider>entry(GTRR, (i, p, ints) -> {
						InsnList build = build(
								load(i, p, ints[0]),
								load(i, p, ints[1])
						);
						build.add(buildIfElse(ints[2], IF_ICMPGT));
						return build;
					}),
					Map.<BaseOperation, AsmInstructionProvider>entry(EQIR, (i, p, ints) -> {
						InsnList build = build(
								new IntInsnNode(BIPUSH, ints[0]),
								new IntInsnNode(ILOAD, ints[1] + 1)
						);
						build.add(buildIfElse(ints[2], IF_ICMPEQ));
						return build;
					}),
					Map.<BaseOperation, AsmInstructionProvider>entry(EQRI, (i, p, ints) ->	{
						InsnList build = build(
								load(i, p, ints[0]),
								new IntInsnNode(BIPUSH, ints[1])
						);
						build.add(buildIfElse(ints[2], IF_ICMPEQ));
						return build;
					}),
					Map.<BaseOperation, AsmInstructionProvider>entry(EQRR, (i, p, ints) -> {
						InsnList build = build(
								load(i, p, ints[0]),
								load(i, p, ints[1])
						);
						build.add(buildIfElse(ints[2], IF_ICMPEQ));
						return build;
					})
			).mapToEntry(Map.Entry::getKey, Map.Entry::getValue).toMap();
	
	
	public static InsnList compileInstruction(int pointerLoc, InstructionLine iline) {
		return instructionBuilders.get(iline.getOperation()).build(iline.getPosition(), pointerLoc, iline.getInputs());
	}
	
	private static InsnList buildIfElse(int c, int ifOpCode) {
		LabelNode skip = new LabelNode();
		LabelNode end = new LabelNode();
		InsnList insns = new InsnList();
		insns.add(new JumpInsnNode(ifOpCode, skip));
		insns.add(new InsnNode(ICONST_0));
		insns.add(new JumpInsnNode(GOTO, end));
		insns.add(skip);
		insns.add(new InsnNode(ICONST_1));
		insns.add(end);
		insns.add(new VarInsnNode(ISTORE, c + 1));
		return insns;
	}
	
	private static AbstractInsnNode load(int i, int p, int a) {
		if (a == p) {
			return new IntInsnNode(BIPUSH, i);
		} else {
			return new IntInsnNode(ILOAD, a + 1);
		}
	}
	
	private static InsnList build(AbstractInsnNode... nodes) {
		InsnList list = new InsnList();
		StreamEx.of(nodes).forEach(list::add);
		return list;
	}
	
	public static InsnList println(int regIndex) {
		InsnList insnList = new InsnList();
		insnList.add(new FieldInsnNode(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
		insnList.add(new IntInsnNode(ILOAD, regIndex + 1));
		insnList.add(new MethodInsnNode(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false));
		return insnList;
	}
	
	interface AsmInstructionProvider {
		InsnList build(int i, int p, int[] inputs);
	}
}
