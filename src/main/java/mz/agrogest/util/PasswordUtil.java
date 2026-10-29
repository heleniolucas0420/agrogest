package mz.agrogest.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Hash de palavras-passe com SHA-256 + "sal" aleatorio.
 * Formato guardado: sal:hash (ambos em Base64). Nunca guardamos a password em texto.
 */
public final class PasswordUtil {

    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordUtil() {
    }

    public static String gerarHash(String password) {
        byte[] sal = new byte[16];
        RANDOM.nextBytes(sal);
        return Base64.getEncoder().encodeToString(sal) + ":" + sha256(sal, password);
    }

    public static boolean verificar(String password, String guardado) {
        if (password == null || guardado == null || !guardado.contains(":")) {
            return false;
        }
        String[] partes = guardado.split(":", 2);
        byte[] sal = Base64.getDecoder().decode(partes[0]);
        String calculado = sha256(sal, password);
        // comparacao em tempo constante
        return MessageDigest.isEqual(calculado.getBytes(StandardCharsets.UTF_8),
                partes[1].getBytes(StandardCharsets.UTF_8));
    }

    private static String sha256(byte[] sal, String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(sal);
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 indisponivel", e);
        }
    }
}
