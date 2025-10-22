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
            -Dsonar.login=sqp_773b26420a1222a5352983e8ad2309dad65b7730 \
            -Dsonar.sources=src/main \
            -Dsonar.java.binaries=target/classes
            """
        }
    }
}

    }
}
