# Back-end GProJurídico

Este repositório contém o back-end do sistema **GProJurídico**, um sistema **eficiente** para gestão e controle de **processos** e **atendimentos jurídicos**, destinado a uma organização que presta atendimentos às pessoas de baixa renda.

As tecnologias envolvidas são: **Java**, **Spring Boot**, **Spring Security**, **JWT** (JSON Web Token), **Firestore do GCP** (Google Cloud Platform), **Junit**, **Mockito** e **Swagger**.

As versões das tecnologias acima encontram-se no arquivo **pom.xml**.

## Sumário

- [Instalação](#instalação)
- [Uso](#uso)
- [Endpoints da API](#endpoints-da-api)
  - [Login e Autenticação](#login-e-autenticação)
- [Autorização](#autorização)
- [Mais sobre o Projeto](#mais-sobre-o-projeto)

## Instalação

1. Clone o repositório:

   1. Via HTTPS
      ```bash
      git clone https://github.com/fabuniprojecaotag/npj-backend.git
      ```
   
   2. Ou via SSH
        ```bash
        git clone git@github.com:fabuniprojecaotag/npj-backend.git
        ```

2. Instale as dependências com o Maven.

3. Instale e faça o login no [gcloud-cli](https://cloud.google.com/docs/authentication/provide-credentials-adc#google-idp).

   1. Ao fornecer suas **credenciais de usuário**, você está configurando o **ADC** (Application Default Credential) para ser autenticar seu usuário e interagir com o Firestore e outros produtos do GCP que o projeto faz uso;
   2. Certifique-se que você autenticou usando as **credenciais de usuário** em **ambiente de desenvolvimento local** do projeto, conforme o [link](https://cloud.google.com/docs/authentication/provide-credentials-adc#google-idp) acima;
   3. **OBS**: É necessário que algum proprietário do projeto adicione o seu usuário do Google para ter acesso ao projeto, tanto no **CLI** quanto no **console web do Firebase**.

## Uso

1. Inicie a aplicação com o Maven.
2. Realize o login (de algum usuário cadastro) para acessar os endpoints da aplicação.

## Endpoints da API

O sistema de back-end disponibiliza uma API REST para acesso aos recursos da aplicação por meio de endpoints. Os seguintes endpoints são fornecidos pela API:

```markdown
POST /login → Realiza uma ação de login ao sistema

(GET | POST | PUT | DELETE) /(usuários | assistidos | atendimentos | processos)/[id] → Realiza alguma ação desejada, expressa pelo verbo HTTP usado, ao recurso alvo.
```

Dicionário de nomenclaturas usadas: 

- Palavras entre **( )** são **obrigatórias** serem usadas
- Palavras entre **[ ]** são **opcionais** de serem usadas
- Uso de **|** , **pipe** em inglês, indica sentença **OU**. Ou seja, se houver duas ou mais palavras dentro de **( )** ou **[ ]** que usam o **|** , _apenas_ **uma** palavra poderá ser usada. 

### Login e Autenticação

Para realizar o login com algum usuário, é necessário que este esteja cadastrado previamente por algum usuário com permissão adequada. O payload da requisição deve seguir este formato:

````JSON
{
  "login": "******",
  "password": "*******"
}
````

Após o sistema **validar** o login, o usuário estará **autenticado** e receberá um **token de acesso**, em formato **JWT**, para acessar os recursos da aplicação. O acesso a esses endpoints _deve_ ser feita via **Bearer Token**.

## Autorização

Dado o login efetuado com sucesso, o usuário precisará ter a **role** necessária para acessar alguns **recursos protegidos**.

A API usa o **Spring Security** para o controle de autenticação. As seguintes roles estão disponíveis:

````
ESTAGIARIO → Possui acesso para executar GET em todos os endpoints
PROFESSOR → possui acesso para executar GET e POST em todos e um endpoint, respectivamente
SECRETARIA → possui acesso para executar GET, POST e PUT em todos os endpoints
COORDENADOR → Possui acesso total
````

## Mais sobre o Projeto

O sistema **GProJurídico** é fruto de um trabalho realizado pelo time da **Fábrica de Software** do **UniProjeção** de Taguatinga. 

Com o intuito de permitir que um **NPJ** (Núcleo de Práticas Jurídicas) vinculado à universidade tenha uma **gestão e controle eficientes dos atendimentos** que este presta para **comunidade de baixa de renda** e realiza entre os **estudantes e professores de Direito**, foi criado este projeto de software.

O trabalho encontra-se, atualmente, **em desenvolvimento**, segmentado entre **Back-end** e **Front-end** pela equipe e possui a previsão de ter a versão **1.0** lançada até o **final do 1º semestre de 2024**.
