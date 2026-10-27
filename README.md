# Sistema de Cadastro e Gerenciamento de Produtos

Projeto acadêmico em Java Swing com Maven.

## Requisitos
- JDK 17 ou superior
- Apache NetBeans com suporte a projetos Maven

## Como abrir no NetBeans
1. Extraia o arquivo ZIP.
2. No NetBeans, escolha **File > Open Project**.
3. Selecione a pasta `SistemaCadastroProdutos` (a pasta que contém `pom.xml`).
4. Aguarde o Maven carregar o projeto.
5. Execute a classe `br.com.sistemaprodutos.Main`.

## Funcionalidades
- Cadastro de produtos com código único.
- Consulta por código.
- Listagem em tabela.
- Atualização de nome, descrição e preço.
- Exclusão com confirmação.
- Entrada e retirada de estoque com validação.
- Cálculo do valor do estoque de cada produto e do valor total.
- Validações para campos obrigatórios, preços negativos, quantidades negativas e estoque insuficiente.

## Estrutura
```text
SistemaCadastroProdutos/
├── pom.xml
├── README.md
└── src/
    └── main/
        └── java/
            └── br/
                └── com/
                    └── sistemaprodutos/
                        ├── Main.java
                        ├── Produto.java
                        ├── CadastroProdutos.java
                        └── TelaProdutos.java
```

## Roteiro sugerido para o vídeo (até 5 minutos)
1. Mostrar a estrutura das classes e explicar o papel de cada uma.
2. Cadastrar cinco produtos com códigos diferentes.
3. Tentar cadastrar um código repetido e mostrar a validação.
4. Consultar e atualizar um produto.
5. Registrar uma entrada e uma retirada; testar uma retirada maior que o estoque.
6. Excluir um produto.
7. Mostrar o valor total do estoque e explicar encapsulamento, construtor, `ArrayList`, objetos e eventos Swing.

**Observação:** os dados ficam em memória e são perdidos quando o programa é fechado. Persistência em arquivo ou banco de dados não foi solicitada na atividade.
