package consumerNSupplier;

public class Table {
	enum EState {
		eReady, eRunning, ePause
	}

	private int sum; // 공유 resource. int y같은 것을 만든 것이다. 얘가 두개의 thread를 만든다. 부모의
						// 자원은 공유된다.
	private EState eState; // 공유 resource

	private Supplier supplier;
	private Consumer consumer; // resource를 하나씩 빼는 애

	public Table() { // main클래스를 따로 만들 것임
		this.eState = EState.eReady;

		// main도 돌아가고 있기 때문에 thread가 최종적으로는3개이다.
		this.supplier = new Supplier(); // new하면 돌아가기 시작
		this.consumer = new Consumer();

	}

	public void start() throws InterruptedException {// 프로그램이 계속 돈다
		
		this.eState = EState.eRunning;
		this.supplier.start(); // supplier thread를 start시키면 run함수가 실행된다. run을 직접
								// 호출하는 것이아니라 start라는 명령을 내리면 run이 실행됨
		this.consumer.start();
	}

	public void pause() {

	}

	public void stop() { // 프로그램이 서고, 초기화된다. sum=0가 된다. pause는 잠깐 서는 것
		this.eState = EState.eReady;
	}

	class Supplier extends Thread { // 이렇게 내부 클래스를 만들어야 sum을 공유할 수 있다.
		// 여기에 run함수가 있어야 start를 시키면 자동으로 실행된다. thread를 시키면 자동으로 start되는함수가
		// run함수이다.
		// thread로서 따로 도는 함수가 run
		public void run() { // run함수는 thread이기 때문에 무조건 사용해야 함
			// while(true)하면 무한대로 돈다. 지금은 bRunning이 true면 start, false면 stop
			while (eState == EState.eRunning) {
				/////////////////////////////////////
				// critical section
				synchronized(this) {
					for (int i = 0; i < 5; i++) {
						sum = sum + 1; // sum은 공유메모리
						System.out.println("+Supplier(" + i + "): sum = " + sum);
					}}

					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				///////////////////////////////

				// Thread.sleep(500); //밀리세컨드로는 1초가 1/1000초임.마이크로세컨드는
				// 1/1000000임.sleep은 잠깐 세우는 것
			}
		}
	

	class Consumer extends Thread { // supplier는 더하기, consumer은 빼기
		public void run() {
			while (eState == EState.eRunning) {
				///////////////
				// critical section
				synchronized(this) {
					for (int i = 0; i < 5; i++) {
						sum = sum - 1;
						System.out.println("-Consumer(" + i + "): sum = " + sum);
					}}

					try {
						Thread.sleep(10); // sleep하면 suspend가 됨
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				///////////////////
			}

		
	}

}
