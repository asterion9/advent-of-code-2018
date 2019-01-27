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
import org.objectweb.asm.tree.LdcInsnNode;
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
import static org.objectweb.asm.Opcodes.DUP_X2;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.ICONST_M1;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.IFGT;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.LADD;
import static org.objectweb.asm.Opcodes.LALOAD;
import static org.objectweb.asm.Opcodes.LAND;
import static org.objectweb.asm.Opcodes.LASTORE;
import static org.objectweb.asm.Opcodes.LCMP;
import static org.objectweb.asm.Opcodes.LCONST_0;
import static org.objectweb.asm.Opcodes.LMUL;
import static org.objectweb.asm.Opcodes.LOR;
import static org.objectweb.asm.Opcodes.POP;
import static org.objectweb.asm.Opcodes.SIPUSH;

public class AsmInstructionBuilder {
	
	public static Map<BaseOperation, AsmInstructionProvider> instructionBuilders =
			StreamEx.of(
					Map.<BaseOperation, AsmInstructionProvider>entry(ADDR, (i, p, ints) -> build(load(i, p, ints[0]).append(load(i, p, ints[1])).append(new InsnNode(LADD)).append(store(ints[2])))),
					Map.<BaseOperation, AsmInstructionProvider>entry(ADDI, (i, p, ints) -> build(load(i, p, ints[0]).append(pushLongConst(ints[1])).append(new InsnNode(LADD)).append(store(ints[2])))),
					Map.<BaseOperation, AsmInstructionProvider>entry(MULR, (i, p, ints) -> build(load(i, p, ints[0]).append(load(i, p, ints[1])).append(new InsnNode(LMUL)).append(store(ints[2])))),
					Map.<BaseOperation, AsmInstructionProvider>entry(MULI, (i, p, ints) -> build(load(i, p, ints[0]).append(pushLongConst(ints[1])).append(new InsnNode(LMUL)).append(store(ints[2])))),
					Map.<BaseOperation, AsmInstructionProvider>entry(BANR, (i, p, ints) -> build(load(i, p, ints[0]).append(load(i, p, ints[1])).append(new InsnNode(LAND)).append(store(ints[2])))),
					Map.<BaseOperation, AsmInstructionProvider>entry(BANI, (i, p, ints) -> build(load(i, p, ints[0]).append(pushLongConst(ints[1])).append(new InsnNode(LAND)).append(store(ints[2])))),
					Map.<BaseOperation, AsmInstructionProvider>entry(BORR, (i, p, ints) -> build(load(i, p, ints[0]).append(load(i, p, ints[1])).append(new InsnNode(LOR)).append(store(ints[2])))),
					Map.<BaseOperation, AsmInstructionProvider>entry(BORI, (i, p, ints) -> build(load(i, p, ints[0]).append(pushLongConst(ints[1])).append(new InsnNode(LOR)).append(store(ints[2])))),
					Map.<BaseOperation, AsmInstructionProvider>entry(SETR, (i, p, ints) -> build(load(i, p, ints[0]).append(store(ints[2])))),
					Map.<BaseOperation, AsmInstructionProvider>entry(SETI, (i, p, ints) -> build(StreamEx.of(pushLongConst(ints[0])).append(store(ints[2])))),
					Map.<BaseOperation, AsmInstructionProvider>entry(GTIR, (i, p, ints) -> build(StreamEx.of(pushLongConst(ints[0])).append(load(i, p, ints[1])).append(buildCompareThenIf(ints[2], IFGT)))),
					Map.<BaseOperation, AsmInstructionProvider>entry(GTRI, (i, p, ints) -> build(load(i, p, ints[0]).append(pushLongConst(ints[1])).append(buildCompareThenIf(ints[2], IFGT)))),
					Map.<BaseOperation, AsmInstructionProvider>entry(GTRR, (i, p, ints) -> build(load(i, p, ints[0]).append(load(i, p, ints[1])).append(buildCompareThenIf(ints[2], IFGT)))),
					Map.<BaseOperation, AsmInstructionProvider>entry(EQIR, (i, p, ints) -> build(StreamEx.of(pushLongConst(ints[0])).append(load(i, p, ints[1])).append(buildCompareThenIf(ints[2], IFEQ)))),
					Map.<BaseOperation, AsmInstructionProvider>entry(EQRI, (i, p, ints) -> build(load(i, p, ints[0]).append(pushLongConst(ints[1])).append(buildCompareThenIf(ints[2], IFEQ)))),
					Map.<BaseOperation, AsmInstructionProvider>entry(EQRR, (i, p, ints) -> build(load(i, p, ints[0]).append(load(i, p, ints[1])).append(buildCompareThenIf(ints[2], IFEQ))))
			).mapToEntry(Map.Entry::getKey, Map.Entry::getValue).toMap();
	
	
	public static InsnList compileInstruction(int pointerLoc, InstructionLine iline) {
		return instructionBuilders.get(iline.getOperation()).build(iline.getPosition(), pointerLoc, iline.getInputs());
	}
	
	private static StreamEx<AbstractInsnNode> buildCompareThenIf(int c, int ifOpCode) {
		LabelNode skip = new LabelNode();
		LabelNode end = new LabelNode();
		return StreamEx.of(
				new InsnNode(LCMP),
				new JumpInsnNode(ifOpCode, skip),
				pushLongConst(0),
				new JumpInsnNode(GOTO, end),
				skip,
				pushLongConst(1),
				end
		).append(store(c));
	}
	
	public static StreamEx<AbstractInsnNode> load(int i, int p, int a) {
		if (a == p) {
			return StreamEx.of(pushLongConst(i));
		} else {
			return StreamEx.<AbstractInsnNode>of(new VarInsnNode(ALOAD, 1))
					.append(pushIntConst(a))
					.append(new InsnNode(LALOAD));
		}
	}
	
	public static StreamEx<AbstractInsnNode> store(int a) {
		return StreamEx.<AbstractInsnNode>of(new VarInsnNode(ALOAD, 1)) // load array ref
				.append(new InsnNode(DUP_X2)).append(new InsnNode(POP)) // push it under the value that was on the stack
				.append(pushIntConst(a)) // push the array index where to store the value
				.append(new InsnNode(DUP_X2)).append(new InsnNode(POP)) // push it under the value that was on the stack
				.append(new InsnNode(LASTORE)); // store the value into the array
	}
	
	public static AbstractInsnNode pushIntConst(int value) {
		if(value >= -1 && value <= 5) {
			return new InsnNode(ICONST_M1 + value + 1);
		} else if(value >= Byte.MIN_VALUE && value <= Byte.MAX_VALUE) {
			return new IntInsnNode(BIPUSH, value);
		} else if(value >= Short.MIN_VALUE && value <= Short.MAX_VALUE) {
			return new IntInsnNode(SIPUSH, value);
		} else {
			return new LdcInsnNode(value);
		}
	}
	
	public static AbstractInsnNode pushLongConst(long value) {
		if(value >= 0 && value <= 1) {
			return new InsnNode((int) (LCONST_0 + value));
		} else {
			return new LdcInsnNode(Long.valueOf(value));
		}
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
				.append(new MethodInsnNode(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(J)V", false))
		);
	}
	
	interface AsmInstructionProvider {
		InsnList build(int i, int p, int[] inputs);
	}
}
