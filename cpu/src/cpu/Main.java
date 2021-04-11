package cpu;
import cpu.MicroProcessorUnit;
public class Main {

	
	public static void main(String[] args) {
		
		MicroProcessorUnit MPU = new MicroProcessorUnit();
		ContextSwitcher CS = new ContextSwitcher(0);
		CS.run();
//		MPU.run();
//		System.out.println("메모리를 읽습니다.");
		
		
		}
	}
