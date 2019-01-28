package fr.sma.adventofcode.resolve.day21;

import fr.sma.adventofcode.resolve.processor.Cpu;

public class Day21Cpu implements Cpu {
	
	private final long c6, c7, c10, c11, c12;
	
	public static Day21Cpu build(String input) {
		String[] lines = input.split("\n");
		long c6 = Long.valueOf(lines[7].split(" ")[2]);
		long c7 = Long.valueOf(lines[8].split(" ")[1]);
		long c10 = Long.valueOf(lines[11].split(" ")[2]);
		long c11 = Long.valueOf(lines[12].split(" ")[2]);
		long c12 = Long.valueOf(lines[13].split(" ")[2]);
		return new Day21Cpu(c6, c7, c10, c11, c12);
	}
	
	public Day21Cpu(long c6, long c7, long c10, long c11, long c12) {
		this.c6 = c6;
		this.c7 = c7;
		this.c10 = c10;
		this.c11 = c11;
		this.c12 = c12;
	}
	
	public long nextNumber(long reg4) {
		long reg1 = reg4 | c6;
		reg4 = c7;
		
		while(true) {
			reg4 += reg1 & 255L;
			reg4 &= c10;
			reg4 *= c11;
			reg4 &= c12;
			if (reg1 < 256L) {
				break;
			}
			
			reg1 /= 256L;
		}
		
		return reg4;
	}
	
	// unused, here is the decompiled bytecode produced by the CpuAsmBuilder, with constants injected
	public long calculate(long[] reg) {
		reg[4] = 123L;
		
		do {
			reg[4] &= 456L;
			reg[4] = reg[4] != 72L ? 0L : 1L;
		} while((int)reg[4] == 0);
		
		reg[4] = 0L;
		
		do {
			reg[1] = reg[4] | c6;
			reg[4] = c7;
			
			while(true) {
				reg[3] = reg[1] & 255L;
				reg[4] += reg[3];
				reg[4] &= c10;
				reg[4] *= c11;
				reg[4] &= c12;
				if (reg[1] < 256L) {
					break;
				}
				
				reg[3] = 0L;
				while(true) {
					reg[5] = reg[3] + 1L;
					reg[5] *= 256L;
					if (reg[5] > reg[1]) {
						reg[1] = reg[3];
						break;
					}
					
					++reg[3];
				}
			}
		} while(reg[4] != reg[0]);
		
		return reg[0];
	}
}
