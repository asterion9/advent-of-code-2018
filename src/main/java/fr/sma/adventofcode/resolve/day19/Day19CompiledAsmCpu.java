//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.sma.adventofcode.resolve.day19;

import fr.sma.adventofcode.resolve.processor.Cpu;

public class Day19CompiledAsmCpu implements Cpu {
	public Day19CompiledAsmCpu() {
	}
	
	public int calculate(int var1, int var2, int var3, int var4, int var5, int var6) {
		while(true) {
			label604: {
				label605: {
					label606: {
						label525: {
							boolean var8;
							switch(var2) {
								case 0:
								case 17:
									var4 += 2;
									var4 *= var4;
									break label525;
								case 1:
								case 26:
								case 35:
									var5 = 1;
									var3 = 1;
									break;
								case 2:
								case 15:
									var3 = 1;
								case 3:
								case 11:
									break;
								case 4:
									var8 = var6 == var4;
									if (var8) {
										var1 += var5;
										System.out.println(var1);
									}
									
									++var3;
									var8 = var3 > var4;
									if (var8) {
										++var5;
										var8 = var5 > var4;
										if (var8) {
											return var1;
										}
										
										var3 = 1;
									}
									break;
								case 5:
									if (var6 != 0) {
										var1 += var5;
										System.out.println(var1);
									}
									
									++var3;
									var8 = var3 > var4;
									if (var8) {
										++var5;
										var8 = var5 > var4;
										if (var8) {
											return var1;
										}
										
										var3 = 1;
									}
									break;
								case 6:
								case 8:
									++var3;
									var8 = var3 > var4;
									if (var8) {
										++var5;
										var8 = var5 > var4;
										if (var8) {
											return var1;
										}
										
										var3 = 1;
									}
									break;
								case 7:
									var1 += var5;
									System.out.println(var1);
									++var3;
									var8 = var3 > var4;
									if (var8) {
										++var5;
										var8 = var5 > var4;
										if (var8) {
											return var1;
										}
										
										var3 = 1;
									}
									break;
								case 9:
									var8 = var3 > var4;
									if (var8) {
										++var5;
										var8 = var5 > var4;
										if (var8) {
											return var1;
										}
										
										var3 = 1;
									}
									break;
								case 10:
									if (var6 != 0) {
										++var5;
										var8 = var5 > var4;
										if (var8) {
											return var1;
										}
										
										var3 = 1;
									}
									break;
								case 12:
									++var5;
									var8 = var5 > var4;
									if (var8) {
										return var1;
									}
									
									var3 = 1;
									break;
								case 13:
									var8 = var5 > var4;
									if (var8) {
										return var1;
									}
									
									var3 = 1;
									break;
								case 14:
									if (var6 == 0) {
										var3 = 1;
										break;
									}
									
									return var1;
								case 16:
								default:
									return var1;
								case 18:
									var4 *= var4;
								case 19:
									break label525;
								case 20:
									var4 *= 11;
								case 21:
									break label606;
								case 22:
									var6 *= 22;
								case 23:
									break label605;
								case 24:
									var4 += var6;
								case 25:
									break label604;
								case 27:
									byte var7 = 27;
									var6 = var7 * 28;
									var6 += 29;
									var6 = 30 * var6;
									var6 *= 14;
									var6 *= 32;
									var4 += var6;
									var1 = 0;
									System.out.println(var1);
									var5 = 1;
									var3 = 1;
									break;
								case 28:
									var6 *= 28;
									var6 += 29;
									var6 = 30 * var6;
									var6 *= 14;
									var6 *= 32;
									var4 += var6;
									var1 = 0;
									System.out.println(var1);
									var5 = 1;
									var3 = 1;
									break;
								case 29:
									var6 += 29;
									var6 = 30 * var6;
									var6 *= 14;
									var6 *= 32;
									var4 += var6;
									var1 = 0;
									System.out.println(var1);
									var5 = 1;
									var3 = 1;
									break;
								case 30:
									var6 = 30 * var6;
									var6 *= 14;
									var6 *= 32;
									var4 += var6;
									var1 = 0;
									System.out.println(var1);
									var5 = 1;
									var3 = 1;
									break;
								case 31:
									var6 *= 14;
									var6 *= 32;
									var4 += var6;
									var1 = 0;
									System.out.println(var1);
									var5 = 1;
									var3 = 1;
									break;
								case 32:
									var6 *= 32;
									var4 += var6;
									var1 = 0;
									System.out.println(var1);
									var5 = 1;
									var3 = 1;
									break;
								case 33:
									var4 += var6;
									var1 = 0;
									System.out.println(var1);
									var5 = 1;
									var3 = 1;
									break;
								case 34:
									var1 = 0;
									System.out.println(var1);
									var5 = 1;
									var3 = 1;
							}
							
							while(true) {
								var6 = var5 * var3;
								var8 = var6 == var4;
								if (var8) {
									var1 += var5;
									System.out.println(var1);
								}
								
								++var3;
								var8 = var3 > var4;
								if (var8) {
									++var5;
									var8 = var5 > var4;
									if (var8) {
										return var1;
									}
									
									var3 = 1;
								}
							}
						}
						
						var4 = 19 * var4;
						var4 *= 11;
					}
					
					var6 += 7;
					var6 *= 22;
				}
				
				var6 += 18;
				var4 += var6;
			}
			
			var2 = 25 + var1;
		}
	}
}