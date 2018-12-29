package fr.sma.adventofcode.resolve.processor.asm;

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

public enum InstructionSetAsm {
	
	ADDR((i, p, a, b, c) -> {
		InsnList build = build(
				load(i, p, a),
				load(i, p, b),
				new InsnNode(IADD),
				new VarInsnNode(ISTORE, c + 1));
		if(c == 0) {
			build.add(println(0));
		}
		return build;
	}),
	ADDI((i, p, a, b, c) -> build(
			load(i, p, a),
			new IntInsnNode(BIPUSH, b),
			new InsnNode(IADD),
			new VarInsnNode(ISTORE, c + 1))),
	MULR((i, p, a, b, c) -> build(
			load(i, p, a),
			load(i, p, b),
			new InsnNode(IMUL),
			new VarInsnNode(ISTORE, c+1))),
	MULI((i, p, a, b, c) -> build(
			load(i, p, a),
			new IntInsnNode(BIPUSH, b),
			new InsnNode(IMUL),
			new VarInsnNode(ISTORE, c+1))),
	BANR((i, p, a, b, c) -> build(
			load(i, p, a),
			load(i, p, b),
			new InsnNode(IAND),
			new VarInsnNode(ISTORE, c+1))),
	BANI((i, p, a, b, c) -> build(
			load(i, p, a),
			new IntInsnNode(BIPUSH, b),
			new InsnNode(IAND),
			new VarInsnNode(ISTORE, c+1))),
	BORR((i, p, a, b, c) -> build(
			load(i, p, a),
			load(i, p, b),
			new InsnNode(IOR),
			new VarInsnNode(ISTORE, c+1))),
	BORI((i, p, a, b, c) -> build(
			load(i, p, a),
			new IntInsnNode(BIPUSH, b),
			new InsnNode(IOR),
			new VarInsnNode(ISTORE, c+1))),
	SETR((i, p, a, b, c) -> build(
			load(i, p, a),
			new VarInsnNode(ISTORE, c+1))),
	SETI((i, p, a, b, c) -> build(
			new IntInsnNode(BIPUSH, a),
			new VarInsnNode(ISTORE, c+1))),
	GTIR((i, p, a, b, c) -> {
		InsnList build = build(
				new IntInsnNode(BIPUSH, a),
				load(i, p, b)
		);
		build.add(buildIfElse(c, IF_ICMPGT));
		return build;
	}),
	GTRI((i, p, a, b, c) -> {
		InsnList build = build(
				load(i, p, a),
				new IntInsnNode(BIPUSH, b)
		);
		build.add(buildIfElse(c, IF_ICMPGT));
		return build;
	}),
	GTRR((i, p, a, b, c) -> {
		InsnList build = build(
				load(i, p, a),
				load(i, p, b)
		);
		build.add(buildIfElse(c, IF_ICMPGT));
		return build;
	}),
	EQIR((i, p, a, b, c) -> {
		InsnList build = build(
				new IntInsnNode(BIPUSH, a),
				new IntInsnNode(ILOAD, b+1)
		);
		build.add(buildIfElse(c, IF_ICMPEQ));
		return build;
	}),
	EQRI((i, p, a, b, c) -> {
		InsnList build = build(
				load(i, p, a),
				new IntInsnNode(BIPUSH, b)
		);
		build.add(buildIfElse(c, IF_ICMPEQ));
		return build;
	}),
	EQRR((i, p, a, b, c) -> {
		InsnList build = build(
				load(i, p, a),
				load(i, p, b)
		);
		build.add(buildIfElse(c, IF_ICMPEQ));
		return build;
	});
	
	private static InsnList buildIfElse(int c, int insn) {
		LabelNode skip = new LabelNode();
		LabelNode end = new LabelNode();
		InsnList insns = new InsnList();
		insns.add(new JumpInsnNode(insn, skip));
		insns.add(new InsnNode(ICONST_0));
		insns.add(new JumpInsnNode(GOTO, end));
		insns.add(skip);
		insns.add(new InsnNode(ICONST_1));
		insns.add(end);
		insns.add(new VarInsnNode(ISTORE, c + 1));
		return insns;
	}
	
	private InstructionAsmProvider instructionAsmProvider;
	
	InstructionSetAsm(InstructionAsmProvider instructionAsmProvider) {
		this.instructionAsmProvider = instructionAsmProvider;
	}
	
	public InsnList createCode(int i, int p, int a, int b, int c) {
		return instructionAsmProvider.build(i, p, a, b, c);
	}
	
	private static AbstractInsnNode load(int i, int p, int a) {
		if(a == p) {
			return new IntInsnNode(BIPUSH, i);
		} else {
			return new IntInsnNode(ILOAD, a+1);
		}
	}
	
	private static InsnList build(AbstractInsnNode... nodes) {
		InsnList list = new InsnList();
		StreamEx.of(nodes).forEach(list::add);
		return list;
	}
	
	private static InsnList println(int regIndex) {
		InsnList insnList = new InsnList();
		insnList.add(new FieldInsnNode(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
		insnList.add(new IntInsnNode(ILOAD, regIndex+1));
		insnList.add(new MethodInsnNode(INVOKEVIRTUAL, "java/io/PrintStream","println","(I)V", false));
		return insnList;
	}
	
}
