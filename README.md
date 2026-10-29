# AgroGest — Sistema de Gestão Agrícola

Aplicação web em **Java 17 + Servlets + JSP**, publicada no **Apache Tomcat 10.1** e construída com **Maven** (empacotamento WAR).
Todos os dados vivem em **memória RAM** (listas Java). Não há base de dados. O estado é gravado num ficheiro (`agrogest.ser`) quando o Tomcat encerra e é restaurado no arranque.

---

## 1. Requisitos

| Ferramenta | Versão |
|---|---|
| Java (JDK) | 17 ou superior |
| Apache Maven | 3.8 ou superior |
| Apache Tomcat | 10.1.x |

Verificar:

```bash
java -version
mvn -v
```

---

## 2. Compilar (gerar o WAR)

Na pasta do projecto (onde está o `pom.xml`):

```bash
mvn clean package
```

Resultado: **`target/agrogest.war`**

---

## 3. Correr a aplicação

### Opção A — Tomcat instalado (forma pedida no enunciado)

1. Descarregar o Tomcat 10.1 (zip / tar.gz) em https://tomcat.apache.org e extrair, por exemplo:
   - Windows: `C:\tomcat`
   - Linux: `/opt/tomcat`
2. **Mudar a porta para 8081** — abrir `conf/server.xml` e trocar:
   ```xml
   <Connector port="8080" protocol="HTTP/1.1"
   ```
   por
   ```xml
   <Connector port="8081" protocol="HTTP/1.1"
   ```
3. Copiar `target/agrogest.war` para a pasta `webapps/` do Tomcat.
4. Arrancar o Tomcat:
   - Windows: `C:\tomcat\bin\startup.bat`
   - Linux: `/opt/tomcat/bin/startup.sh`
5. Abrir no browser: **http://localhost:8081/agrogest**

Parar o Tomcat (grava os dados em ficheiro):
- Windows: `C:\tomcat\bin\shutdown.bat`
- Linux: `/opt/tomcat/bin/shutdown.sh`

### Opção B — Arranque rápido com Maven (para testar)

Descarrega um Tomcat 10.1 automaticamente e corre a app na porta 8081:

```bash
mvn clean package cargo:run
```

Abrir **http://localhost:8081/agrogest**. Parar com `Ctrl + C`.

---

## 4. Credenciais de teste

| Perfil | Email | Palavra-passe |
|---|---|---|
| Administrador | `admin@agro.mz` | `admin234` |
| Cliente | `maria@teste.mz` | `1234` |
| Cliente | `carlos@teste.mz` | `1234` |
| Cliente | `ana@teste.mz` | `1234` |
| Cliente | `pedro@teste.mz` | `1234` |
| Cliente | `fatima@teste.mz` | `1234` |

No primeiro arranque (sem ficheiro gravado) o sistema carrega dados de exemplo:
**3 fornecedores · 3 produtos · 5 clientes · 4 vendas · 1 campanha**.

---

## 5. Deploy em Linux com systemd

```bash
# 1) Utilizador e pasta do Tomcat
sudo useradd -r -m -U -d /opt/tomcat -s /bin/false tomcat
sudo tar xzf apache-tomcat-10.1.*.tar.gz -C /opt/tomcat --strip-components=1
sudo sed -i 's/port="8080"/port="8081"/' /opt/tomcat/conf/server.xml
sudo chown -R tomcat:tomcat /opt/tomcat
sudo chmod +x /opt/tomcat/bin/*.sh

# 2) Serviço systemd (confirme o JAVA_HOME dentro do ficheiro)
sudo cp deploy/agrogest-tomcat.service /etc/systemd/system/tomcat.service
sudo systemctl daemon-reload
sudo systemctl enable --now tomcat

# 3) Publicar a app
mvn clean package
sudo cp target/agrogest.war /opt/tomcat/webapps/
```

Comandos úteis:

```bash
sudo systemctl status tomcat      # estado
sudo systemctl restart tomcat     # reiniciar
sudo systemctl stop tomcat        # parar (grava o estado em ficheiro)
sudo journalctl -u tomcat -f      # ver logs do serviço
tail -f /opt/tomcat/logs/catalina.out
```

Ou, de uma vez: `./deploy/deploy.sh`

---

## 6. Arquitectura (MVC)

```
Browser ──HTTP :8081──► Tomcat 10.1
                          │
                          ├─ filter/AuthFilter        sessão + perfil (403 em /admin/* para não-admin)
                          ├─ servlet/*                Controllers (Login, Registo, Catalogo, Carrinho,
                          │                           Compra, Factura, MinhasCompras, admin/*)
                          ├─ service/*                Regras de negócio (VendaService = synchronized, tudo-ou-nada)
                          ├─ repo/DataStore           Listas em memória (synchronizedList + AtomicInteger)
                          ├─ repo/Persistencia        Gravação em ficheiro .ser (escrita atómica + .bak)
                          ├─ listener/AppListener     Restaura no arranque / grava no encerramento
                          └─ WEB-INF/views/*.jsp      Vistas (JSP + JSTL + EL + CSS próprio)
```

### Pacotes

| Pacote | Responsabilidade |
|---|---|
| `mz.agrogest.model` | POJOs `Serializable`: Utilizador, Fornecedor, Produto, Campanha, Cultivo, Colheita, Venda, VendaItem, Carrinho… |
| `mz.agrogest.repo` | `DataStore` (dados em RAM) e `Persistencia` (ficheiro) |
| `mz.agrogest.service` | Regras de negócio e validações |
| `mz.agrogest.servlet` | Controladores (`@WebServlet`) |
| `mz.agrogest.filter` | `AuthFilter` (`@WebFilter`) |
| `mz.agrogest.listener` | `AppListener` (`@WebListener`) |
| `mz.agrogest.util` | Hash de passwords, validações, formatos, dados de exemplo |

### Regras de negócio implementadas

| Regra | Onde |
|---|---|
| a) Não vender mais que o stock | `VendaService.finalizarCompra` (fase 1) e `verificarDisponibilidade` ao adicionar ao carrinho |
| b) Venda desconta stock de todos os itens, tudo ou nada | `VendaService.finalizarCompra` — valida tudo primeiro, só depois desconta, dentro de `synchronized` |
| c) Cliente só vê as suas facturas; admin vê todas | `FacturaServlet` (HTTP 403) e `VendaService.listarDoCliente` |
| d) `/admin/*` só para o administrador (HTTP 403) | `AuthFilter` |
| e) `/catalogo`, `/carrinho/*`, `/compra`, `/factura`, `/minhas-compras` exigem sessão | `AuthFilter` |
| f) IDs sequenciais e únicos com `AtomicInteger` | `DataStore` |

Outras validações: produtos fora da validade não podem ser vendidos (nem criados com validade já passada), não remover campanha com cultivos, não remover cultivo com colheitas, não remover fornecedor com produtos, email único no registo, campanha fechada não aceita cultivos.

### Persistência em ficheiro

- Ficheiro: `CATALINA_BASE/agrogest-dados/agrogest.ser` (ou o caminho em `-Dagrogest.dados=...`).
- Gravação automática quando o Tomcat encerra e depois de cada operação que altera dados (venda, registo, gestão do admin); restauro automático no arranque. Se o Tomcat for parado à força, perde-se no máximo a última operação.
- Endpoint manual: **Admin → Persistência** (`POST /admin/persistencia` com `acao=gravar` ou `acao=eliminar`).
- Escrita atómica: grava em `.tmp`, guarda a versão anterior em `.bak` e só depois troca. Se o ficheiro principal estiver corrompido, usa o `.bak`.
- Para voltar aos dados de exemplo: eliminar o ficheiro e reiniciar o Tomcat.

### Segurança

- Palavras-passe guardadas com **SHA-256 + sal aleatório** (nunca em texto puro).
- Nova sessão a cada login (evita *session fixation*); cookie de sessão `HttpOnly`.
- Todo o texto vindo do utilizador é escapado nas JSP com `<c:out>` (evita XSS).

---

## 7. Rotas principais

| Rota | Quem | O quê |
|---|---|---|
| `/login`, `/registo`, `/logout` | todos | autenticação e auto-registo |
| `/catalogo` | sessão | produtos com filtro por categoria |
| `/carrinho`, `/carrinho/adicionar`, `/carrinho/atualizar`, `/carrinho/remover`, `/carrinho/esvaziar` | cliente | carrinho em sessão |
| `/compra` | cliente | confirmar e emitir factura |
| `/factura?id=N` (`&imprimir=1`) | dono ou admin | ver / imprimir factura |
| `/minhas-compras` | cliente | histórico |
| `/admin` | admin | painel |
| `/admin/fornecedores`, `/admin/produtos`, `/admin/campanhas`, `/admin/cultivos`, `/admin/colheitas` | admin | gestão |
| `/admin/listagens`, `/admin/compras`, `/admin/relatorios` | admin | listagens com ordenação, compras detalhadas, relatórios |
| `/admin/persistencia` | admin | gravar / eliminar ficheiro |

---

## 8. Problemas comuns

| Problema | Solução |
|---|---|
| Porta 8081 ocupada | Mudar a porta em `conf/server.xml` (ou `<tomcat.porta>` no `pom.xml` para o cargo) |
| Página 404 em `/agrogest` | Confirmar que o ficheiro se chama `agrogest.war` e que o Tomcat o extraiu em `webapps/agrogest/` |
| Erro com `javax.servlet` | Está a usar Tomcat 9. Este projecto usa `jakarta.servlet` → **Tomcat 10.1** |
| Acentos estranhos | Os ficheiros estão em UTF-8; arrancar o Tomcat com `-Dfile.encoding=UTF-8` |
