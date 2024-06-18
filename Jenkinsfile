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

        stage('Build Docker Image') {
            steps {
                script {
                    def image = docker.build("order-service:${env.BUILD_ID}", "order-service/.")
                }
            }
        }
        stage('Push Docker Image') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', 'dockerhub-credentials') {
                        def image = docker.image("order-service:${env.BUILD_ID}")
                        image.push("latest")
                    }
                }
            }
        }
        stage('Deploy') {
            steps {
                script {
                    sh 'docker-compose down'
                    sh 'docker-compose up -d'
                }
            }
        }
    }
}
