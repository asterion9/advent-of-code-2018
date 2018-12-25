package fr.sma.adventofcode.resolve.processor.asm;

import one.util.streamex.StreamEx;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.VarInsnNode;

import static org.objectweb.asm.Opcodes.BIPUSH;
import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.IADD;
import static org.objectweb.asm.Opcodes.IAND;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.ICONST_1;
import static org.objectweb.asm.Opcodes.IF_ICMPEQ;
import static org.objectweb.asm.Opcodes.IF_ICMPGT;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.IMUL;
import static org.objectweb.asm.Opcodes.IOR;
import static org.objectweb.asm.Opcodes.ISTORE;

public enum InstructionSetAsm {
	
	ADDR((i, p, a, b, c) -> build(
			new VarInsnNode(ILOAD, a+1),
			new VarInsnNode(ILOAD, b+1),
			new InsnNode(IADD),
			new VarInsnNode(ISTORE, c+1))),
	ADDI((i, p, a, b, c) -> build(
			new VarInsnNode(ILOAD, a+1),
			new IntInsnNode(BIPUSH, b),
			new InsnNode(IADD),
			new VarInsnNode(ISTORE, c+1))),
	MULR((i, p, a, b, c) -> build(
			new VarInsnNode(ILOAD, a+1),
			new VarInsnNode(ILOAD, b+1),
			new InsnNode(IMUL),
			new VarInsnNode(ISTORE, c+1))),
	MULI((i, p, a, b, c) -> build(
			new VarInsnNode(ILOAD, a+1),
			new IntInsnNode(BIPUSH, b),
			new InsnNode(IMUL),
			new VarInsnNode(ISTORE, c+1))),
	BANR((i, p, a, b, c) -> build(
			new VarInsnNode(ILOAD, a+1),
			new IntInsnNode(ILOAD, b+1),
			new InsnNode(IAND),
			new VarInsnNode(ISTORE, c+1))),
	BANI((i, p, a, b, c) -> build(
			new VarInsnNode(ILOAD, a+1),
			new IntInsnNode(BIPUSH, b),
			new InsnNode(IAND),
			new VarInsnNode(ISTORE, c+1))),
	BORR((i, p, a, b, c) -> build(
			new VarInsnNode(ILOAD, a+1),
			new IntInsnNode(ILOAD, b+1),
			new InsnNode(IOR),
			new VarInsnNode(ISTORE, c+1))),
	BORI((i, p, a, b, c) -> build(
			new VarInsnNode(ILOAD, a+1),
			new IntInsnNode(BIPUSH, b),
			new InsnNode(IOR),
			new VarInsnNode(ISTORE, c+1))),
	SETR((i, p, a, b, c) -> build(
			new VarInsnNode(ILOAD, a+1),
			new VarInsnNode(ISTORE, c+1))),
	SETI((i, p, a, b, c) -> build(
			new IntInsnNode(BIPUSH, a),
			new VarInsnNode(ISTORE, c+1))),
	GTIR((i, p, a, b, c) -> {
		LabelNode skip = new LabelNode();
		LabelNode end = new LabelNode();
		return build(
				new IntInsnNode(BIPUSH, a),
				new IntInsnNode(ILOAD, b+1),
				new JumpInsnNode(IF_ICMPGT, skip),
				new InsnNode(ICONST_0),
				new JumpInsnNode(GOTO, end),
				skip,
				new InsnNode(ICONST_1),
				end,
				new VarInsnNode(ISTORE, c+1)
		);
	}),
	GTRI((i, p, a, b, c) -> {
		LabelNode skip = new LabelNode();
		LabelNode end = new LabelNode();
		return build(
				new IntInsnNode(ILOAD, a+1),
				new IntInsnNode(BIPUSH, b),
				new JumpInsnNode(IF_ICMPGT, skip),
				new InsnNode(ICONST_0),
				new JumpInsnNode(GOTO, end),
				skip,
				new InsnNode(ICONST_1),
				end,
				new VarInsnNode(ISTORE, c+1)
		);
	}),
	GTRR((i, p, a, b, c) -> {
		LabelNode skip = new LabelNode();
		LabelNode end = new LabelNode();
		return build(
				new IntInsnNode(ILOAD, a+1),
				new IntInsnNode(ILOAD, b+1),
				new JumpInsnNode(IF_ICMPGT, skip),
				new InsnNode(ICONST_0),
				new JumpInsnNode(GOTO, end),
				skip,
				new InsnNode(ICONST_1),
				end,
				new VarInsnNode(ISTORE, c+1)
		);
	}),
	EQIR((i, p, a, b, c) -> {
		LabelNode skip = new LabelNode();
		LabelNode end = new LabelNode();
		return build(
				new IntInsnNode(BIPUSH, a),
				new IntInsnNode(ILOAD, b+1),
				new JumpInsnNode(IF_ICMPEQ, skip),
				new InsnNode(ICONST_0),
				new JumpInsnNode(GOTO, end),
				skip,
				new InsnNode(ICONST_1),
				end,
				new VarInsnNode(ISTORE, c+1)
		);
	}),
	EQRI((i, p, a, b, c) -> {
		LabelNode skip = new LabelNode();
		LabelNode end = new LabelNode();
		return build(
				new IntInsnNode(ILOAD, a+1),
				new IntInsnNode(BIPUSH, b),
				new JumpInsnNode(IF_ICMPEQ, skip),
				new InsnNode(ICONST_0),
				new JumpInsnNode(GOTO, end),
				skip,
				new InsnNode(ICONST_1),
				end,
				new VarInsnNode(ISTORE, c+1)
		);
	}),
	EQRR((i, p, a, b, c) -> {
		LabelNode skip = new LabelNode();
		LabelNode end = new LabelNode();
		return build(
				new IntInsnNode(ILOAD, a+1),
				new IntInsnNode(ILOAD, b+1),
				new JumpInsnNode(IF_ICMPEQ, skip),
				new InsnNode(ICONST_0),
				new JumpInsnNode(GOTO, end),
				skip,
				new InsnNode(ICONST_1),
				end,
				new VarInsnNode(ISTORE, c+1)
		);
	});
	
	private InstructionAsmProvider instructionAsmProvider;
	
	InstructionSetAsm(InstructionAsmProvider instructionAsmProvider) {
		this.instructionAsmProvider = instructionAsmProvider;
	}
	
	public InsnList createCode(int a, int b, int c) {
		return instructionAsmProvider.build(i, p, a, b, c);
	}
	
	private static AbstractInsnNode read(int i, int p, int a) {
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
}
