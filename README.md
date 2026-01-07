# service-monitoring-broker

## Caso de uso para o Message Broker (Trabalho de Redes)

- O sistema é composto por 3 serviços
- Um Serviço de Pagamentos
- Um Serviço de Emails
- Um Serviço de Logs
- A comunicação entre os serviços ocorre de forma **assíncrona**, utilizando um **Message Broker via sockets**
- Cada serviço pode atuar como Producer**, Consumer ou ambos

---

## Serviços Envolvidos

### Serviço de Pagamentos
Responsável por:
- Simular a criação de pagamentos
- Publicar eventos de pagamento
- Enviar mensagens de log para o servidor de logs

Atua como:
- **Producer** da fila `email.service`
- **Producer** da fila `logg.server`

---

### Serviço de Emails
Responsável por:
- Consumir eventos de pagamento
- Enviar emails relacionados a pagamentos
- Publicar logs de suas operações

Atua como:
- Consumer da fila `email.service`
- Producer da fila `logg.server`

---

### Servidor de Logs
Responsável por:
- Consumir mensagens de log
- Persistir logs no MongoDB

Atua como:
- Consumer da fila `logg.server`

---

## Filas Utilizadas


-  `logg.server`: Centraliza logs de todos os serviços           
- `email.service`: Eventos relacionados a pagamentos (emails)    

---

## Funcionamento Geral

- Cada serviço inicia sua comunicação com o broker durante a inicialização da aplicação
- A inicialização e o encerramento de serviços são registrados como logs
- Os serviços se comunicam exclusivamente via mensagens que são distribuídas pelo broker, sem acoplamento direto

---

## Inicialização dos Serviços

### Startup

1. Ao iniciar, cada serviço:
    - Cria conexão socket com o Message Broker
    - Envia uma mensagem de log indicando inicialização
2. Essa mensagem é publicada na fila `logg.server`
3. O servidor de logs consome essa mensagem e a persiste no MongoDB

