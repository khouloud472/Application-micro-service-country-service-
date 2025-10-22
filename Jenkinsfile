pipeline {
    agent any
    tools {
        maven 'Maven'
        jdk 'JDK17'
    }

    environment {
        SONAR_PROJECT_KEY = 'country-service'
        SONAR_SOURCES = 'src/main'
        SONAR_BINARIES = 'target/classes'
        GIT_REPO = 'https://github.com/khouloud472/Application-micro-service-country-service-.git'
        GIT_BRANCH = 'main'
    }

    options {
        timeout(time: 30, unit: 'MINUTES')
        timestamps()          // Valide dans options
    }

    stages {
        stage('Checkout') {
            steps {
                ansiColor('xterm') {
                    git url: env.GIT_REPO, branch: env.GIT_BRANCH, credentialsId: env.GIT_CREDENTIALS
                }
            }
        }

        stage('Build') {
            steps {
                ansiColor('xterm') {
                    sh 'mvn clean package -Dmaven.test.failure.ignore=true'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                ansiColor('xterm') {
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
        }

        stage('Quality Gate') {
            steps {
                ansiColor('xterm') {
                    timeout(time: 5, unit: 'MINUTES') {
                        waitForQualityGate abortPipeline: true
                    }
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
            cleanWs()
        }
    }
}
