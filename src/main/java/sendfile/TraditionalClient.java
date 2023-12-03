package sendfile;


import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TraditionalClient {
    private static String fileName;
    private static int port;

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("number of params: " + args.length);
        fileName = ArgsUtil.getFileName(args);
        port = ArgsUtil.getPort(args);
        Socket socket = null;
        DataOutputStream output = null;
        InputStream inputStream = null;

        // 1. create socket and connect to server
        try {
            socket = new Socket(Common.SERVER, port);
            System.out.println("Connected with server " + socket.getInetAddress() + ":" + socket.getPort());
        } catch (UnknownHostException e) {
            System.out.println(e);
            System.exit(Common.ERROR);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(Common.ERROR);
        }

        // 2. send data to server
        try {
            inputStream = Files.newInputStream(Paths.get(fileName));
//            inputStream =  TraditionalClient.class.getResourceAsStream(fileName);
            output = new DataOutputStream(socket.getOutputStream());
            long start = System.currentTimeMillis();
            byte[] b = new byte[4096];
            long read = 0;
            long total = 0;
            while ((read = inputStream.read(b)) >= 0) {
                total = total + read;
                System.out.println("total size:" + total);
                output.write(b);
            }
            System.out.println("bytes send: " + total + " and totalTime(ms):" + (System.currentTimeMillis() - start));
        } catch (IOException e) {
            System.out.println(e);
        }

        // 3. close connection
        try {
            output.close();
            socket.close();
            inputStream.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
