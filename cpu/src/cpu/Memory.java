package cpu;

import java.util.Scanner;

public class Memory {
	
	CPU cpu = new CPU();
	private short memory[];
	private final int size = 50;
	
	public Memory(){
		this.memory = new short[size];
		
		
		
		// �޸𸮴� ������ �ε����� ������
		
	}
	public short fetch(short MAR) throws Exception {
		if (MAR < this.size) {
			return memory[MAR];
		}
		throw new Exception();
	}

	public void store(short MBR, short MAR) throws Exception {
		if (MAR < this.size) {
			memory[MAR] = MBR;
		}
		throw new Exception("Memory Out Of Bounds");
	}
	public void read(Scanner scanner) {
		String address = scanner.next();
	}

}
