pipeline {
    agent any
    tools {
        maven 'Maven'
        jdk 'JDK17'
    }

    environment {
        MAVEN_OPTS = '-Xmx1024m'
        SONAR_PROJECT_KEY = 'country-service'
        SONAR_SOURCES = 'src/main'
        SONAR_BINARIES = 'target/classes'
        GIT_REPO = 'https://github.com/khouloud472/Application-micro-service-country-service-.git'
        GIT_BRANCH = 'main'
        GIT_CREDENTIALS = 'github-token'
    }

    options {
        timeout(time: 30, unit: 'MINUTES')  // Timeout global pour éviter les builds bloqués
        ansiColor('xterm')                  // Couleurs dans la console
        timestamps()                         // Ajout de timestamps dans les logs
    }

    stages {

        stage('Checkout') {
            steps {
                git url: env.GIT_REPO, branch: env.GIT_BRANCH, credentialsId: env.GIT_CREDENTIALS
            }
        }

        stage('Build') {
            steps {
                echo 'Building the project...'
                sh 'mvn clean package -Dmaven.test.failure.ignore=true'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo 'Running SonarQube analysis...'
                withSonarQubeEnv('SonarQubeScanner') {
                    sh """
                        mvn sonar:sonar \
                        -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                        -Dsonar.sources=${SONAR_SOURCES} \
                        -Dsonar.java.binaries=${SONAR_BINARIES}
                    """
                }
            }
        }

        stage('Quality Gate') {
            steps {
                echo 'Checking SonarQube Quality Gate...'
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

    }

    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed. Check the logs and fix the issues.'
        }
        always {
            cleanWs()  // Nettoie l’espace de travail à la fin
        }
    }
}
