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
                withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
                    sh """
                        mvn sonar:sonar \
                        -Dsonar.projectKey=country-service \
                        -Dsonar.host.url=http://localhost:9000 \
                        -Dsonar.login=${SONAR_TOKEN}
                    """
                }
            }
        }

        stage('Deploy to Nexus') {
            steps {
                script {
                    // Check if this is a SNAPSHOT version
                    def isSnapshot = sh(script: 'mvn help:evaluate -Dexpression=project.version -q -DforceStdout', returnStdout: true).trim().endsWith('-SNAPSHOT')
                    
                    if (isSnapshot) {
                        sh 'mvn deploy -Dnexus.username=admin -Dnexus.password=admin -DaltSnapshotDeploymentRepository=nexus-snapshots::default::http://localhost:8081/repository/maven-snapshots/'
                    } else {
                        sh 'mvn deploy -Dnexus.username=admin -Dnexus.password=admin -DaltReleaseDeploymentRepository=nexus-releases::default::http://localhost:8081/repository/maven-releases/'
                    }
                }
            }
        }

        stage('Deploy to Tomcat') {
            steps {
                sh '''
                # Définir le chemin vers Tomcat
                TOMCAT_HOME=/opt/tomcat
                
                # Copier le .war
                cp target/Reservationavion-*.war $TOMCAT_HOME/webapps/
                
                # Redémarrer Tomcat pour prendre en compte le nouveau .war
                $TOMCAT_HOME/bin/shutdown.sh || true
                sleep 5
                $TOMCAT_HOME/bin/startup.sh
                '''
            }
        }
    }
}