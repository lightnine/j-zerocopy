package sendfile;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class TransferToServer {
    private static int port;
    ServerSocketChannel listener = null;
    protected ServerSocket mySetup() {
        InetSocketAddress listenAddr = new InetSocketAddress(port);
        try {
            listener = ServerSocketChannel.open();
            ServerSocket ss = listener.socket();
            ss.setReuseAddress(true);
            ss.bind(listenAddr);
            System.out.println("Listening on port: " + listenAddr.toString());
            return ss;
        } catch (IOException e) {
            System.out.println("Failed to bind ,is port: " + listenAddr.toString() + " already in use? Error msg: "
                    + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        port = ArgsUtil.getServerPort(args);
        TransferToServer dns = new TransferToServer();
        ServerSocket ss = dns.mySetup();
        dns.readData();
//        ss.close();
    }

    private void readData() {
        ByteBuffer dst = ByteBuffer.allocate(4096);
        try {
            while (true) {
                SocketChannel conn = listener.accept();
                System.out.println("Accepted: " + conn);
                conn.configureBlocking(true);
                int nread = 0;
                int total = 0;
                while (nread != -1) {
                    try {
                        nread = conn.read(dst);
                        total = total + nread;
                        System.out.println("total: " + total);
                    } catch (IOException e) {
                        e.printStackTrace();
                        nread = -1;
                    }
                    dst.rewind();
                }
                conn.close();
                break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
