package com.joaovitor.tucaprodutosdelimpeza.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothPrinter {

    static final int ALIGN_CENTER = 100;
    static final int ALIGN_RIGHT = 101;
    static final int ALIGN_LEFT = 102;

    private static final byte[] NEW_LINE = {10};
    private static final byte[] ESC_ALIGN_CENTER = new byte[]{0x1b, 'a', 0x01};
    private static final byte[] ESC_ALIGN_RIGHT = new byte[]{0x1b, 'a', 0x02};
    private static final byte[] ESC_ALIGN_LEFT = new byte[]{0x1b, 'a', 0x00};

    private BluetoothDevice printer;
    private BluetoothSocket btSocket = null;
    private OutputStream btOutputStream = null;
    private byte[] arrayOfByte = { 27, 33, 0 };

    BluetoothPrinter(BluetoothDevice printer) {
        this.printer = printer;
    }

    void connectPrinter(final PrinterConnectListener listener) {
        new ConnectTask(new ConnectTask.BtConnectListener() {
            @Override
            public void onConnected(BluetoothSocket socket) {
                btSocket = socket;
                try {
                    btOutputStream = socket.getOutputStream();
                    listener.onConnected();
                } catch (IOException e) {
                    listener.onFailed();
                }
            }

            @Override
            public void onFailed() {
                listener.onFailed();
            }
        }).execute(printer);
    }

    // public boolean isConnected() {
    //    return btSocket != null && btSocket.isConnected();
    //}

    void finish() {
        if (btSocket != null) {
            try {
                btOutputStream.close();
                btSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            btSocket = null;
        }
    }

    void printText(String text) {
        try {
            btOutputStream.write(encodeNonAscii(text).getBytes());
            addNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printUnicode(byte[] data) {
        try {
            btOutputStream.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void printLine() {
        printText("________________________________");
    }

    void printDoubleLine() {
        printText("================================");
    }

    void addNewLine() {
        printUnicode(NEW_LINE);
    }

  /* public int addNewLines(int count) {
        int success = 0;
        for (int i = 0; i < count; i++) {
            if (addNewLine()) success++;
        }
        return success;
    }

     public boolean printImage(Bitmap bitmap) {
        byte[] command = decodeBitmap(bitmap);
        return printUnicode(command);
    }

     public void setLineSpacing(int lineSpacing) {
        byte[] cmd = new byte[]{0x1B, 0x33, (byte) lineSpacing};
        printUnicode(cmd);
    }*/

    void setAlign(int alignType) {
        byte[] d;
        switch (alignType) {
            case ALIGN_CENTER:
                d = ESC_ALIGN_CENTER;
                break;
            case ALIGN_LEFT:
                d = ESC_ALIGN_LEFT;
                break;
            case ALIGN_RIGHT:
                d = ESC_ALIGN_RIGHT;
                break;
            default:
                d = ESC_ALIGN_LEFT;
                break;
        }

        try {
            btOutputStream.write(d);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    void setUnderline(){
        byte[] format = { 27, 33, 0 };
        format[2] = ((byte)(0x80 | arrayOfByte[2]));
        printUnicode(format);
    }

    void setBigSize(){
        byte[] format = { 27, 33, 0 };

        // Width
        format[2] = ((byte) (0x20 | arrayOfByte[2]));
        printUnicode(format);
    }

    void setSmallSize(){
        byte[] format = { 27, 33, 0 };

        // Width
        format[2] = ((byte)(0x1 | arrayOfByte[2]));
        printUnicode(format);
    }

    public void setBold(boolean bold) {
        byte[] cmd = new byte[]{0x1B, 0x45, bold ? (byte) 1 : 0};
        printUnicode(cmd);
    }
    void setNormal() {
        printUnicode(arrayOfByte);
    }

    void feedPaper() {
        addNewLine();
        addNewLine();
        addNewLine();
        addNewLine();
        addNewLine();
    }

    private static class ConnectTask extends AsyncTask<BluetoothDevice, Void, BluetoothSocket> {
        private BtConnectListener listener;

        private ConnectTask(BtConnectListener listener) {
            this.listener = listener;
        }

        @Override
        protected BluetoothSocket doInBackground(BluetoothDevice... bluetoothDevices) {
            BluetoothDevice device = bluetoothDevices[0];
            UUID uuid = device.getUuids()[0].getUuid();
            BluetoothSocket socket = null;
            boolean connected = true;

            try {
                socket = device.createRfcommSocketToServiceRecord(uuid);
            } catch (IOException ignored) {
            }
            try {
                assert socket != null;
                socket.connect();
            } catch (IOException e) {
                FirebaseCrashlytics.getInstance().recordException(e);
                try {
                    socket = (BluetoothSocket) device.getClass().getMethod("createRfcommSocket", int.class)
                            .invoke(device, 1);
                    socket.connect();
                } catch (Exception e2) {
                    connected = false;
                    FirebaseCrashlytics.getInstance().recordException(e);
                }
            }

            return connected ? socket : null;
        }

        @Override
        protected void onPostExecute(BluetoothSocket bluetoothSocket) {
            if (listener != null) {
                if (bluetoothSocket != null) listener.onConnected(bluetoothSocket);
                else listener.onFailed();
            }
        }

        private interface BtConnectListener {
            void onConnected(BluetoothSocket socket);

            void onFailed();
        }
    }

    public interface PrinterConnectListener {
        void onConnected();

        void onFailed();
    }

    private static String encodeNonAscii(String text) {
        return text.replace('á', 'a')
                .replace('ç', 'c')
                .replace('č', 'c')
                .replace('ď', 'd')
                .replace('é', 'e')
                .replace('ě', 'e')
                .replace('í', 'i')
                .replace('ň', 'n')
                .replace('ó', 'o')
                .replace('ř', 'r')
                .replace('š', 's')
                .replace('ť', 't')
                .replace('ú', 'u')
                .replace('ů', 'u')
                .replace('ý', 'y')
                .replace('ž', 'z')
                .replace('Á', 'A')
                .replace('Č', 'C')
                .replace('Ď', 'D')
                .replace('É', 'E')
                .replace('Ě', 'E')
                .replace('Í', 'I')
                .replace('Ň', 'N')
                .replace('Ó', 'O')
                .replace('Ř', 'R')
                .replace('Š', 'S')
                .replace('Ť', 'T')
                .replace('Ú', 'U')
                .replace('Ů', 'U')
                .replace('Ý', 'Y')
                .replace('Ž', 'Z')
                .replace('ã','a')
                .replace('à','a')
                .replace('â','a')
                .replace('õ','o')
                .replace('ò','o')
                .replace('ô','o')
                .replace('ê','e')
                .replace('è','e')
                .replace('ì','i')
                .replace('Ã','A')
                .replace('À','A')
                .replace('Â','A')
                .replace('Ç','C')
                .replace('Õ','O')
                .replace('Ò','O')
                .replace('Ô','O')
                .replace('Ê','E')
                .replace('È','E')
                .replace('Ì','I')
                .replace("°","")
                ;
    }

    /*public static byte[] decodeBitmap(Bitmap bmp) {
        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();

        List<String> list = new ArrayList<>();
        StringBuffer sb;
        int zeroCount = bmpWidth % 8;
        String zeroStr = "";
        if (zeroCount > 0) {
            for (int i = 0; i < (8 - zeroCount); i++) zeroStr = zeroStr + "0";
        }

        for (int i = 0; i < bmpHeight; i++) {
            sb = new StringBuffer();
            for (int j = 0; j < bmpWidth; j++) {
                int color = bmp.getPixel(j, i);
                int r = (color >> 16) & 0xff;
                int g = (color >> 8) & 0xff;
                int b = color & 0xff;
                if (r > 160 && g > 160 && b > 160) sb.append("0");
                else sb.append("1");
            }
            if (zeroCount > 0) sb.append(zeroStr);
            list.save(sb.toString());
        }

        List<String> bmpHexList = binaryListToHexStringList(list);
        String commandHexString = "1D763000";
        String widthHexString = Integer
                .toHexString(bmpWidth % 8 == 0 ? bmpWidth / 8 : (bmpWidth / 8 + 1));
        if (widthHexString.length() > 2) {
            return null;
        } else if (widthHexString.length() == 1) {
            widthHexString = "0" + widthHexString;
        }
        widthHexString = widthHexString + "00";

        String heightHexString = Integer.toHexString(bmpHeight);
        if (heightHexString.length() > 2) {
            return null;
        } else if (heightHexString.length() == 1) {
            heightHexString = "0" + heightHexString;
        }
        heightHexString = heightHexString + "00";

        List<String> commandList = new ArrayList<>();
        commandList.save(commandHexString + widthHexString + heightHexString);
        commandList.addAll(bmpHexList);

        return hexList2Byte(commandList);
    }

    private static List<String> binaryListToHexStringList(List<String> list) {
        List<String> hexList = new ArrayList<>();
        for (String binaryStr : list) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < binaryStr.length(); i += 8) {
                String str = binaryStr.substring(i, i + 8);
                String hexString = strToHexString(str);
                sb.append(hexString);
            }
            hexList.save(sb.toString());
        }
        return hexList;
    }

    private static String[] binaryArray = {"0000", "0001", "0010", "0011",
            "0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011",
            "1100", "1101", "1110", "1111"};

    private static String strToHexString(String binaryStr) {
        StringBuilder hex = new StringBuilder();
        String f4 = binaryStr.substring(0, 4);
        String b4 = binaryStr.substring(4, 8);
        String hexStr = "0123456789ABCDEF";
        for (int i = 0; i < binaryArray.length; i++) {
            if (f4.equals(binaryArray[i]))
                hex.append(hexStr.substring(i, i + 1));
        }
        for (int i = 0; i < binaryArray.length; i++) {
            if (b4.equals(binaryArray[i]))
                hex.append(hexStr.substring(i, i + 1));
        }

        return hex.toString();
    }

    private static byte[] hexList2Byte(List<String> list) {
        List<byte[]> commandList = new ArrayList<>();
        for (String hexStr : list) commandList.save(hexStringToBytes(hexStr));
        return sysCopy(commandList);
    }

    private static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) return null;
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte[] sysCopy(List<byte[]> srcArrays) {
        int len = 0;
        for (byte[] srcArray : srcArrays) {
            len += srcArray.length;
        }
        byte[] destArray = new byte[len];
        int destLen = 0;
        for (byte[] srcArray : srcArrays) {
            System.arraycopy(srcArray, 0, destArray, destLen, srcArray.length);
            destLen += srcArray.length;
        }

        return destArray;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public BluetoothSocket getSocket() {
        return btSocket;
    }

    public BluetoothDevice getDevice() {
        return printer;
    }
    */

}

