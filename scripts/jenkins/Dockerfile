FROM jenkins/jenkins:lts

USER root

# Instalação do Docker e do Tini
RUN apt update
RUN apt install -y apt-transport-https ca-certificates curl software-properties-common
RUN curl -fsSL https://download.docker.com/linux/ubuntu/gpg | gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg

RUN echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu xenial stable" | tee /etc/apt/sources.list.d/docker.list > /dev/null
RUN apt update
RUN apt-cache policy docker-ce

RUN apt install -y docker-ce

# Adiciona o usuário jenkins ao grupo docker se o grupo não existir
RUN groupadd -g 1001 docker || true
RUN usermod -aG docker jenkins || true

# Configuração do Jenkins
USER jenkins
