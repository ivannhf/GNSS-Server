import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
	ServerSocket serverSocket;
	Socket socket;
	BufferedInputStream in;
	DataInputStream d;

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
				try {
					while (true) {
						serverSocket = new ServerSocket(TCP.PORT);
						System.out.println("Server running at: " + serverSocket.getLocalPort());
						socket = serverSocket.accept();
						System.out.println("Server accept");

						in = new BufferedInputStream(socket.getInputStream());
						System.out.println("buffer stream open");
						d = new DataInputStream(in);
						String fileName = d.readUTF();
						Files.copy(d, Paths.get(TCP.fileDest + fileName));
						System.out.println("file finish " + TCP.fileDest + fileName);
						
						in.close();
						d.close();
						socket.close();
						serverSocket.close();

						//socket.close();
						//serverSocket.close();

						/*if (!TCP.started) {
							d.close();
							in.close();
							socket.close();
							serverSocket.close();
							break;
						}*/

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
			//in.close();
			//d.close();
			if (socket.isClosed() == false) socket.close();
			if (serverSocket.isClosed() == false) serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}