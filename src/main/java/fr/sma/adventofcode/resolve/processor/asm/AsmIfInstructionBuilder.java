package fr.sma.adventofcode.resolve.processor.asm;

import fr.sma.adventofcode.resolve.processor.BaseOperation;
import fr.sma.adventofcode.resolve.processor.InstructionLine;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.util.EnumSet;
import java.util.Set;

import static org.objectweb.asm.Opcodes.IFNE;
import static org.objectweb.asm.Opcodes.ILOAD;

public class AsmIfInstructionBuilder {
	public static boolean doesApplyTo(int pointerLoc, InstructionLine previous, InstructionLine current) {
		if(EnumSet.range(BaseOperation.GTIR, BaseOperation.EQRR).contains(previous.getOperation())) {
			int writeResultLoc = previous.getWriteIndexes().iterator().next();
			if(current.getOperation() == BaseOperation.ADDR) {
				return current.getReadIndexes().containsAll(Set.of(writeResultLoc, pointerLoc)) && current.getWriteIndexes().contains(pointerLoc);
			}
		}
		return false;
	}
	
	public static InsnList compileInstruction(int pointerLoc, InstructionLine previous, InstructionLine current, LabelProvider labelProvider) {
		int comparisonResultLoc = previous.getWriteIndexes().iterator().next();
		InsnList insnList = new InsnList();
		insnList.add(new VarInsnNode(ILOAD, comparisonResultLoc+1)); // load the result of the comparison (0 or 1) onto the stack
		insnList.add(new JumpInsnNode(IFNE, labelProvider.getForPosition(current.getPosition()+2)));
		return insnList;
	}
}
