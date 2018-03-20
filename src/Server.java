import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
	ServerSocket serverSocket;
	Socket socket;
	BufferedInputStream in, bisRaw, bisRinex, bisNmea;
	DataInputStream d, disRaw, disRinex, disNmea;
	InputStreamReader inputStreamReader;
	BufferedReader bufferedReader;
	String message = "";
	
	File input;
	BufferedWriter bufferedWriter;
	

	public Server() {
		try {
			socket = new Socket();
			socket.setReuseAddress(true);
			serverSocket = new ServerSocket();
			serverSocket.setReuseAddress(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void start() {

		Runnable r = new Runnable() {
			public void run() {
				input = new File (TCP.fileDest, "test.txt");
				try {
					bufferedWriter = new BufferedWriter(new FileWriter(input));
	            } catch (IOException e) {
	            }
				
				try {
					while (true) {
						serverSocket = new ServerSocket(TCP.PORT);
						serverSocket.setReuseAddress(true);
						System.out.println("Server running at: " + serverSocket.getLocalPort());
						socket = serverSocket.accept();
						socket.setReuseAddress(true);
						System.out.println("Server accept");

						inputStreamReader = new InputStreamReader(socket.getInputStream());
						bufferedReader = new BufferedReader(inputStreamReader); 
						message = bufferedReader.readLine();
						bufferedWriter.write(message);

						System.out.println(message);
							
						inputStreamReader.close(); 
						bufferedReader.close();

						socket.close();
						serverSocket.close();

						// socket.close();
						// serverSocket.close();

						/*
						 * if (!TCP.started) { d.close(); in.close();
						 * socket.close(); serverSocket.close(); break; }
						 */

						/*
						 * InputStream in = new
						 * BufferedInputStream(socket.getInputStream());
						 * 
						 * OutputStream out = new FileOutputStream
						 * ("E:\\Windows\\Desktop\\test\\test.txt"); byte[]
						 * bytes = new byte[2048];
						 * 
						 * int count; while ((count = in.read(bytes)) > 0) {
						 * out.write(bytes, 0, count); }
						 * System.out.println("jpg finish");
						 * 
						 * out.close(); in.close(); socket.close();
						 * serverSocket.close();
						 */

						/*
						 * inputStreamReader = new
						 * InputStreamReader(socket.getInputStream());
						 * bufferedReader = new
						 * BufferedReader(inputStreamReader); message =
						 * bufferedReader.readLine();
						 * 
						 * System.out.println(message);
						 * 
						 * inputStreamReader.close(); bufferedReader.close();
						 * serverSocket.close(); socket.close();
						 */
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};

		new Thread(r).start();
	}

	public void stop() {
		try {
			// in.close();
			// d.close();
			if (socket.isClosed() == false)
				socket.close();
			if (serverSocket.isClosed() == false)
				serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (bufferedWriter != null) {
            try {
            	bufferedWriter.flush();
            	bufferedWriter.close();
            	bufferedWriter = null;
            } catch (IOException e) {
            }
        }
		
	}
}