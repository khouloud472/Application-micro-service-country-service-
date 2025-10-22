pipeline {
    agent any
    tools {
        maven 'Maven'
        jdk 'JDK17'
    }

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/khouloud472/Application-micro-service-country-service-.git',
                    branch: 'main',
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
                withSonarQubeEnv('SonarQubeScanner') {
                    withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
                        script {
                            def mvnHome = tool name: 'Maven', type: 'maven'
                            sh """
                                ${mvnHome}/bin/mvn sonar:sonar \
                                -Dsonar.projectKey=Application-micro-service-country-service \
                                -Dsonar.projectName='Application-micro-service-country-service' \
                                -Dsonar.token=${SONAR_TOKEN}
                            """
                        }
                    }
                }
            }
        }

         stage('Build & Deploy to Nexus') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'nexus-credentials', usernameVariable: 'NEXUS_USERNAME', passwordVariable: 'NEXUS_PASSWORD')]) {
                    sh "mvn clean deploy"
                }
            }
        }
    }
}
