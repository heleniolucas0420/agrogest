package mz.agrogest.repo;

import java.io.Serializable;
import java.util.ArrayList;

import mz.agrogest.model.Campanha;
import mz.agrogest.model.Colheita;
import mz.agrogest.model.Cultivo;
import mz.agrogest.model.Fornecedor;
import mz.agrogest.model.Produto;
import mz.agrogest.model.Utilizador;
import mz.agrogest.model.Venda;

/**
 * "Fotografia" de todo o estado do sistema. E este objecto que e gravado
 * no ficheiro .ser (serializacao Java) e lido no arranque.
 */
public class Snapshot implements Serializable {

    private static final long serialVersionUID = 1L;

    ArrayList<Utilizador> utilizadores;
    ArrayList<Fornecedor> fornecedores;
    ArrayList<Produto> produtos;
    ArrayList<Campanha> campanhas;
    ArrayList<Cultivo> cultivos;
    ArrayList<Colheita> colheitas;
    ArrayList<Venda> vendas;

    int seqUtilizador;
    int seqFornecedor;
    int seqProduto;
    int seqCampanha;
    int seqCultivo;
    int seqColheita;
    int seqVenda;
    int seqFactura;
}
