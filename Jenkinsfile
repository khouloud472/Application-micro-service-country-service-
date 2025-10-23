pipeline {
    agent any

    tools {
        maven 'Maven'
        jdk 'JDK17'
    }

    environment {
        SONARQUBE_ENV = credentials('sonarqube-token') // si tu as un token SonarQube
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/khouloud472/Application-micro-service-country-service-.git',
                    credentialsId: 'github-token'
            }
        }

        stage('Build & Test') {
            steps {
                sh 'mvn clean install -Dmaven.test.failure.ignore=true'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withCredentials([string(credentialsId: 'sonarqube-token', variable: 'SONAR_TOKEN')]) {
                    sh """
                        mvn sonar:sonar \
                        -Dsonar.projectKey=country-service \
                        -Dsonar.host.url=http://localhost:9000 \
                        -Dsonar.login=$SONAR_TOKEN
                    """
                }
            }
        }

        stage('Build & Deploy to Nexus') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'nexus-credentials', usernameVariable: 'NEXUS_USERNAME', passwordVariable: 'NEXUS_PASSWORD')]) {
                    sh """
                        mvn deploy \
                        -Dnexus.username=${NEXUS_USERNAME} \
                        -Dnexus.password=${NEXUS_PASSWORD} \
                        -DskipTests
                    """
                }
            }
        }
    }

    post {
        success {
            echo '✅ Pipeline executed successfully — code built, tested, analyzed, and deployed to Nexus!'
        }
        failure {
            echo '❌ Pipeline failed — check build logs.'
        }
    }
}
