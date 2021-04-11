package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main {
	public static void main(String[] args) {
		Socket socket = null;

		BufferedReader bufferedReader = null;
		BufferedReader bufferedReaderFromServer = null;
		PrintWriter printWriter = null;
		try {
			socket = new Socket("127.0.0.1", 1);
			bufferedReader = new BufferedReader(new InputStreamReader(System.in));

			printWriter = new PrintWriter(socket.getOutputStream());

			System.out.print("Client - 보낼 메세지를 입력하시오 : ");
			String str = bufferedReader.readLine();

			printWriter.println(str);
			printWriter.flush();

			bufferedReaderFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			String strFromServer = bufferedReaderFromServer.readLine();

			System.out.println("Server - " + strFromServer + " 라고 Client에게서 받음");

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (bufferedReaderFromServer != null) {
				try {
					bufferedReaderFromServer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (printWriter != null) {
				printWriter.close();
			}
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
}
