# 🍕 iFome - Sistema de Delivery

Um sistema completo de entrega de comida desenvolvido em Java com arquitetura orientada a objetos, persistência de dados e interface por linha de comando.

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
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Documentação de Funcionalidades](#documentação-de-funcionalidades)
- [Dados de Teste](#dados-de-teste)
- [Tratamento de Erros](#tratamento-de-erros)
- [Melhorias Futuras](#melhorias-futuras)

---

## ✨ Características

### 🛍️ Para Clientes
- ✅ Cadastro e login de conta
- ✅ Buscar restaurantes abertos
- ✅ Visualizar cardápios completos
- ✅ Adicionar produtos ao carrinho com observações
- ✅ Aplicar cupons de desconto
- ✅ Gerenciar múltiplos endereços
- ✅ Finalizar pedidos com validação de valor mínimo
- ✅ Múltiplas formas de pagamento (PIX, Cartão, Dinheiro)
- ✅ Acompanhar histórico de pedidos
- ✅ Avaliar pedidos entregues (notas 1-5 com comentários)

### 🏪 Para Restaurantes
- ✅ Cadastro e login de conta
- ✅ Gerenciar cardápio (adicionar/remover/atualizar produtos)
- ✅ Abrir e fechar restaurante
- ✅ Visualizar fila de pedidos
- ✅ Atualizar status dos pedidos (Pendente → Confirmado → Preparando → Pronto → Em Entrega → Entregue)
- ✅ Receber avaliações dos clientes
- ✅ Calcular média de avaliações

### 🔧 Funcionalidades Técnicas
- ✅ Persistência de dados em arquivos `.txt`
- ✅ Padrão Singleton para gerenciamento de sessão
- ✅ Validação de CEP (formato brasileiro)
- ✅ Validação de CNPJ
- ✅ Criptografia básica de senhas
- ✅ Suporte UTF-8 em Windows
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

### Camadas da Aplicação

```
┌─────────────────────────────────┐
│    Aplicacao (Interface CLI)    │
├─────────────────────────────────┤
│  model/  (Lógica de Negócio)    │
│  util/   (Utilitários)          │
│  exceptions/ (Exceções)         │
└─────────────────────────────────┘
       ↓
  data/ (Arquivos)
```

---

## 📦 Pré-requisitos

- **Java 11+** (recomendado Java 17+)
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

### Menu Principal

```
iFOME - SISTEMA DE DELIVERY
==================================================
1. [CLIENTE] Login
2. [CLIENTE] Criar Conta
3. [RESTAURANTE] Login
4. [RESTAURANTE] Cadastrar
5. Ver Restaurantes Disponiveis
0. Sair
```

### 👤 Fluxo do Cliente

1. **Login/Cadastro**
   - Digite email e senha
   - Sistema valida credenciais
   - Cria conta se necessário

2. **Escolher Restaurante**
   - Visualiza lista de restaurantes abertos
   - Seleciona um restaurante
   - Acessa cardápio completo

3. **Fazer Pedido**
   - Seleciona produtos
   - Define quantidade e observações
   - Adiciona ao carrinho
   - Aplica cupom (opcional)
   - Confirma endereço de entrega
   - Escolhe forma de pagamento
   - Finaliza pedido

4. **Gerenciar Pedidos**
   - Visualiza histórico de pedidos
   - Rastreia status em tempo real
   - Avalia pedidos entregues

### 🏪 Fluxo do Restaurante

1. **Login/Cadastro**
   - Cria conta do restaurante
   - Define credenciais de acesso

2. **Gerenciar Cardápio**
   - Adiciona novos produtos
   - Remove produtos indisponíveis
   - Atualiza preços
   - Define disponibilidade

3. **Gerenciar Pedidos**
   - Visualiza fila de pedidos
   - Atualiza status (Preparando → Pronto → Entrega)
   - Marca como entregue

4. **Controlar Restaurante**
   - Abre/fecha para receber pedidos
   - Visualiza avaliações dos clientes

---

## 📁 Estrutura do Projeto

```
MC322-Projeto-Final-Grupo-3/
├── src/
│   └── main/
│       └── java/
│           └── ifome/
│               ├── Aplicacao.java           # Classe principal (menus)
│               ├── model/
│               │   ├── Usuario.java         # Classe base abstrata
│               │   ├── Cliente.java         # Usuário cliente
│               │   ├── Restaurante.java     # Usuário restaurante
│               │   ├── Pedido.java          # Pedido finalizado
│               │   ├── Carrinho.java        # Carrinho de compras
│               │   ├── ItemPedido.java      # Item no pedido
│               │   ├── Produto.java         # Classe base de produtos
│               │   ├── Comida.java          # Produto: comida
│               │   ├── Bebida.java          # Produto: bebida
│               │   ├── Sobremesa.java       # Produto: sobremesa
│               │   ├── Adicional.java       # Produto: adicional
│               │   ├── Endereco.java        # Endereço do cliente
│               │   ├── Cupom.java           # Cupom de desconto
│               │   ├── Avaliacao.java       # Avaliação (1-5 estrelas)
│               │   ├── FormaPagamento.java  # Classe abstrata
│               │   ├── PIX.java             # Pagamento PIX
│               │   ├── CartaoCredito.java   # Pagamento cartão
│               │   ├── Dinheiro.java        # Pagamento dinheiro
│               │   ├── Calculavel.java      # Interface
│               │   ├── Rastreavel.java      # Interface
│               │   ├── Avaliavel.java       # Interface
│               │   └── Promocional.java     # Interface
│               ├── util/
│               │   ├── InputManager.java    # Gerenciador de entrada
│               │   ├── SessaoUsuario.java   # Sessão (Singleton)
│               │   └── RepositorioRestaurantes.java # Repositório (Singleton)
│               └── exceptions/
│                   ├── PagamentoRecusadoException.java
│                   ├── RestauranteFechadoException.java
│                   ├── ProdutoIndisponivelException.java
│                   ├── ValorMinimoException.java
│                   └── EnderecoForaRaioException.java
├── data/
│   ├── restaurantes.txt        # Dados persistidos
│   └── clientes.txt            # Dados persistidos
├── build.gradle                # Configuração do Gradle
├── settings.gradle
├── gradlew                      # Gradle Wrapper (Linux/Mac)
├── gradlew.bat                  # Gradle Wrapper (Windows)
└── README.md                    # Este arquivo
```

---

## 🎯 Documentação de Funcionalidades

### Gerenciamento de Carrinho

```java
// Adicionar item
carrinho.adicionarItem(produto, quantidade, observacoes);

// Remover item
carrinho.removerItem(itemPedido);

// Aplicar cupom
carrinho.aplicarCupom(cupom);

// Gerar pedido
Pedido pedido = carrinho.gerarPedido();
```

**Validações:**
- Carrinho não pode estar vazio
- Valor mínimo: R$ 15,00
- Restaurante deve estar aberto
- Todos os produtos devem estar disponíveis

### Sistema de Pagamento

```java
// PIX
FormaPagamento pix = new PIX();

// Cartão de Crédito
FormaPagamento cartao = new CartaoCredito("1234567890123456", "NOME", "123");

// Dinheiro
FormaPagamento dinheiro = new Dinheiro(100.0);
```

**Validações Cartão:**
- 16 dígitos
- CVV 3-4 dígitos
- Data de validade formato MM/YY

### Sistema de Cupons

```java
// Cupom percentual
Cupom cupom = Cupom.criarCupomPercentual("DESCONTO10", 10.0);

// Cupom valor fixo
Cupom cupom = Cupom.criarCupomFixo("PRIMEIRACOMPRA", 15.0);
```

### Rastreamento de Pedidos

```
Pendente → Confirmado → Preparando → Pronto → Em Entrega → Entregue
   ↓
Cancelado (em qualquer etapa)
```

---

## 📊 Dados de Teste

### Clientes Pré-cadastrados

| Email | Senha | Nome |
|-------|-------|------|
| jp@gmail.com | 123 | João Pedro |

**Para criar novo cliente:** Menu → Opção 2 (Criar Conta)

### Restaurantes Pré-cadastrados

| Email | Senha | Nome | CNPJ |
|-------|-------|------|------|
| pizzaria@ifome.com | 123 | Pizzaria Italiana | 12345678000199 |
| burger@ifome.com | 123 | Burger House | 98765432000188 |
| sushi@ifome.com | 123 | Sushi Master | 11122233000144 |

### Produtos de Exemplo

**Pizzaria Italiana:**
- Pizza Margherita - R$ 45,90
- Pizza Calabresa - R$ 48,90
- Coca-Cola 350ml - R$ 6,00
- Petit Gateau - R$ 18,90

**Burger House:**
- X-Burger - R$ 22,90
- X-Bacon - R$ 26,90
- Suco Natural 500ml - R$ 8,00

**Sushi Master:**
- Combo Sashimi - R$ 65,90
- Temaki Salmão - R$ 28,90

---

## ⚠️ Tratamento de Erros

### Exceções Customizadas

| Exceção | Cenário |
|---------|---------|
| `PagamentoRecusadoException` | Pagamento falha na validação |
| `RestauranteFechadoException` | Tenta fazer pedido em restaurante fechado |
| `ProdutoIndisponivelException` | Produto selecionado não está disponível |
| `ValorMinimoException` | Valor do pedido menor que R$ 15,00 |
| `EnderecoForaRaioException` | Endereço fora da área de entrega |

### Validações Implementadas

```java
// CEP: formato brasileiro (12345-678 ou 12345678)
String cep = "12345-678";  // ✓ Válido

// CNPJ: 14 dígitos
String cnpj = "12345678000199";  // ✓ Válido

// Email: contém @ e .
String email = "usuario@ifome.com";  // ✓ Válido

// Telefone: mínimo 10 dígitos
String telefone = "(11) 9 9999-9999";  // ✓ Válido

// Nota: 1-5 estrelas
int nota = 4;  // ✓ Válido
```

---

## 🔒 Segurança

- ✅ Senhas armazenadas em arquivo (não criptografadas em v1.0)
- ✅ Validação de email com `@` e `.`
- ✅ Validação de CNPJ com 14 dígitos
- ✅ Validação de cartão (16 dígitos)
- ✅ CVV mascarado na exibição
- ✅ Transações validadas antes de confirmar

**Recomendações para produção:**
- Implementar hash (bcrypt, argon2) para senhas
- Usar banco de dados em vez de arquivos
- Adicionar autenticação JWT/OAuth
- Implementar HTTPS
- Validar endereço com API de geolocalização

---

## 🧪 Testes

### Executar Testes Unitários

```bash
# Windows
gradlew.bat test

# Linux/macOS
./gradlew test
```

### Cenários de Teste Recomendados

1. **Cadastro e Login**
   - Criar conta com dados válidos
   - Tentar login com senha errada
   - Email duplicado

2. **Carrinho**
   - Adicionar mesmo produto 2x (incremente quantidade)
   - Remover item do carrinho
   - Tentar confirmar carrinho vazio

3. **Pedido**
   - Pedido com valor mínimo atingido
   - Pedido com valor mínimo não atingido
   - Pedido em restaurante fechado

4. **Pagamento**
   - PIX (simulado)
   - Cartão com CVV inválido
   - Dinheiro com valor insuficiente

5. **Avaliação**
   - Avaliar pedido entregue
   - Tentar avaliar pedido pendente

---

## 📈 Melhorias Futuras

### Versão 2.0
- [ ] Interface gráfica (JavaFX)
- [ ] Banco de dados (PostgreSQL/MySQL)
- [ ] Sistema de recomendações
- [ ] Raio de entrega com mapa
- [ ] Notificações por email/SMS
- [ ] Integração com gateway de pagamento real
- [ ] Dashboard com estatísticas
- [ ] Pedidos agendados para depois
- [ ] Sistema de entregadores
- [ ] Chat entre cliente e restaurante

### Segurança
- [ ] Hash de senhas (bcrypt)
- [ ] Autenticação com JWT
- [ ] Rate limiting
- [ ] Validação de CEP com API de geolocalização

### Performance
- [ ] Cache de restaurantes
- [ ] Índices em banco de dados
- [ ] Paginação de pedidos
- [ ] Compressão de dados

---

## 📝 Sobre o Projeto

Este projeto foi desenvolvido como trabalho final da disciplina **MC322 - Programação Orientada a Objetos** da Universidade Estadual de Campinas (Unicamp).

### Objetivos de Aprendizado

✅ Aplicar conceitos de POO (herança, polimorfismo, encapsulamento)  
✅ Utilizar padrões de design (Singleton, Factory)  
✅ Implementar persistência de dados  
✅ Criar interfaces bem definidas  
✅ Tratar exceções apropriadamente  
✅ Trabalhar em equipe com controle de versão  

---

## 👥 Autores

- **Rafael Rodrigues Pimentel de Melo**
- **Matheus Boazão Silveira**

---

## 📄 Licença

Este projeto é fornecido como trabalho acadêmico e pode ser usado livremente para fins educacionais.

---

## 📞 Suporte

### Problemas Comuns

**P: "Erro ao compilar - Java not found"**  
R: Instale Java 11+ e adicione ao PATH

**P: "Arquivo de dados não encontrado"**  
R: Crie a pasta `data/` na raiz do projeto

**P: "Acentuação quebrada no Windows"**  
R: O script detecta Windows e configura UTF-8 automaticamente

**P: "Gradle não funciona"**  
R: Use `./gradlew` (Linux/Mac) ou `gradlew.bat` (Windows)

---

## 🔗 Links Úteis

- [Java Documentation](https://docs.oracle.com/en/java/)
- [Gradle User Guide](https://docs.gradle.org/current/userguide/userguide.html)
- [Padrões de Design em Java](https://refactoring.guru/design-patterns/java)
- [Unicode em Java](https://docs.oracle.com/javase/tutorial/i18n/index.html)

---

**Última atualização:** Janeiro de 2025  
**Status:** ✅ Completo e testado