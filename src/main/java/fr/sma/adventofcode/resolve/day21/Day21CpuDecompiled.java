package fr.sma.adventofcode.resolve.day21;

import fr.sma.adventofcode.resolve.processor.Cpu;

public class Day21CpuDecompiled implements Cpu {
	
	public static final long L_2_POW_16 = 0b10000000000000000;
	
	public long calculate(long[] reg) {
		reg[4] = 123L;
		
		do {
			reg[4] &= 456L;
			reg[4] = reg[4] != 72L ? 0L : 1L;
		} while((int)reg[4] == 0);
		
		reg[4] = 0L;
		
		do {
			reg[1] = reg[4] | L_2_POW_16;
			reg[4] = 16031208L;
			
			while(true) {
				reg[3] = reg[1] & 255L;
				reg[4] += reg[3];
				reg[4] &= 16777215L; // 2^25-1
				reg[4] *= 65899L;
				reg[4] &= 16777215L;
				if (reg[1] < 256L) {
					break;
				}
				
				reg[3] = 0L;
				reg[1] = reg[1] / 256L + 1;
				/*while(true) {
					reg[5] = reg[3] + 1L;
					reg[5] *= 256L;
					if (reg[5] > reg[1]) {
						reg[1] = reg[3];
						break;
					}
					
					++reg[3];
				}*/
			}
		} while(reg[4] != reg[0]);
		
		return reg[0];
	}
}
