package fr.sma.adventofcode.resolve.processor.asm;

import fr.sma.adventofcode.resolve.processor.Cpu;
import fr.sma.adventofcode.resolve.processor.InstructionLine;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.RETURN;
import static org.objectweb.asm.Opcodes.V11;

public class CpuAsmBuilder {
	
	public static class DynamicClassLoader extends ClassLoader {
		
		public DynamicClassLoader() {
			super(DynamicClassLoader.class.getClassLoader());
		}
		
		public Class<?> defineClass(String name, byte[] b) {
			return super.defineClass(name, b, 0, b.length);
		}
	}
	
	public static Cpu buildDynamic(int pointerLoc, List<InstructionLine> code) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException, IOException, URISyntaxException {
		ClassNode calcImpl = new ClassNode();
		calcImpl.version = V11;
		calcImpl.access = ACC_PUBLIC;
		calcImpl.interfaces = List.of(Cpu.class.getName().replace(".", "/"));
		calcImpl.name = CpuAsmBuilder.class.getPackageName().replace(".", "/") + "/CpuAsm";
		calcImpl.superName = "java/lang/Object";
		
		
		MethodNode constructor = new MethodNode(ACC_PUBLIC,"<init>","()V",null,null);
		constructor.instructions.add(new VarInsnNode(ALOAD, 0));
		constructor.instructions.add(new MethodInsnNode(INVOKESPECIAL,"java/lang/Object","<init>", "()V",false));
		constructor.instructions.add(new InsnNode(RETURN));
		
		MethodNode calculator = new MethodNode(ACC_PUBLIC,"calculate","(IIIIII)I",null,null);
		
		//compileCodeWithSwitch(pointerLoc, code, calculator);
		compileCodeWithOptim(pointerLoc, code, calculator);
		
		calcImpl.methods.add(constructor);
		calcImpl.methods.add(calculator);
		
		ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		calcImpl.accept(classWriter);
		byte[] classCode = classWriter.toByteArray();
		Path cpuAsmClassFile = Files.createTempFile("aoc_", ".class");
		try (OutputStream os = new FileOutputStream(cpuAsmClassFile.toFile())) {
			os.write(classCode);
			System.out.println(new URI("file:///" + cpuAsmClassFile.toString().replace("\\", "/")));
		}
		Class<?> CpuAsmClass = new DynamicClassLoader().defineClass(calcImpl.name.replace("/", "."), classCode);
		return (Cpu) CpuAsmClass.getDeclaredConstructor().newInstance();
	}
	
	private static void compileCodeWithOptim(int pointerLoc, List<InstructionLine> code, MethodNode calculator) {
		LabelNode switchLabel = new LabelNode();
		LabelNode end = new LabelNode();
		
		InsnList insnCode = new InsnList();
		
		LabelProvider labelProvider = new LabelProvider(code.size()-1, end);
		EntryStream.of(code)
				.mapToValue((i, iline) -> {
					if(AsmGotoInstructionBuilder.doesApplyTo(pointerLoc, iline)) {
						return AsmGotoInstructionBuilder.compileInstruction(pointerLoc, iline, labelProvider);
					}
					if(i > 0 && i < code.size() -1 && AsmIfInstructionBuilder.doesApplyTo(pointerLoc, code.get(i-1), iline)) {
						return AsmIfInstructionBuilder.compileInstruction(pointerLoc, code.get(i-1), iline, labelProvider);
					}
					InsnList codeLine = AsmInstructionBuilder.compileInstruction(pointerLoc, iline);
					if(iline.getWriteIndexes().contains(0)) {
						codeLine.add(AsmInstructionBuilder.println(0));
					}
					if (iline.getWriteIndexes().contains(pointerLoc)) {
						codeLine.add(new IincInsnNode(pointerLoc+1, 1));
						codeLine.add(new JumpInsnNode(GOTO, switchLabel));
					}
					return codeLine;
				}).forKeyValue((i, codeInsns) -> {
			LabelNode ln = labelProvider.getForPosition(i);
			codeInsns.insert(ln);
			insnCode.add(codeInsns);
		});
		
		AbstractInsnNode switchNode = new LookupSwitchInsnNode(
				end,
				labelProvider.getLabelMap().keySet().stream().mapToInt(i -> i).toArray(),
				labelProvider.getLabelMap().values().toArray(new LabelNode[0]));
		
		
		calculator.instructions.add(switchLabel);
		calculator.instructions.add(new IntInsnNode(ILOAD, pointerLoc+1));
		calculator.instructions.add(switchNode);
		calculator.instructions.add(insnCode);
		calculator.instructions.add(end);
		calculator.instructions.add(new IntInsnNode(ILOAD, 1));
		calculator.instructions.add(new InsnNode(IRETURN));
	}
	
	private static void compileCodeWithSwitch(int pointerLoc, List<InstructionLine> code, MethodNode calculator) {
		LabelNode switchLabel = new LabelNode();
		LabelNode end = new LabelNode();
		
		InsnList insnCode = new InsnList();
		
		LabelProvider labelProvider = new LabelProvider(code.size()-1, end);
		StreamEx.of(code)
				.mapToEntry(InstructionLine::getPosition, iline -> {
					InsnList codeLine = AsmInstructionBuilder.compileInstruction(pointerLoc, iline);
					if (iline.getWriteIndexes().contains(pointerLoc)) {
						codeLine.add(new IincInsnNode(pointerLoc+1, 1));
						codeLine.add(new JumpInsnNode(GOTO, switchLabel));
					}
					return codeLine;
				}).forKeyValue((i, codeInsns) -> {
					LabelNode ln = labelProvider.getForPosition(i);
					codeInsns.insert(ln);
					insnCode.add(codeInsns);
				});
		
		AbstractInsnNode switchNode = new LookupSwitchInsnNode(
				end,
				labelProvider.getLabelMap().keySet().stream().mapToInt(i -> i).toArray(),
				labelProvider.getLabelMap().values().toArray(new LabelNode[0]));
		
		
		calculator.instructions.add(switchLabel);
		calculator.instructions.add(new IntInsnNode(ILOAD, pointerLoc+1));
		calculator.instructions.add(switchNode);
		calculator.instructions.add(insnCode);
		calculator.instructions.add(end);
		calculator.instructions.add(new IntInsnNode(ILOAD, 1));
		calculator.instructions.add(new InsnNode(IRETURN));
	}
}
