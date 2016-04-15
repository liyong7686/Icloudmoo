package com.icloudmoo.business.file.upload.util;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringUtils;

public class MessageTransferDecoder {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String result = decodeString(
                "636d643a75706c6f61640d0a73657373696f6e3a303030303030310d0a756e69743a573030303030300d0a70726f6a6563743a3435360d0a646f63547970653a444652435352540d0a646f63537562547970653a444652435352543030310d0a7365637265744c6576656c3a300d0a617263684c6f636174696f6e3a0d0a61726368446174653a323031332d30342d32340d0a706167654e6f3a310d0a73746f7261676559656172733a300d0a636f64653a0d0a6e616d653a3630305f3830300d0a757365723a0d0a73797374656d3a330d0a747970653a6a70670d0a73697a653a31333239340d0a0d0d0affd8ffe000104a46494600010100000100010000ffe100584578696600004d4d002a000000080002011200030000000100010000876900040000000100000026000000000003a00100030000000100010000a00200040000000100000258a0030004000000010000032000000000ffdb004300110c0d0f0d0b110f0e0f131211141a2b1c1a18181a3526281f2b3e3741403d373c3b454d635445495d4a3b3c5675575d66696f706f43537982786b81636c6f6affdb0043011213131a171a321c1c326a473c476a6a6a6a6a6a6a6a6a6a6a6a6a6a6a6a6a6a6a6a6a6a6a6a6a6a6a6a6a6a6a6a6a6a6a6a6a6a6a6a6a6a6a6a6a6a6a6a6a6affc00011080320025803012200021101031101ffc4001f0000010501010101010100000000000000000102030405060708090a0bffc400b5100002010303020403050504040000017d01020300041105122131410613516107227114328191a1082342b1c11552d1f02433627282090a161718191a25262728292a3435363738393a434445464748494a535455565758595a636465666768696a737475767778797a838485868788898a92939495969798999aa2a3a4a5a6a7a8a9aab2b3b4b5b6b7b8b9bac2c3c4c5c6c7c8c9cad2d3d4d5d6d7d8d9dae1e2e3e4e5e6e7e8e9eaf1f2f3f4f5f6f7f8f9faffc4001f0100030101010101010101010000000000000102030405060708090a0bffc400b51100020102040403040705040400010277000102031104052131061241510761711322328108144291a1b1c109233352f0156272d10a162434e125f11718191a262728292a35363738393a434445464748494a535455565758595a636465666768696a737475767778797a82838485868788898a92939495969798999aa2a3a4a5a6a7a8a9aab2b3b4b5b6b7b8b9bac2c3c4c5c6c7c8c9cad2d3d4d5d6d7d8d9dae2e3e4e5e6e7e8e9eaf2f3f4f5f6f7f8f9faffda000c03010002110311003f006f7e283fce8cfad252205145149f8d03145251450019a638ca91ed4ea46e41c50072d76bb66615055cd4176dc363a66a99a6509451450014514948028a28a0628a0d0296828662929c6929",
                "ISO-8859-1");
        System.out.println(result);

        // 将一次传消息的情况，多余的消息存入到真实的文件内容
        byte[] content = result.getBytes("ISO-8859-1");

    }

    public static String decodeString(String string, String encoding) {
        try {
            byte[] data = hexStr2ByteArray(string);
            return new String(data, encoding);
        }
        catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static byte[] hexStr2ByteArray(String hexString) {
        if (StringUtils.isEmpty(hexString))
            throw new IllegalArgumentException("this hexString must not be empty");

        hexString = hexString.toLowerCase();
        final byte[] byteArray = new byte[hexString.length() / 2];
        int k = 0;
        for (int i = 0; i < byteArray.length; i++) {

            byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 0xff);
            byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 0xff);
            byteArray[i] = (byte) (high << 4 | low);
            k += 2;
        }
        return byteArray;
    }

    public static String bytes2HexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString().toLowerCase();
    }

}
