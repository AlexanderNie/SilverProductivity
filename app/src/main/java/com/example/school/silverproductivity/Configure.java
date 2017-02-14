package com.example.school.silverproductivity;


public class Configure {
    // public static String server = "https://trainingpoint.000webhostapp.com";
    public static String server = "http://10.8.88.202";
    //public static String server = "http://172.16.100.227";
    //public static String server = "http://192.168.1.105";
    static byte[] salt = { (byte)0xc7, (byte)0x73, (byte)0x21,
            (byte)0x8c, (byte)0x7e, (byte)0xc8, (byte)0xee, (byte)0x99 };
    static int iterations = 2048;
    static String password = "SsXrNsX3KF2jzmirDxrZzTlY9pkSeQyaOFDNtvmq9gU23xoATvvr4hRZca6PxLvbBJFWgwTDYzsMogRlopmj6anNS7azCpN8x5xo";

    // File upload url (replace the ip with your server address)
    public static final String FILE_UPLOAD_URL = server+"/silverproductivity/uploadImages.php";

    // Directory name to store captured images and videos
    public static final String IMAGE_DIRECTORY_NAME = "Android File Upload";
}
