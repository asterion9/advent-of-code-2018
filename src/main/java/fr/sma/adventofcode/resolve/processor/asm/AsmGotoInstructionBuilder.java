package fr.sma.adventofcode.resolve.processor.asm;

import fr.sma.adventofcode.resolve.processor.InstructionLine;
import fr.sma.adventofcode.resolve.processor.lambda.LambdaInstructionBuilder;
import fr.sma.adventofcode.resolve.processor.lambda.NamedInstruction;
import one.util.streamex.IntStreamEx;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;

import static org.objectweb.asm.Opcodes.GOTO;

public class AsmGotoInstructionBuilder {
	
	public static boolean doesApplyTo(int pointerLoc, InstructionLine iline) {
		if(iline.getReadIndexes().isEmpty() || (iline.getReadIndexes().size() == 1 && iline.getReadIndexes().contains(pointerLoc))) {
			return iline.getWriteIndexes().contains(pointerLoc);
		}
		return false;
	}
	
	public static InsnList compileInstruction(int pointerLoc, InstructionLine iline, LabelProvider labelProvider) {
		NamedInstruction buildedInstruction = LambdaInstructionBuilder.build(pointerLoc, iline);
		int maxIndex = IntStreamEx.of(iline.getReadIndexes()).append(IntStreamEx.of(iline.getWriteIndexes())).append(pointerLoc).max().getAsInt();
		long[] register = new long[maxIndex + 1];
		register[pointerLoc] = iline.getPosition();
		buildedInstruction.execute(register);
		long gotoDestination = register[pointerLoc];
		InsnList insnList = new InsnList();
		if(gotoDestination > Integer.MAX_VALUE) {
			gotoDestination = Integer.MAX_VALUE;
		}
		insnList.add(new JumpInsnNode(GOTO, labelProvider.getForPosition((int) gotoDestination)));
		return insnList;
	}
}
