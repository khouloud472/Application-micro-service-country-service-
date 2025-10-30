pipeline {
    agent any

    tools {
        maven 'Maven'
        jdk 'JDK17'
    }

    environment {
        IMAGE_NAME = "my-country-service"
        DOCKERHUB_USER = "khouloudchrif"
        DOCKERHUB_CREDENTIALS = "dockerhub-pwd"
    }

    stages {

        stage('Checkout') {
            steps {
                git(
                    url: 'https://github.com/khouloud472/Application-micro-service-country-service-.git',
                    branch: 'main',
                    credentialsId: 'github-token'
                )
            }
        }

        stage('Build & Test') {
            steps {
                sh 'mvn clean package -Dmaven.test.failure.ignore=true'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${IMAGE_NAME}:v9 ."
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    
                    sh 'kubectl get pods'
                }
            }
        }
    } // Fin stages
} // Fin pipeline

