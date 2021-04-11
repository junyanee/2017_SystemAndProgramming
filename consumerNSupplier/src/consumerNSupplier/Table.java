package consumerNSupplier;

public class Table {
	enum EState {
		eReady, eRunning, ePause
	}

	private int sum; // ���� resource. int y���� ���� ���� ���̴�. �갡 �ΰ��� thread�� �����. �θ���
						// �ڿ��� �����ȴ�.
	private EState eState; // ���� resource

	private Supplier supplier;
	private Consumer consumer; // resource�� �ϳ��� ���� ��

	public Table() { // mainŬ������ ���� ���� ����
		this.eState = EState.eReady;

		// main�� ���ư��� �ֱ� ������ thread�� ���������δ�3���̴�.
		this.supplier = new Supplier(); // new�ϸ� ���ư��� ����
		this.consumer = new Consumer();

	}

	public void start() throws InterruptedException {// ���α׷��� ��� ����
		
		this.eState = EState.eRunning;
		this.supplier.start(); // supplier thread�� start��Ű�� run�Լ��� ����ȴ�. run�� ����
								// ȣ���ϴ� ���̾ƴ϶� start��� ����� ������ run�� �����
		this.consumer.start();
	}

	public void pause() {

	}

	public void stop() { // ���α׷��� ����, �ʱ�ȭ�ȴ�. sum=0�� �ȴ�. pause�� ��� ���� ��
		this.eState = EState.eReady;
	}

	class Supplier extends Thread { // �̷��� ���� Ŭ������ ������ sum�� ������ �� �ִ�.
		// ���⿡ run�Լ��� �־�� start�� ��Ű�� �ڵ����� ����ȴ�. thread�� ��Ű�� �ڵ����� start�Ǵ��Լ���
		// run�Լ��̴�.
		// thread�μ� ���� ���� �Լ��� run
		public void run() { // run�Լ��� thread�̱� ������ ������ ����ؾ� ��
			// while(true)�ϸ� ���Ѵ�� ����. ������ bRunning�� true�� start, false�� stop
			while (eState == EState.eRunning) {
				/////////////////////////////////////
				// critical section
				synchronized(this) {
					for (int i = 0; i < 5; i++) {
						sum = sum + 1; // sum�� �����޸�
						System.out.println("+Supplier(" + i + "): sum = " + sum);
					}}

					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				///////////////////////////////

				// Thread.sleep(500); //�и�������δ� 1�ʰ� 1/1000����.����ũ�μ������
				// 1/1000000��.sleep�� ��� ����� ��
			}
		}
	

	class Consumer extends Thread { // supplier�� ���ϱ�, consumer�� ����
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
						Thread.sleep(10); // sleep�ϸ� suspend�� ��
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				///////////////////
			}

		
	}

}
