pipeline {
    agent any
    tools {
        maven 'Maven'
        jdk 'JDK17'
    }

    stages {
        stage('Checkout') { 
            steps { 
                git url: 'https://github.com/khouloud472/Application-micro-service-country-service-.git', branch: 'main', credentialsId: 'github-token' 
            } 
        }

        stage('Build') {
            steps {
                    sh 'mvn clean package -Dmaven.test.failure.ignore=true'
            }
        }


        stage('SonarQube Analysis') {
    steps {
        withSonarQubeEnv('SonarQubeScanner') {
            
            sh "${mvn}/bin/mvn clean verify sonar:sonar -Dsonar.projectKey=Application-micro-service-country-service -Dsonar.projectName='Application-micro-service-country-service'"
        }
    }
}

    }
}
