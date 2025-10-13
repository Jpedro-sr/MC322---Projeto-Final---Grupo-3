package app;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Classe responsável por salvar e carregar o estado do jogo.
 * Utiliza JAXB para serialização em XML.
 * (Conforme Tarefa 6, Seção 3.1)
 */
public class GerenciadorDePersistencia {
    
    private static final String PASTA_SAVES = "saves";
    private static final String EXTENSAO = ".xml";
    
    /**
     * Salva o estado atual da batalha em um arquivo XML.
     * @param batalha A batalha a ser salva
     * @param nomeBatalha Nome do arquivo (sem extensão)
     * @return true se salvou com sucesso, false caso contrário
     */
    public static boolean salvarBatalha(Batalha batalha, String nomeBatalha) {
        try {
            // Cria pasta de saves se não existir
            criarPastaSaves();
            
            // Cria contexto JAXB
            JAXBContext context = JAXBContext.newInstance(Batalha.class);
            Marshaller marshaller = context.createMarshaller();
            
            // Formata o XML para ficar legível
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            
            // Salva em arquivo
            File arquivo = new File(PASTA_SAVES + File.separator + nomeBatalha + EXTENSAO);
            marshaller.marshal(batalha, arquivo);
            
            System.out.println("[DEBUG] Jogo salvo em: " + arquivo.getAbsolutePath());
            return true;
            
        } catch (JAXBException e) {
            System.err.println("[ERRO] Falha ao salvar batalha: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Carrega uma batalha salva a partir de um arquivo XML.
     * @param nomeBatalha Nome do arquivo (sem extensão)
     * @return A batalha carregada, ou null se houver erro
     */
    public static Batalha carregarBatalha(String nomeBatalha) {
        try {
            File arquivo = new File(PASTA_SAVES + File.separator + nomeBatalha + EXTENSAO);
            
            if (!arquivo.exists()) {
                System.err.println("[ERRO] Arquivo não encontrado: " + arquivo.getAbsolutePath());
                return null;
            }
            
            // Cria contexto JAXB
            JAXBContext context = JAXBContext.newInstance(Batalha.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            
            // Carrega do arquivo
            Batalha batalha = (Batalha) unmarshaller.unmarshal(arquivo);
            
            System.out.println("[DEBUG] Jogo carregado de: " + arquivo.getAbsolutePath());
            return batalha;
            
        } catch (JAXBException e) {
            System.err.println("[ERRO] Falha ao carregar batalha: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Lista todos os saves disponíveis.
     * @return Array com nomes dos saves (sem extensão)
     */
    public static String[] listarSaves() {
        File pasta = new File(PASTA_SAVES);
        
        if (!pasta.exists() || !pasta.isDirectory()) {
            return new String[0];
        }
        
        File[] arquivos = pasta.listFiles((dir, nome) -> nome.endsWith(EXTENSAO));
        
        if (arquivos == null || arquivos.length == 0) {
            return new String[0];
        }
        
        String[] nomes = new String[arquivos.length];
        for (int i = 0; i < arquivos.length; i++) {
            nomes[i] = arquivos[i].getName().replace(EXTENSAO, "");
        }
        
        return nomes;
    }
    
    /**
     * Verifica se existe algum save disponível.
     */
    public static boolean existemSaves() {
        return listarSaves().length > 0;
    }
    
    /**
     * Cria a pasta de saves se ela não existir.
     */
    private static void criarPastaSaves() {
        try {
            Path path = Paths.get(PASTA_SAVES);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (Exception e) {
            System.err.println("[ERRO] Falha ao criar pasta de saves: " + e.getMessage());
        }
    }
}