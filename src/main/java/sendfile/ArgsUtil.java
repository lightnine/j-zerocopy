package sendfile;

public class ArgsUtil {
    public static String getFileName(String[] args) {
        if (args.length >= 1) {
            return args[0];
        }
        return Common.FNAME;
    }

    public static int getPort(String[] args) {
        if (args.length >= 2) {
            return Integer.parseInt(args[1]);
        }
        return Common.PORT;
    }
    public static int getServerPort(String[] args) {
        if (args.length >= 1) {
            return Integer.parseInt(args[0]);
        }
        return Common.PORT;
    }
}
