package mz.agrogest.listener;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import mz.agrogest.repo.Persistencia;
import mz.agrogest.service.AuthService;
import mz.agrogest.util.DadosIniciais;

/**
 * Corre quando o Tomcat arranca e quando encerra a aplicação.
 *  - Arranque: restaura o estado do ficheiro .ser; se não houver, carrega dados de exemplo.
 *  - Encerramento: grava automaticamente o estado em ficheiro.
 */
@WebListener
public class AppListener implements ServletContextListener {

    private static final Logger LOG = Logger.getLogger(AppListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        boolean restaurado = Persistencia.carregar();
        if (!restaurado) {
            LOG.info("AgroGest: sem ficheiro gravado, a carregar dados de exemplo.");
            DadosIniciais.carregar();
        }
        new AuthService().garantirAdmin(); // a conta do admin existe sempre
        LOG.info("AgroGest: arranque concluído. Ficheiro de dados: " + Persistencia.ficheiro());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (!Persistencia.isGravarAoEncerrar()) {
            LOG.info("AgroGest: ficheiro eliminado manualmente, o estado nao e gravado no encerramento.");
            return;
        }
        try {
            Persistencia.gravar();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "AgroGest: falha ao gravar o estado no encerramento", e);
        }
    }
}
