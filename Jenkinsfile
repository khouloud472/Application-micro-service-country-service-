pipeline {
    agent any
    tools {
        maven 'Maven'
        jdk 'JDK17'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/khouloud472/Application-micro-service-country-service-.git',
                    credentialsId: 'github-token'
            }
        }

        stage('Build') {
            steps {
                script {
                    try {
                        sh 'mvn clean compile'
                    } catch (err) {
                        echo "❌ Erreur de compilation : ${err}"
                        error("Build failed")
                    }
                }
            }
        }

      stage('SonarQube Analysis') {
    steps {
        withSonarQubeEnv('SonarQubeScanner') {
            sh "mvn sonar:sonar -Dsonar.projectKey=country-service -Dsonar.sources=src/main -Dsonar.java.binaries=target/classes"
        }
    }
}


        
        stage('Test') {
            steps {
                script {
                    try {
                        sh 'mvn test'
                    } catch (err) {
                        echo "⚠️ Certains tests ont échoué, mais on continue..."
                        // Le build continue même si des tests échouent
                    }
                }
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml' // Publication des résultats des tests
                }
            }
        }
    } // fin de stages
} // fin du pipeline
