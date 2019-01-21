package fr.sma.adventofcode.resolve.processor.lambda;

import fr.sma.adventofcode.resolve.processor.BaseOperation;
import fr.sma.adventofcode.resolve.processor.InstructionLine;
import one.util.streamex.StreamEx;

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

public class LambdaInstructionBuilder {
	
	public static Map<BaseOperation, LambdaInstructionProvider> instructionBuilders =
			StreamEx.of(
				Map.<BaseOperation, LambdaInstructionProvider>entry(ADDR, i -> r -> r[i[2]] = r[i[0]] + r[i[1]]),
				Map.<BaseOperation, LambdaInstructionProvider>entry(ADDI, i -> r -> r[i[2]] = r[i[0]] + i[1]),
				Map.<BaseOperation, LambdaInstructionProvider>entry(MULR, i -> r -> r[i[2]] = r[i[0]] * r[i[1]]),
				Map.<BaseOperation, LambdaInstructionProvider>entry(MULI, i -> r -> r[i[2]] = r[i[0]] * i[1]),
				Map.<BaseOperation, LambdaInstructionProvider>entry(BANR, i -> r -> r[i[2]] = r[i[0]] & r[i[1]]),
				Map.<BaseOperation, LambdaInstructionProvider>entry(BANI, i -> r -> r[i[2]] = r[i[0]] & i[1]),
				Map.<BaseOperation, LambdaInstructionProvider>entry(BORR, i -> r -> r[i[2]] = r[i[0]] | r[i[1]]),
				Map.<BaseOperation, LambdaInstructionProvider>entry(BORI, i -> r -> r[i[2]] = r[i[0]] | i[1]),
				Map.<BaseOperation, LambdaInstructionProvider>entry(SETR, i -> r -> r[i[2]] = r[i[0]]),
				Map.<BaseOperation, LambdaInstructionProvider>entry(SETI, i -> r -> r[i[2]] = i[0]),
				Map.<BaseOperation, LambdaInstructionProvider>entry(GTIR, i -> r -> r[i[2]] = i[0] > r[i[1]] ? 1 : 0),
				Map.<BaseOperation, LambdaInstructionProvider>entry(GTRI, i -> r -> r[i[2]] = r[i[0]] > i[1] ? 1 : 0),
				Map.<BaseOperation, LambdaInstructionProvider>entry(GTRR, i -> r -> r[i[2]] = r[i[0]] > r[i[1]] ? 1 : 0),
				Map.<BaseOperation, LambdaInstructionProvider>entry(EQIR, i -> r -> r[i[2]] = i[0] == r[i[1]] ? 1 : 0),
				Map.<BaseOperation, LambdaInstructionProvider>entry(EQRI, i -> r -> r[i[2]] = r[i[0]] == i[1] ? 1 : 0),
				Map.<BaseOperation, LambdaInstructionProvider>entry(EQRR, i -> r -> r[i[2]] = r[i[0]] == r[i[1]] ? 1 : 0)
	).mapToEntry(Map.Entry::getKey, Map.Entry::getValue).toMap();
	
	public static NamedInstruction build(int pointerLoc, InstructionLine iline) {
		return new NamedInstruction(
				r -> {
					instructionBuilders.get(iline.getOperation()).build(iline.getInputs()).execute(r);
					r[pointerLoc]++;
				},
				iline.getReadIndexes(),
				StreamEx.of(iline.getWriteIndexes()).append(pointerLoc).toSet()
		);
	}
	
	public static NamedInstruction build(InstructionLine iline) {
		return new NamedInstruction(
				r -> instructionBuilders.get(iline.getOperation()).build(iline.getInputs()).execute(r),
				iline.getReadIndexes(),
				iline.getWriteIndexes()
		);
	}
	
	interface LambdaInstructionProvider {
		Instruction build(int[] inputs);
	}
}
