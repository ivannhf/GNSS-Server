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
	BufferedInputStream in, bisRaw, bisRinex, bisNmea;
	DataInputStream d, disRaw, disRinex, disNmea;

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

						/*
						 * in = new
						 * BufferedInputStream(socket.getInputStream()); d = new
						 * DataInputStream(in);
						 */

						bisRaw = new BufferedInputStream(socket.getInputStream());
						disRaw = new DataInputStream(bisRaw);

						//bisRinex = new BufferedInputStream(socket.getInputStream());
						//disRinex = new DataInputStream(bisRinex);

						//bisNmea = new BufferedInputStream(socket.getInputStream());
						//disNmea = new DataInputStream(bisNmea);

						String rawName = disRaw.readUTF();
						//String rinexName = disRinex.readUTF();
						//String nmeaName = disNmea.readUTF();

						// String fileName = d.readUTF();
						//if (rawName.compareTo("") != 0)
							Files.copy(disRaw, Paths.get(TCP.fileDest + "\\" + rawName));

						/*if (rinexName.compareTo("") != 0)
							Files.copy(disRinex, Paths.get(TCP.fileDest + "\\" + rinexName));

						if (nmeaName.compareTo("") != 0)
							Files.copy(disNmea, Paths.get(TCP.fileDest + "\\" + nmeaName));*/

						disRaw.close();
						bisRaw.close();
						
						/*disRinex.close();
						bisRinex.close();
						
						disNmea.close();						
						bisNmea.close();*/

						// in.close();
						// d.close();

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
	}
}