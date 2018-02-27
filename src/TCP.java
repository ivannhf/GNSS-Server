import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Font;

public class TCP extends JFrame {

	private JPanel contentPane;

	private static ServerSocket serverSocket;
	private static Socket socket;
	private static BufferedReader bufferedReader;
	private static InputStreamReader inputStreamReader;
	private static String message = "";
	private static String path = "";

	private static int bytesRead;
	public static int PORT = 8080;
	public static String fileDest = "C://";
	int current = 0;
	private static JTextField port;
	private static JLabel lblIP;
	private static JButton btnStartServer, btnStopServer;

	public static Boolean started = false;

	static Server server;
	private JLabel lblFileDestination;
	private JTextField fileDestTF;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {					
					TCP frame = new TCP();
					frame.setVisible(true);

					//server = new Server();
					//Server();
					//bkServer ();
					
					System.out.println(InetAddress.getLocalHost().getHostAddress());
					lblIP.setText(InetAddress.getLocalHost().getHostAddress());
					

					btnStartServer.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							// TODO Auto-generated method stub
							//System.out.println(Integer.parseInt(port.getText()));
							if (check()) {
								started = true;
								server = new Server();
								PORT = Integer.parseInt(port.getText());
								port.setEnabled(!started);
								btnStartServer.setVisible(!started);
								btnStopServer.setVisible(started);
								server.start();
							}
						}
					});

					btnStopServer.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							// TODO Auto-generated method stub
							started = false;
							port.setEnabled(!started);
							btnStartServer.setVisible(!started);
							btnStopServer.setVisible(started);
							server.stop();
							server = null;
						}
					});

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * Create the frame.
	 */
	public TCP() {
		setTitle("GNSS Recorder Server");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblServer = new JLabel("Server");
		lblServer.setFont(new Font("Arial", Font.BOLD, 16));
		lblServer.setBounds(10, 10, 55, 25);
		contentPane.add(lblServer);

		port = new JTextField();
		port.setBounds(104, 76, 113, 21);
		contentPane.add(port);
		port.setColumns(10);
		port.setText(PORT + "");

		JLabel lblServerPort = new JLabel("Server Port");
		lblServerPort.setBounds(10, 79, 74, 15);
		contentPane.add(lblServerPort);

		btnStartServer = new JButton("Start Server");
		btnStartServer.setBounds(10, 169, 108, 23);
		contentPane.add(btnStartServer);

		btnStopServer = new JButton("Stop Server");
		btnStopServer.setBounds(10, 169, 108, 23);
		btnStopServer.setVisible(false);
		contentPane.add(btnStopServer);
		
		JLabel lblServerIp = new JLabel("Server IP");
		lblServerIp.setBounds(10, 51, 74, 15);
		contentPane.add(lblServerIp);
		
		lblIP = new JLabel("");
		lblIP.setBounds(104, 51, 120, 15);
		contentPane.add(lblIP);
		
		lblFileDestination = new JLabel("File Destination");
		lblFileDestination.setBounds(10, 107, 108, 15);
		contentPane.add(lblFileDestination);
		
		fileDestTF = new JTextField();
		fileDestTF.setBounds(104, 104, 142, 21);
		contentPane.add(fileDestTF);
		fileDestTF.setColumns(10);
		fileDestTF.setText(fileDest);
		
		JButton btnDirectory = new JButton("Directory");
		btnDirectory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new java.io.File(fileDest));
				fileDestTF.setText(fileDest);
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION)  
				{ 
					fileDest = fileChooser.getSelectedFile().getAbsolutePath();
					fileDestTF.setText(fileDest);
					System.out.println(fileDest);
				} 
			}
		});
		btnDirectory.setBounds(254, 103, 108, 23);
		contentPane.add(btnDirectory);
	}
	
	private static boolean check() {
		String portStr = port.getText();
		char[] portArr = portStr.toCharArray();
		boolean pass = true;
		if (portStr == "") {
			pass = false;
			return pass;
		}
		for (int i = 0; i < portArr.length; i++) {
			if ((portArr[i] < '0') || (portArr[i] > '9')) {
				pass = false;
			}
			if (!pass) break;
		}
		return pass;
	}

	private static void bkServer() {
		try {
			while (true) {
				serverSocket = new ServerSocket(8080);
				System.out.println("Server running at: " + serverSocket.getLocalPort());
				socket = serverSocket.accept();

				BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
				DataInputStream d = new DataInputStream(in);
				String fileName = d.readUTF();
				Files.copy(d, Paths.get("E:\\Windows\\Desktop\\test\\" + fileName));
				System.out.println("file finish " + fileName);

				in.close();
				d.close();
				socket.close();
				serverSocket.close();

				/*
				 * InputStream in = new
				 * BufferedInputStream(socket.getInputStream());
				 * 
				 * OutputStream out = new FileOutputStream
				 * ("E:\\Windows\\Desktop\\test\\test.txt"); byte[] bytes = new
				 * byte[2048];
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
				 * InputStreamReader(socket.getInputStream()); bufferedReader =
				 * new BufferedReader(inputStreamReader); message =
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
}
