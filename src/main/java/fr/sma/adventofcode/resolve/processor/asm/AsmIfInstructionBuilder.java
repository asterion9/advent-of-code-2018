package fr.sma.adventofcode.resolve.processor.asm;

import fr.sma.adventofcode.resolve.processor.BaseOperation;
import fr.sma.adventofcode.resolve.processor.InstructionLine;
import one.util.streamex.StreamEx;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;

import java.util.EnumSet;
import java.util.Set;

import static org.objectweb.asm.Opcodes.IFNE;

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
		return AsmInstructionBuilder.build(
				StreamEx.of(AsmInstructionBuilder.load(-1, -1, comparisonResultLoc)
						.append(new JumpInsnNode(IFNE, labelProvider.getForPosition(current.getPosition()+2))))
		);
	}
}
