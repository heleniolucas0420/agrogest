package mz.agrogest.service;

import mz.agrogest.model.Perfil;
import mz.agrogest.model.Utilizador;
import mz.agrogest.repo.DataStore;
import mz.agrogest.util.PasswordUtil;
import mz.agrogest.util.Validar;

/** Autenticação (login) e auto-registo de clientes. */
public class AuthService {

    public static final String ADMIN_EMAIL = "admin@agro.mz";
    public static final String ADMIN_PASSWORD = "admin234";

    private final DataStore ds = DataStore.get();

    /** @return o utilizador autenticado, ou null se email/palavra-passe errados ou conta inactiva. */
    public Utilizador autenticar(String email, String password) {
        if (email == null || password == null) {
            return null;
        }
        Utilizador u = buscarPorEmail(email.trim());
        if (u == null || !u.isAtivo() || !PasswordUtil.verificar(password, u.getPasswordHash())) {
            return null;
        }
        return u;
    }

    public Utilizador registar(String nome, String email, String password, String telefone, String endereco) {
        String n = Validar.texto(nome, "Nome");
        String e = Validar.email(email);
        String p = Validar.texto(password, "Palavra-passe");
        String t = Validar.texto(telefone, "Telefone");
        String end = Validar.texto(endereco, "Endereço");
        if (p.length() < 4) {
            throw new ValidacaoException("A palavra-passe deve ter pelo menos 4 caracteres.");
        }
        synchronized (ds.lock) {
            if (buscarPorEmail(e) != null) {
                throw new ValidacaoException("Já existe uma conta com o email " + e + ".");
            }
            Utilizador u = new Utilizador(ds.seqUtilizador.incrementAndGet(), n, e,
                    PasswordUtil.gerarHash(p), Perfil.CLIENTE, t, end);
            ds.utilizadores.add(u);
            return u;
        }
    }

    /** Conta fixa do administrador, criada no arranque se ainda não existir. */
    public void garantirAdmin() {
        synchronized (ds.lock) {
            if (buscarPorEmail(ADMIN_EMAIL) == null) {
                Utilizador admin = new Utilizador(ds.seqUtilizador.incrementAndGet(), "Administrador",
                        ADMIN_EMAIL, PasswordUtil.gerarHash(ADMIN_PASSWORD), Perfil.ADMIN,
                        "+258 84 000 0000", "Nampula");
                ds.utilizadores.add(admin);
            }
        }
    }

    public Utilizador buscarPorEmail(String email) {
        for (Utilizador u : DataStore.copia(ds.utilizadores)) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return u;
            }
        }
        return null;
    }

    public Utilizador buscarPorId(int id) {
        for (Utilizador u : DataStore.copia(ds.utilizadores)) {
            if (u.getId() == id) {
                return u;
            }
        }
        return null;
    }
}
