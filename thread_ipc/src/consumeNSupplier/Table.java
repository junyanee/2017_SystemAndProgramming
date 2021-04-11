package consumeNSupplier;

import java.util.concurrent.Semaphore;

public class Table {
	
	enum EState { eReady, eRunning, ePause };
	private EState eState; //공유 resource
	
	private final Semaphore semaphore; //관리자임
	private DQueue<Integer> sharedQ; //공유 resource. int y같은 것을 만든 것이다. 얘가 두개의  thread를 만든다. 부모의 자원은 공유된다.
	
	private Client client;
	private Server server; // resource를 하나씩 빼는 애
	
	public Table() { //main클래스를 따로 만들 것임
		this.sharedQ = new DQueue<Integer>();
		this.eState = EState.eReady;
		this.semaphore = new Semaphore(1); // 크리티컬 섹션에 들어갈 
		//main도 돌아가고 있기 때문에 thread가 최종적으로는3개이다.
		this.client = new Client(); //new하면 돌아가기 시작
		this.server = new Server();

	}

	public void start() {//프로그램이 계속 돈다
		this.sharedQ = new DQueue<Integer>();
		this.eState = EState.eRunning;
		this.client.start(); //supplier thread를 start시키면 run함수가 실행된다. run을 직접 호출하는 것이아니라 start라는 명령을 내리면 run이 실행됨
		this.server.start();
	}
	public void pause() {
		
	}
	
	public void stop() { //프로그램이 서고, 초기화된다. sum=0가 된다. pause는 잠깐 서는 것
		this.eState = EState.eReady;
	}
	
	class Client extends Thread { //이렇게 내부 클래스를 만들어야 sum을 공유할 수 있다.
		//여기에 run함수가 있어야 start를 시키면 자동으로 실행된다. thread를 시키면 자동으로 start되는함수가 run함수이다.
		// thread로서 따로 도는 함수가 run
		
		private void send() {
			try {
				semaphore.acquire();
				sharedQ.enqueue(1); // 집어넣는 중간에는 dequeue를 하면 안됨
				semaphore.release();
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		public void run() { //run함수는 thread이기 때문에 무조건 사용해야 함
			while(eState == EState.eRunning) { 
				///////////////////////////////
				//critical section
				try {	
					this.send();
					Thread.sleep(1000);
				} catch (InterruptedException e){
					semaphore.release(); //에러가 나면 세마포어를 놔주어야함. 아니면 큐는 아무도 못쓰는 상태가됨. 세어드 리소스는 백프로 다 크리티컬 섹션임
					e.printStackTrace();
				}
				}
				///////////////////////////////
//				Thread.sleep(500); //밀리세컨드로는 1초가 1/1000초임.마이크로세컨드는 1/1000000임.sleep은 잠깐 세우는 것
			}
		}
	
	class Server extends Thread {
		private void receive() {
			while(sharedQ.empty());		
			try {
				semaphore.acquire();
				int rValue = sharedQ.dequeue();
				System.out.println("Server.receive-" + rValue);
				semaphore.release();
			} catch (InterruptedException e) {
				semaphore.release();
				e.printStackTrace();
			}
		}
		
		public void run() {
			while(eState == EState.eRunning) {
				///////////////////
				//critical section
				try {
					this.receive();
					Thread.sleep(100); //sleep하면 suspend가 됨
				} catch (InterruptedException e){
					semaphore.release();
					e.printStackTrace();
				}
				}
				///////////////////
			}
		}
	
	private class  DQueue<T>{ // circular queue
		private final static int MAXQSIZE = 20;
		private int data[];
		private int front, rear, size;
		
		public DQueue() {
			this.data = new int[MAXQSIZE];
			this.size = 0;
			this.front = 0;
			this.rear = 0;
		}
		
		public boolean empty() {
			if (this.size > 0)
			return false;
			return true;
		}
		
		public boolean full() {
			if (this.size == MAXQSIZE)
			return false;
			return true;
		}
		
		public void enqueue(int value){ 
			this.data[this.rear++] = value; // ++this.rear는 1에다 집어 넣는 것, 0에다 집어넣고 그다음에 증가시킨 것
//			rear = rear++ % 20; //20의 나머지 계산 = 자동으로 0이됨
			if (rear == MAXQSIZE) {
				rear = 0;	
			}
			this.size ++;
			
		}
		public int dequeue(){
			int value = this.data[this.front++];
			if (front == MAXQSIZE) {
				front = 0;
			}
			this.size --;
			return value;
			
			
		}
		
	}
	}

	
