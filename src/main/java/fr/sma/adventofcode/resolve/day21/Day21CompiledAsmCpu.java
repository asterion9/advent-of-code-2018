//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package fr.sma.adventofcode.resolve.day21;

import fr.sma.adventofcode.resolve.processor.Cpu;

public class Day21CompiledAsmCpu implements Cpu {
	public Day21CompiledAsmCpu() {
	}
	
	public int calculate(int var1, int var2, int var3, int var4, int var5, int var6) {
		while(true) {
			label138:
			while(true) {
				label136:
				while(true) {
					label134:
					while(true) {
						label142: {
							label143: {
								label144: {
									label127: {
										label126: {
											label125: {
												label159: {
													label160: {
														label119: {
															label118: {
																label117: {
																	switch(var3) {
																		case 0:
																			var5 = 123;
																		case 1:
																		case 4:
																			break label125;
																		case 2:
																			break label127;
																		case 3:
																			break label134;
																		case 5:
																			var5 = 0;
																		case 6:
																		case 30:
																			var2 = var5 | 0;
																		case 7:
																			var5 = -24;
																		case 8:
																		case 27:
																			break;
																		case 9:
																			break label117;
																		case 10:
																			break label118;
																		case 11:
																			break label160;
																		case 12:
																			break label126;
																		case 13:
																			break label144;
																		case 14:
																			break label136;
																		case 15:
																		case 17:
																			var4 = 0;
																			break label119;
																		case 16:
																		case 28:
																			var4 = var5 != var1 ? 0 : 1;
																			break label142;
																		case 18:
																		case 25:
																			break label119;
																		case 19:
																			break label159;
																		case 20:
																			break label143;
																		case 21:
																			break label138;
																		case 22:
																		case 24:
																			++var4;
																			break label119;
																		case 23:
																		case 26:
																			var2 = var4;
																			break;
																		case 29:
																			break label142;
																		default:
																			return var1;
																	}
																	
																	var4 = var2 & -1;
																}
																
																var5 += var4;
															}
															
															var5 &= -1;
															break label160;
														}
														
														var6 = var4 + 1;
														break label159;
													}
													
													var5 *= 107;
													break label126;
												}
												
												var6 *= 0;
												break label143;
											}
											
											var5 &= -56;
											break label127;
										}
										
										var5 &= -1;
										break label144;
									}
									
									var5 = var5 != 72 ? 0 : 1;
									break;
								}
								
								var4 = 0 <= var2 ? 0 : 1;
								break label136;
							}
							
							var6 = var6 <= var2 ? 0 : 1;
							break label138;
						}
						
						var3 = var4 + 29;
						++var3;
					}
					
					var3 = var5 + 3;
					++var3;
				}
				
				var3 = var4 + 14;
				++var3;
			}
			
			var3 = var6 + 21;
			++var3;
		}
	}
}
