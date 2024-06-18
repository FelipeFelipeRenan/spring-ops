FROM jenkins/jenkins:jdk21

USER root

ENV JAVA_HOME /usr/lib/jvm/java-21-openjdk-amd64


ENV PATH $JAVA_HOME/bin:$PATH

RUN apt-get update && apt-get install -y maven

USER jenkins
