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

        stage('Compile, Test, Package') {
            steps {
                sh 'mvn clean install'
            }
            post {
                success {
                    junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQubeScanner') {
                    script {
                        def mvnHome = tool name: 'Maven', type: 'maven'
                        sh """
                            ${mvnHome}/bin/mvn sonar:sonar \
                            -Dsonar.projectKey=Application-micro-service-country-service \
                            -Dsonar.projectName='Application-micro-service-country-service'
                        """
                    }
                }
            }
        }
    }
}
