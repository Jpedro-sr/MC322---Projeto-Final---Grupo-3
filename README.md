# 🍕 iFome - Sistema de Delivery

Um sistema completo de entrega de comida desenvolvido em Java com arquitetura orientada a objetos, persistência de dados e interface gráfica JavaFX.

**Disciplina:** MC322 - Programação Orientada a Objetos  
**Instituição:** Universidade Estadual de Campinas (Unicamp)  
**Semestre:** 1º semestre de 2025

---

## 📋 Índice

- [Características](#características)
- [Arquitetura](#arquitetura)
- [Pré-requisitos](#pré-requisitos)
- [Instalação](#instalação)
- [Como Usar](#como-usar)
- [Validações e Segurança](#validações-e-segurança)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Limitações Conhecidas](#limitações-conhecidas)
- [Tratamento de Erros](#tratamento-de-erros)
- [Testes](#testes)
- [Melhorias Futuras](#melhorias-futuras)

---

## ✨ Características

### 🛍️ Para Clientes
- ✅ Cadastro e login de conta com validações robustas
- ✅ Buscar restaurantes abertos
- ✅ Visualizar cardápios completos com informações detalhadas
- ✅ Adicionar produtos ao carrinho com observações
- ✅ Aplicar cupons de desconto (verificação de uso único)
- ✅ Gerenciar múltiplos endereços
- ✅ Finalizar pedidos com validação de valor mínimo
- ✅ Múltiplas formas de pagamento (PIX, Cartão, Dinheiro)
- ✅ Acompanhar histórico de pedidos
- ✅ Avaliar pedidos entregues (notas 1-5 com comentários)

### 🏪 Para Restaurantes
- ✅ Cadastro e login de conta com validação de CNPJ
- ✅ Gerenciar cardápio (adicionar/remover/atualizar produtos)
- ✅ Abrir e fechar restaurante
- ✅ Visualizar fila de pedidos
- ✅ Aceitar/Recusar pedidos pendentes
- ✅ Atualizar status dos pedidos (Pendente → Confirmado → Preparando → Pronto → Em Entrega → Entregue)
- ✅ Cancelar pedidos em qualquer etapa
- ✅ Ver estatísticas e produtos mais vendidos
- ✅ Receber avaliações dos clientes
- ✅ Calcular média de avaliações

### 🔧 Funcionalidades Técnicas
- ✅ Interface gráfica moderna com JavaFX
- ✅ Persistência de dados em arquivos `.txt`
- ✅ Padrão Singleton para gerenciamento de sessão
- ✅ Validação robusta de CPF, CNPJ, telefone, email
- ✅ Proteção contra SQL injection e caracteres especiais
- ✅ Limites de tamanho para todos os campos de texto
- ✅ Validação de dígitos verificadores de CNPJ
- ✅ Suporte UTF-8 completo
- ✅ Sistema de cupons com verificação de uso único
- ✅ Interfaces bem definidas (Calculavel, Rastreavel, Avaliavel, etc.)

---

## 🏗️ Arquitetura

### Padrões de Design Utilizados

**1. Singleton**
- `SessaoUsuario`: Gerencia única sessão do usuário logado
- `RepositorioRestaurantes`: Acesso único ao repositório de dados

**2. Herança e Polimorfismo**
- `Usuario` → `Cliente` e `Restaurante`
- `Produto` → `Comida`, `Bebida`, `Sobremesa`, `Adicional`
- `FormaPagamento` → `PIX`, `CartaoCredito`, `Dinheiro`

**3. Interfaces**
- `Calculavel`: Calcula preços totais
- `Rastreavel`: Rastreia status de pedidos
- `Avaliavel`: Permite avaliações
- `Promocional`: Aplica descontos

**4. Composição e Agregação**
- Composição: `Pedido` contém `ItemPedido`
- Agregação: `Cliente` gerencia múltiplos `Endereco` e `Pedido`

---

## 📦 Pré-requisitos

- **Java 11+** (recomendado Java 17+)
- **JavaFX 19+** (incluído nas dependências)
- **Gradle 9.0+** (já incluído no projeto via Gradle Wrapper)
- **Sistema Operacional**: Windows, Linux ou macOS

### Verificar Instalação

```bash
# Verificar Java
java -version

# Verificar Gradle (não necessário se usar o wrapper)
gradle --version
```

---

## 🚀 Instalação

### 1. Clonar o Repositório

```bash
git clone <URL-DO-REPOSITORIO>
cd MC322-Projeto-Final-Grupo-3
```

### 2. Compilar o Projeto

**Windows:**
```bash
gradlew.bat build
```

**Linux/macOS:**
```bash
./gradlew build
```

### 3. Executar a Aplicação

**Windows:**
```bash
gradlew.bat runApp
```

**Linux/macOS:**
```bash
./gradlew runApp
```

---

## 💻 Como Usar

### Interface Gráfica (Padrão)

O sistema inicia com uma interface gráfica moderna desenvolvida em JavaFX:

1. **Tela de Login**
   - Digite email e senha para acessar
   - Opção de criar conta (Cliente ou Restaurante)

2. **Menu Cliente**
   - Escolher Restaurante → Navega pelos restaurantes abertos
   - Ver Carrinho → Visualiza itens selecionados
   - Meus Pedidos → Histórico e rastreamento
   - Avaliar Pedidos → Avalia pedidos entregues

3. **Menu Restaurante**
   - Gerenciar Pedidos → Aceita/recusa e atualiza status
   - Gerenciar Cardápio → Adiciona/remove produtos
   - Ver Estatísticas → Visualiza métricas de vendas
   - Abrir/Fechar → Controla disponibilidade

### Modo Console (Alternativo)

Para executar no modo console, edite `Aplicacao.java` e descomente:

```java
AplicacaoConsole.main(args);
```

---

## 🔒 Validações e Segurança

### ✅ **Validações Implementadas**

#### **1. CNPJ (Restaurante)**
```java
// ✅ Remove caracteres não numéricos
// ✅ Valida 14 dígitos exatos
// ✅ Rejeita sequências repetitivas (11111111111111)
// ✅ Valida dígitos verificadores com algoritmo oficial
String cnpj = "12.345.678/0001-99"; // Formato aceito
String cnpj = "12345678000199";     // Formato aceito
String cnpj = "123";                 // ❌ REJEITADO: Muito curto
String cnpj = "11111111111111";      // ❌ REJEITADO: Repetitivo
String cnpj = "12345678000100";      // ❌ REJEITADO: Dígito verificador inválido
```

#### **2. Telefone (Cliente)**
```java
// ✅ Remove caracteres não numéricos
// ✅ Valida mínimo 10 e máximo 15 dígitos
// ✅ Rejeita sequências repetitivas
// ✅ Limite de 20 caracteres no formato original
String tel = "(11) 99999-9999";  // ✓ Válido (11 dígitos)
String tel = "11999999999";      // ✓ Válido
String tel = "+55 11 99999-9999"; // ✓ Válido (13 dígitos)
String tel = "999";               // ❌ REJEITADO: Muito curto
String tel = "11111111111";       // ❌ REJEITADO: Repetitivo
String tel = "-123456789";        // ❌ REJEITADO: Negativo (removido por regex)
```

#### **3. Nome (Cliente/Restaurante)**
```java
// ✅ Remove caracteres perigosos: < > " ' & ;
// ✅ Limite máximo de 100 caracteres
// ✅ Mínimo de 2 caracteres (Cliente) / 3 caracteres (Restaurante)
String nome = "João da Silva";           // ✓ Válido
String nome = "Restaurante & Cia";       // ✓ Válido (& removido → "Restaurante  Cia")
String nome = "<script>alert(1)</script>"; // ✓ Protegido (caracteres removidos)
String nome = "A".repeat(150);           // ❌ REJEITADO: Muito longo
String nome = "J";                       // ❌ REJEITADO: Muito curto
```

#### **4. Email**
```java
// ✅ Deve conter "@" e "."
// ✅ Limite de tamanho implícito (banco de dados)
String email = "usuario@email.com";  // ✓ Válido
String email = "teste@";             // ❌ REJEITADO: Sem domínio
String email = "teste.com";          // ❌ REJEITADO: Sem @
```

#### **5. Preço de Produtos**
```java
// ✅ Deve ser maior que zero
// ✅ Conversão automática de vírgula para ponto
// ✅ Máximo de 2 casas decimais
double preco = 45.90;   // ✓ Válido
double preco = 0.01;    // ✓ Válido
double preco = -10.00;  // ❌ REJEITADO: Negativo
double preco = 0;       // ❌ REJEITADO: Zero
```

#### **6. Cupons de Desconto**
```java
// ✅ Verificação de uso único por cliente
// ✅ Validação de validade e status ativo
// ✅ Código em UPPERCASE automático
Cupom cupom = Cupom.criarCupomPercentual("DESC10", 10);
cliente.jaUsouCupom("DESC10"); // Verifica se já foi usado
cliente.registrarUsoCupom("DESC10"); // Registra uso
```

### 🛡️ **Proteções Contra Ataques**

| Tipo de Ataque | Proteção Implementada |
|----------------|----------------------|
| **SQL Injection** | Não aplicável (usa arquivos, não SQL) |
| **XSS (Cross-Site Scripting)** | Remoção de caracteres perigosos `< > " ' & ;` |
| **Buffer Overflow** | Limites de tamanho em todos os campos |
| **Números Negativos** | Regex remove sinais negativos antes da validação |
| **Textos Muito Longos** | Limite de 100 caracteres (nome), 20 (telefone), etc. |
| **CNPJ Inválidos** | Validação completa com dígitos verificadores |
| **Sequências Repetitivas** | Rejeita 11111111111, 00000000000, etc. |

---

## 📁 Estrutura do Projeto

```
MC322-Projeto-Final-Grupo-3/
├── src/
│   ├── main/
│   │   ├── java/ifome/
│   │   │   ├── Aplicacao.java              # ✅ Ponto de entrada (JavaFX)
│   │   │   ├── AplicacaoConsole.java       # Versão console (opcional)
│   │   │   ├── controller/                 # Controladores JavaFX
│   │   │   │   ├── LoginController.java
│   │   │   │   ├── CadastroController.java
│   │   │   │   ├── MenuClienteController.java
│   │   │   │   ├── MenuRestauranteController.java
│   │   │   │   ├── GerenciarPedidosController.java  # ✅ Cancelamento corrigido
│   │   │   │   └── ...
│   │   │   ├── model/                      # Modelos de dados
│   │   │   │   ├── Cliente.java            # ✅ Validações robustas
│   │   │   │   ├── Restaurante.java        # ✅ Validação CNPJ
│   │   │   │   ├── Pedido.java
│   │   │   │   ├── Produto.java
│   │   │   │   ├── Carrinho.java
│   │   │   │   └── ...
│   │   │   ├── util/                       # Utilitários
│   │   │   │   ├── SessaoUsuario.java      # Singleton
│   │   │   │   └── RepositorioRestaurantes.java
│   │   │   └── exceptions/                 # Exceções customizadas
│   │   │       ├── PagamentoRecusadoException.java
│   │   │       ├── RestauranteFechadoException.java
│   │   │       └── ...
│   │   └── resources/ifome/                # Arquivos FXML (interfaces)
│   │       ├── TelaLogin.fxml
│   │       ├── MenuCliente.fxml
│   │       ├── GerenciarPedidos.fxml       # ✅ Interface de pedidos
│   │       └── ...
│   └── test/java/ifome/                    # Testes unitários
│       ├── ClienteTest.java
│       ├── RestauranteTest.java
│       ├── CarrinhoTest.java
│       └── ...
├── data/                                   # ✅ Persistência de dados
│   ├── clientes.txt
│   ├── restaurantes.txt
│   ├── pedidos.txt
│   ├── cardapios.txt
│   ├── cupons.txt
│   ├── cupons_usados.txt                  # ✅ Rastreamento de cupons
│   ├── avaliacoes.txt
│   └── ...
├── build.gradle                           # Configuração do Gradle
├── settings.gradle
├── gradlew / gradlew.bat                  # Gradle Wrapper
└── README.md                              # Este arquivo
```

---

## ⚠️ Limitações Conhecidas

### 🔄 **Sincronização Entre Instâncias**

**Pergunta:** É possível rodar duas instâncias do sistema ao mesmo tempo e comunicá-las através da persistência?

**Resposta:** **NÃO**, com a arquitetura atual.

**Por quê?**
- A persistência em arquivos `.txt` só sincroniza quando você **salva** (`salvarDados()`) ou **carrega** (`carregarDados()`)
- Não há mecanismo de **polling** (verificação periódica) ou **push** (notificação em tempo real)
- Se você abrir duas instâncias:
  - Instância A abre o restaurante
  - Instância B **não verá** essa mudança até reiniciar

**Como fazer funcionar?**
Para implementar sincronização em tempo real, seria necessário:
1. **Banco de Dados** (PostgreSQL, MySQL, SQLite) com polling a cada X segundos
2. **WebSockets** ou **Server-Sent Events** para notificações push
3. **Sistema de Mensageria** (RabbitMQ, Kafka) para comunicação entre processos
4. **Arquivos de Lock** com monitoramento de mudanças (solução mais simples, mas menos eficiente)

**Solução atual:**
- Cada instância opera de forma **independente**
- Dados são sincronizados apenas no **início** (load) e **fim** (save)
- Para testar multi-usuário, use apenas **UMA instância** e faça login/logout

---

## 🧪 Testes

### Executar Testes Unitários

```bash
# Windows
gradlew.bat test

# Linux/macOS
./gradlew test
```

### Cenários de Teste Implementados

| Classe de Teste | Cenários Cobertos |
|----------------|-------------------|
| **ClienteTest** | Cadastro, validação de telefone, endereços |
| **RestauranteTest** | Validação CNPJ, cardápio, avaliações |
| **CarrinhoTest** | Adicionar itens, aplicar cupons, gerar pedidos |
| **PedidoTest** | Atualização de status, cálculo de total |
| **FormaPagamentoTest** | PIX, Cartão, Dinheiro |
| **CupomTest** | Desconto percentual, desconto fixo |

### ✅ **Novos Testes Recomendados**

1. **Validação de CNPJ**
   ```java
   // Teste com dígitos verificadores inválidos
   assertThrows(IllegalArgumentException.class, () -> {
       new Restaurante("email", "123", "Nome", "12345678000100");
   });
   ```

2. **Validação de Telefone Negativo**
   ```java
   // Teste com número negativo
   assertThrows(IllegalArgumentException.class, () -> {
       new Cliente("email", "123", "Nome", "-11999999999");
   });
   ```

3. **Uso Duplo de Cupom**
   ```java
   cliente.registrarUsoCupom("DESC10");
   assertTrue(cliente.jaUsouCupom("DESC10"));
   ```

4. **Cancelamento de Pedido**
   ```java
   pedido.atualizarStatus("Cancelado");
   assertEquals("Cancelado", pedido.getStatus());
   ```

---

## 📈 Melhorias Futuras

### Versão 2.0 (Próximas Iterações)
- [ ] Banco de dados (PostgreSQL/MySQL) para sincronização em tempo real
- [ ] Sistema de WebSockets para comunicação multi-instância
- [ ] Autenticação JWT/OAuth
- [ ] Criptografia de senhas (bcrypt, argon2)
- [ ] API RESTful para integração com apps mobile
- [ ] Sistema de notificações push
- [ ] Integração com gateway de pagamento real (Stripe, PagSeguro)
- [ ] Raio de entrega com geolocalização (API Google Maps)
- [ ] Sistema de entregadores (rastreamento GPS)
- [ ] Chat entre cliente e restaurante
- [ ] Dashboard administrativo
- [ ] Relatórios avançados (PDF, Excel)

### Segurança Avançada
- [ ] Rate limiting para login
- [ ] Captcha para registro
- [ ] Auditoria de ações (logs)
- [ ] Backup automático de dados
- [ ] Recuperação de senha por email

---

## 📝 Sobre o Projeto

Este projeto foi desenvolvido como trabalho final da disciplina **MC322 - Programação Orientada a Objetos** da Universidade Estadual de Campinas (Unicamp).

### Objetivos Alcançados

✅ Aplicar conceitos de POO (herança, polimorfismo, encapsulamento, abstração)  
✅ Utilizar padrões de design (Singleton, Factory Method)  
✅ Implementar persistência de dados  
✅ Criar interfaces bem definidas  
✅ Tratar exceções apropriadamente  
✅ Desenvolver interface gráfica moderna (JavaFX)  
✅ Implementar validações robustas e segurança  
✅ Trabalhar em equipe com controle de versão (Git)  
✅ Documentar código e funcionalidades  

---

## 👥 Autores

- **Rafael Rodrigues Pimentel de Melo**
- **Matheus Boazão Silveira**
- **João Pedro dos Santos Rodrigues**
- **Pedro Romasanta Rosa**

---

## 📄 Licença

Este projeto é fornecido como trabalho acadêmico e pode ser usado livremente para fins educacionais.

---

## 📞 Suporte

### Problemas Comuns

**P: Erro de validação ao cadastrar CNPJ**  
R: ✅ Agora validamos dígitos verificadores. Use um CNPJ válido ou desabilite a validação para testes

**P: Não consigo aplicar o mesmo cupom duas vezes**  
R: ✅ Isso é intencional! Cada cupom pode ser usado apenas uma vez por cliente

**P: Cancelamento de pedido não funciona**  
R: ✅ CORRIGIDO! Agora há dois tipos de cancelamento:
- **RECUSAR** (Pendente): Recusa antes de aceitar
- **CANCELAR** (Após aceito): Cancela em qualquer etapa

**P: Mudanças em uma instância não aparecem em outra**  
R: ✅ Isso é uma limitação técnica. Use apenas UMA instância ou implemente banco de dados

**P: Caracteres especiais no nome causam problemas**  
R: ✅ CORRIGIDO! Removemos automaticamente caracteres perigosos

---

## 🔗 Links Úteis

- [Java Documentation](https://docs.oracle.com/en/java/)
- [JavaFX Documentation](https://openjfx.io/)
- [Gradle User Guide](https://docs.gradle.org/current/userguide/userguide.html)
- [Validação de CNPJ](https://www.devmedia.com.br/validando-o-cnpj-em-java/33224)
- [Regex Tutorial](https://regexr.com/)

---

**Última atualização:** Janeiro de 2025  
**Status:** ✅ Completo, testado e validado  
**Versão:** 2.0 - Release Final