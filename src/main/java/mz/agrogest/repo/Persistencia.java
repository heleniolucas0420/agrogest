package mz.agrogest.repo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.logging.Level;
import java.util.logging.Logger;

import mz.agrogest.util.Formatos;

/**
 * Grava e le o estado do sistema num ficheiro (ObjectOutputStream / ObjectInputStream).
 *
 * Tolerancia a falhas:
 *  1. Escreve primeiro num ficheiro temporario (.tmp);
 *  2. Guarda a versao anterior como copia de seguranca (.bak);
 *  3. So depois troca o .tmp pelo ficheiro final (movimento atomico).
 * Se o ficheiro principal estiver corrompido, tenta ler o .bak.
 */
public final class Persistencia {

    private static final Logger LOG = Logger.getLogger(Persistencia.class.getName());
    private static final String NOME_FICHEIRO = "agrogest.ser";

    /**
     * Depois de "Eliminar ficheiro" o encerramento do Tomcat NAO volta a gravar,
     * para que o arranque seguinte comece com os dados de exemplo.
     * Um "Gravar agora" volta a activar a gravacao automatica.
     */
    private static volatile boolean gravarAoEncerrar = true;

    private Persistencia() {
    }

    public static boolean isGravarAoEncerrar() {
        return gravarAoEncerrar;
    }

    /**
     * Pasta dos dados: -Dagrogest.dados=... se existir; senao CATALINA_BASE/agrogest-dados;
     * senao a pasta pessoal do utilizador.
     */
    public static Path ficheiro() {
        String pasta = System.getProperty("agrogest.dados");
        if (pasta == null || pasta.isBlank()) {
            String base = System.getProperty("catalina.base");
            if (base == null) {
                base = System.getProperty("user.home");
            }
            pasta = Paths.get(base, "agrogest-dados").toString();
        }
        return Paths.get(pasta, NOME_FICHEIRO);
    }

    private static Path backup() {
        Path f = ficheiro();
        return f.resolveSibling(NOME_FICHEIRO + ".bak");
    }

    public static synchronized void gravar() throws IOException {
        Snapshot s = DataStore.get().criarSnapshot();
        Path destino = ficheiro();
        Files.createDirectories(destino.getParent());
        Path tmp = destino.resolveSibling(NOME_FICHEIRO + ".tmp");

        try (ObjectOutputStream out = new ObjectOutputStream(
                new BufferedOutputStream(Files.newOutputStream(tmp)))) {
            out.writeObject(s);
        }

        if (Files.exists(destino)) {
            Files.copy(destino, backup(), StandardCopyOption.REPLACE_EXISTING);
        }
        try {
            Files.move(tmp, destino, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(tmp, destino, StandardCopyOption.REPLACE_EXISTING);
        }
        gravarAoEncerrar = true;
        LOG.info("AgroGest: estado gravado em " + destino);
    }

    /**
     * Gravacao automatica depois de cada operacao que altera dados (vendas, registos, gestao).
     * Assim, se o Tomcat for parado a forca, perde-se no maximo a ultima operacao.
     * Nao lanca excepcao: uma falha de disco nao deve cancelar a operacao do utilizador.
     * Depois de "Eliminar ficheiro" nao grava, para o proximo arranque voltar aos dados de exemplo.
     */
    public static void gravarSilencioso() {
        if (!gravarAoEncerrar) {
            return;
        }
        try {
            gravar();
        } catch (IOException e) {
            LOG.log(Level.WARNING, "AgroGest: falha na gravacao automatica", e);
        }
    }

    /** @return true se carregou dados do ficheiro (ou do backup). */
    public static synchronized boolean carregar() {
        if (lerDe(ficheiro())) {
            return true;
        }
        if (Files.exists(ficheiro())) {
            LOG.warning("AgroGest: ficheiro principal ilegivel, a tentar a copia .bak");
        }
        return lerDe(backup());
    }

    private static boolean lerDe(Path f) {
        if (!Files.exists(f)) {
            return false;
        }
        try (ObjectInputStream in = new ObjectInputStream(
                new BufferedInputStream(Files.newInputStream(f)))) {
            Snapshot s = (Snapshot) in.readObject();
            DataStore.get().restaurar(s);
            LOG.info("AgroGest: estado restaurado de " + f);
            return true;
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            LOG.log(Level.WARNING, "AgroGest: nao foi possivel ler " + f, e);
            return false;
        }
    }

    /** Apaga o ficheiro gravado (e o backup). Os dados em memoria continuam. */
    public static synchronized boolean eliminar() throws IOException {
        gravarAoEncerrar = false;
        boolean apagou = Files.deleteIfExists(ficheiro());
        Files.deleteIfExists(backup());
        return apagou;
    }

    public static boolean existe() {
        return Files.exists(ficheiro());
    }

    public static long tamanhoBytes() {
        try {
            return existe() ? Files.size(ficheiro()) : 0;
        } catch (IOException e) {
            return 0;
        }
    }

    public static String ultimaGravacao() {
        try {
            if (!existe()) {
                return "-";
            }
            LocalDateTime t = LocalDateTime.ofInstant(
                    Files.getLastModifiedTime(ficheiro()).toInstant(), ZoneId.systemDefault());
            return Formatos.dataHora(t);
        } catch (IOException e) {
            return "-";
        }
    }
}
