# Dockerfile para Jenkins com JDK 21 e Maven
FROM jenkins/jenkins:lts

USER root

# Instalar JDK 21
RUN apt-get update && \
    apt-get install -y openjdk-21-jdk

# Definir JAVA_HOME
ENV JAVA_HOME /usr/lib/jvm/java-21-openjdk-amd64
ENV PATH $JAVA_HOME/bin:$PATH

# Instalar Maven
RUN apt-get install -y maven

# Voltar ao usuário Jenkins
USER jenkins
