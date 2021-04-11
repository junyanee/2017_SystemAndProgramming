package consumeNSupplier;

import java.util.concurrent.Semaphore;

public class Table {
	
	enum EState { eReady, eRunning, ePause };
	private EState eState; //���� resource
	
	private final Semaphore semaphore; //��������
	private DQueue<Integer> sharedQ; //���� resource. int y���� ���� ���� ���̴�. �갡 �ΰ���  thread�� �����. �θ��� �ڿ��� �����ȴ�.
	
	private Client client;
	private Server server; // resource�� �ϳ��� ���� ��
	
	public Table() { //mainŬ������ ���� ���� ����
		this.sharedQ = new DQueue<Integer>();
		this.eState = EState.eReady;
		this.semaphore = new Semaphore(1); // ũ��Ƽ�� ���ǿ� �� 
		//main�� ���ư��� �ֱ� ������ thread�� ���������δ�3���̴�.
		this.client = new Client(); //new�ϸ� ���ư��� ����
		this.server = new Server();

	}

	public void start() {//���α׷��� ��� ����
		this.sharedQ = new DQueue<Integer>();
		this.eState = EState.eRunning;
		this.client.start(); //supplier thread�� start��Ű�� run�Լ��� ����ȴ�. run�� ���� ȣ���ϴ� ���̾ƴ϶� start��� ����� ������ run�� �����
		this.server.start();
	}
	public void pause() {
		
	}
	
	public void stop() { //���α׷��� ����, �ʱ�ȭ�ȴ�. sum=0�� �ȴ�. pause�� ��� ���� ��
		this.eState = EState.eReady;
	}
	
	class Client extends Thread { //�̷��� ���� Ŭ������ ������ sum�� ������ �� �ִ�.
		//���⿡ run�Լ��� �־�� start�� ��Ű�� �ڵ����� ����ȴ�. thread�� ��Ű�� �ڵ����� start�Ǵ��Լ��� run�Լ��̴�.
		// thread�μ� ���� ���� �Լ��� run
		
		private void send() {
			try {
				semaphore.acquire();
				sharedQ.enqueue(1); // ����ִ� �߰����� dequeue�� �ϸ� �ȵ�
				semaphore.release();
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		public void run() { //run�Լ��� thread�̱� ������ ������ ����ؾ� ��
			while(eState == EState.eRunning) { 
				///////////////////////////////
				//critical section
				try {	
					this.send();
					Thread.sleep(1000);
				} catch (InterruptedException e){
					semaphore.release(); //������ ���� ������� ���־����. �ƴϸ� ť�� �ƹ��� ������ ���°���. ����� ���ҽ��� ������ �� ũ��Ƽ�� ������
					e.printStackTrace();
				}
				}
				///////////////////////////////
//				Thread.sleep(500); //�и�������δ� 1�ʰ� 1/1000����.����ũ�μ������ 1/1000000��.sleep�� ��� ����� ��
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
					Thread.sleep(100); //sleep�ϸ� suspend�� ��
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
			this.data[this.rear++] = value; // ++this.rear�� 1���� ���� �ִ� ��, 0���� ����ְ� �״����� ������Ų ��
//			rear = rear++ % 20; //20�� ������ ��� = �ڵ����� 0�̵�
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

	
