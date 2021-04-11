package cpu;

public class MicroProcessorUnit {
	private CPU cpu;
	private Memory memory;
	
	public MicroProcessorUnit() {
		this.cpu = new CPU();
		this.memory = new Memory();
		this.cpu.connect(this.memory);
		}
	
	public void run() {		
		while(true) {
			// fetch the next instruction
			try {
				this.cpu.fetch();
				this.cpu.decode();
				this.cpu.execute();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	

}
