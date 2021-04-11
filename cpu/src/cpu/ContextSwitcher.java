package cpu;
import java.util.Random;
public class ContextSwitcher extends Thread {
	private int id = -1;
	public ContextSwitcher(int id){
		this.id = id;
	}
	public void run(){
		System.out.println( id + "번 프로그램 동작 중..." );
		Random r = new Random(System.currentTimeMillis());
		try {
			long s = r.nextInt(3000);
			Thread.sleep(s);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println( id + "번 프로그램 동작 종료..." );
	

		System.out.println("프로그램 시작");

		for(int i = 1 ; i < 3 ; i++ ){
			ContextSwitcher cs = new ContextSwitcher(i);
			cs.start(); 
		}

		System.out.println("프로그램 종료");
	}
}