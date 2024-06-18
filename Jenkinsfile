pipeline {
    agent any
    environment {
        JAVA_HOME = '/opt/java/openjdk'
        PATH = "${env.JAVA_HOME}/bin:${env.PATH}"
    }
    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/FelipeFelipeRenan/spring-ops.git', branch: 'main'
            }
        }
        stage('Build') {
            steps {
                dir('order-service') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }
        stage('Build Docker Image') {
            steps {
                dir('order-service') {
                    script {
                        docker.build("order-service:${env.BUILD_ID}")
                    }
                }
            }
        }
        stage('Push Docker Image') {
            steps {
                dir('order-service') {
                    script {
                        docker.withRegistry('https://index.docker.io/v1/', 'dockerhub-credentials') {
                            docker.image("order-service:${env.BUILD_ID}").push("latest")
                        }
                    }
                }
            }
        }
        stage('Deploy') {
            steps {
                dir('order-service') {
                    sh 'docker-compose down'
                    sh 'docker-compose up -d'
                }
            }
        }
    }
}
