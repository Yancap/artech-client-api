# Artech Client API

O Artech Client API é um microsserviço back-end desenvolvido com Quarkus, responsável pela disponibilização dos conteúdos publicados da plataforma Artech para o Artech Web Client, além do gerenciamento de acesso dos usuários clientes da aplicação.

Este serviço atua de forma independente do Artech CMS API, separando claramente as responsabilidades entre os usuários administrativos (administradores e autores, gerenciados pelo CMS) e os usuários clientes, que consomem os conteúdos publicados. Essa separação garante maior segurança, organização do domínio e escalabilidade da arquitetura como um todo.

O Artech Client API expõe endpoints REST otimizados para consumo público e leitura de dados, focando na entrega eficiente de artigos e informações relacionadas, bem como no controle de acesso e autenticação dos usuários clientes. As regras de negócio relacionadas à publicação, visibilidade e acesso ao conteúdo são centralizadas neste microsserviço, evitando acoplamento direto com o CMS.

Desenvolvido com Quarkus, o projeto se beneficia de alta performance, rápida inicialização e baixo consumo de recursos, sendo adequado para ambientes cloud-native e arquiteturas baseadas em microsserviços. A aplicação foi estruturada para permitir fácil evolução, incluindo funcionalidades como autenticação, autorização, controle de sessões, cache, versionamento de API e integração com outros serviços da plataforma.

Do ponto de vista técnico, o projeto aborda:

- Desenvolvimento de APIs REST com Quarkus
- Arquitetura de microsserviços com separação clara de responsabilidades
- Gerenciamento de usuários clientes e controle de acesso
- Exposição segura e eficiente de conteúdo para aplicações front-end
- Preparação para ambientes containerizados e deploy em nuvem

Este repositório representa o microsserviço de acesso público da plataforma Artech, sendo responsável pela entrega de conteúdo ao cliente final e pela gestão dos usuários consumidores, complementando o CMS e garantindo uma arquitetura bem definida e escalável.
