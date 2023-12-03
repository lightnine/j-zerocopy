package sendfile;


import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TransferToClient {
    private static String fileName;
    private static int port;
    public static void main(String[] args) throws IOException {
        System.out.println("number of args: " + args.length);
        fileName = ArgsUtil.getFileName(args);
        port = ArgsUtil.getPort(args);
        TransferToClient sfc = new TransferToClient();
        sfc.testSendfile();
    }

    public void testSendfile() throws IOException  {
        // 1. get file size(bytes)
        Path path = Paths.get(fileName);
        long fsize = Files.size(path);

        SocketAddress sad = new InetSocketAddress(Common.SERVER, port);
        SocketChannel sc = SocketChannel.open();
        sc.connect(sad);
        sc.configureBlocking(true);
//        long fsize = 11983720;
        FileInputStream fis = new FileInputStream(fileName);
        FileChannel fc = fis.getChannel();
        long start = System.currentTimeMillis();
        long curnset = 0;

        curnset = fc.transferTo(0, fsize, sc);
        System.out.println("total bytes transferred: " + curnset + " and time taken in MS: " +
                (System.currentTimeMillis() - start) );

        fc.close();
        fis.close();
    }
}
