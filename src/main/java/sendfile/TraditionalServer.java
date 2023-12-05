package sendfile;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TraditionalServer {
    private static int port;
    public static void main(String[] args) {
        //  just one thread
        port = ArgsUtil.getServerPort(args);

        ServerSocket serverSocket;
        DataInputStream input;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server waiting for client on port " + serverSocket.getLocalPort());
            while (true) {
                Socket socket = serverSocket.accept();
                long total = 0;
                System.out.println("New connection accepted " + socket.getInetAddress() + ":" + socket.getPort());
                input = new DataInputStream(socket.getInputStream());
                try {
                    byte[] byteArray = new byte[4096];
                    while (true) {
                        // read function cause user mode to kernel mode
                        // DMA engine copy data from nic buffer to kernel buffer
                        // then CPU copy data from kernel buffer to byteArray,
                        // when read return, cause kernel mode to user mode
                        // Summary: two context switch, two copy(one cpu copy)
                        int nread = input.read(byteArray, 0, 4096);
                        total = total + nread;
                        System.out.println("accepted total size:" + total);
                        if (nread == -1) {
                            System.out.println("finish read");
                            break;
                        }
                    }
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
