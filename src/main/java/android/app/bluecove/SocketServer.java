package android.app.bluecove;

import java.io.DataInputStream;
import java.io.IOException;
import javax.bluetooth.*;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

public class SocketServer {
        private final LocalDevice mLocalDevice;
    public static void main(String[] args) throws IOException, InterruptedException {
        new SocketServer().start();
    }

    public SocketServer() throws IOException {
        mLocalDevice = LocalDevice.getLocalDevice();
    }

    public void start() throws IOException {
        StreamConnectionNotifier connectionNotifier =
            (StreamConnectionNotifier) Connector.open("btspp://localhost:" +
            "0000000000000000000000000000ABCD;name=BtExample;" +
            "authenticate=false;encrypt=false;master=false");
        System.out.println("accepting on " + mLocalDevice.getBluetoothAddress());
        StreamConnection streamConnection = connectionNotifier.acceptAndOpen();
        DataInputStream is = streamConnection.openDataInputStream();

        byte[] bytes = new byte[1024];
        int r;
        while ((r = is.read(bytes)) > 0) {
                System.out.println(new String(bytes, 0, r));
        }
    }

}