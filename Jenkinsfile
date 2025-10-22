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
            sh """
            mvn sonar:sonar \
            -Dsonar.projectKey=country-service \
            -Dsonar.host.url=http://localhost:9000
            -Dsonar.login=b844cb4b71acdf0eae6b8fb47c1c015fd4ef20db \
            -Dsonar.sources=src/main \
            -Dsonar.java.binaries=target/classes
            """
        }
    }
}

    }
}
