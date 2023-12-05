package sendfile;


import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TraditionalClient {
    private static String fileName;
    private static int port;

    public static void main(String[] args) {
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
            output = new DataOutputStream(socket.getOutputStream());
            long start = System.currentTimeMillis();
            byte[] b = new byte[4096];
            long read = 0;
            long total = 0;
            // read function cause user mode to kernel mode,
            // and DMA engine read file content from disk to kernel buffer
            // then copy kernel buffer to the b array. This cause another context switch
            // then when read return, cause kernel mode to user mode
            // Summary: two context switch, two copy(one cpu copy)
            while ((read = inputStream.read(b)) >= 0) {
                total = total + read;
                System.out.println("total size:" + total);
                // write function cause user mode to kernel mode,
                // and copy data from b array to socket buffer,
                // then DMA engine copy socket buffer to nic(network interface) buffer
                // then when write return, cause kernel mode to user mode,
                // Summary: two context switch, two copy(one cpu copy)
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
