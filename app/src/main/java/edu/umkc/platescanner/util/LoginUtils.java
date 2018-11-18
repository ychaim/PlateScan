package edu.umkc.platescanner.util;

public static class LoginUtils {

    public static boolean SignIn(String username, String password) {
        String salt = "abcdefghij";
        String passwordHash = HashUsingMD5WithSalt(salt, password);

    }

    private static String HashUsingMD5WithSalt(String salt, String password) {

    }
}
