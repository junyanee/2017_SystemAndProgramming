package cpu;

import java.io.File;
import java.util.*;

public class CPU {
	// registers
	private short PC;
	private InstructionRegister IR;
	private short AC;
	private short MAR;
	private short MBR;
//	private FlagRegister FR;
	// association
	private Memory memory;

	public CPU() {
		this.IR = new InstructionRegister();
	}

	public void connect(Memory memory) {
		this.memory = memory;
	}

	public void LDA(short address) throws Exception {
		MAR = address;
		MBR = memory.fetch(MAR);
		AC = MBR;
	}

	public void STA(short address) throws Exception {
		MAR = address;
		MBR = AC;
		memory.store(MAR, MBR);
	}

	public void ADD(short address) throws Exception {
		MAR = address;
		MBR = memory.fetch(MAR);
		AC = (short) (AC + MBR);
	}

	public void AND(short address) throws Exception {
		MAR = address;
		MBR = memory.fetch(MAR);
		AC = (short) (AC & MBR);
	}

	public void JMP(short address) {
		PC = address;
	}
//	public void BZ(int address) {
//		if (FR.getZERO() == 0){
//			PC = address;
//		}
//	}
	// public void NOT() {}
	// public void SHR() {}

	public void fetch() throws Exception {
		Scanner sc = new Scanner(new File("Harddisk/program.txt"));
		while (sc.hasNext()) {
			Memory memory = new Memory();
			memory.read(sc);
			System.out.println(sc);
		}
		System.out.println();
		MAR = PC;
		MBR = memory.fetch(MAR);
		IR.set(MBR);
		PC++;
	}
	
	public void decode() throws Exception {
		switch (IR.getOpcode()) {
		case 0x0:
			break;
		case 0x1:
			LDA(IR.getOperand());
			break;
		case 0x2:
			STA(IR.getOperand());
			break;
		case 0x3:
			ADD(IR.getOperand());
			break;
		}
	}

	public void execute() {
	}

	private class InstructionRegister {
		short instruction;

		// ������ 4����Ʈ �ް� �ٶ� 2����Ʈ�� ��
		public void set(short instruction) {
			this.instruction = instruction;
		}

		// �ڿ� OPERAND ���ֱ� ���ؼ� 16���� 0�� 4������
		public short getOpcode() {

			// return instruction / 0x10000;
			return (short) (instruction >> 8);
		}

		// �տ� OPCODE 4�ڸ� ���
		public short getOperand() {

			return (short) (instruction & 0x0011);
		}
		private class FlagRegister {
			short flags;
			
			public short getZERO() {
				return (short) (flags & 0x0001);
			}
		}

	}

	// int x, y;
	// x = 2;
	// y = 3;
	// x = x + y;
	// if(x>0)
	// y=1;

	// 1. �� ���α׷��� ����� �ٲ�
	// 2. cpu�� �޸����α׷� �ۼ�
	// 3. ��� �޸𸮿� ���� ����
}
