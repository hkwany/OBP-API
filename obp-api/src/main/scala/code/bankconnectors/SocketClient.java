package code.bankconnectors;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.HashMap;

public class SocketClient {
  public static void sendOTP(String phoneNumber, String OTP) throws Exception {
    // SocketServer IP and Port
    String host = "127.0.0.1";
    int port = 12345;
    // Try to establish a connection to the SocketServer
    Socket socket = new Socket(host, port);
    // Acquire output stream when the connection is established
    // OutputStream outputStream = socket.getOutputStream();
    ObjectOutputStream mapOutputStream = new ObjectOutputStream(socket.getOutputStream());

    Map<String, String> map = new HashMap<String, String>();
    map.put("phoneNumber", phoneNumber);
    map.put("OTP", OTP);
//    System.out.println(map);
//    System.out.println(map.get("phoneNumber"));
//    System.out.println(map.get("OTP"));
    mapOutputStream.writeObject(map);
    // String message="phoneNumber="+phoneNumber+"\n"+"OTP="+OTP+"\n";
    // socket.getOutputStream().write(map.getBytes("UTF-8"));
    mapOutputStream.close();
    socket.close();
  }
}
