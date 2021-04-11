package consumerNSupplier;

public class Main {
	
	static private Table table;
	
	public static void main(String[] args) throws InterruptedException {
		table = new Table();
		table.start();

	}

}
