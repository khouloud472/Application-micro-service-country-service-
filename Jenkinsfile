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

          stage('Deploy WAR to Tomcat') {
            steps {
                script {
                    // Définir le chemin Tomcat et le WAR
                    def TOMCAT_HOME = "/mnt/c/Users/khouloud/Downloads/apache-tomcat-11.0.13/apache-tomcat-11.0.13"
                    def WAR_FILE = "target/Reservationavion-0.0.1-SNAPSHOT.war"
                    def DEPLOY_WAR = "${TOMCAT_HOME}/webapps/Reservationavion.war"

                    // Supprimer ancienne version et copier le nouveau WAR
                    sh """
                        rm -rf ${TOMCAT_HOME}/webapps/Reservationavion
                        cp ${WAR_FILE} ${DEPLOY_WAR}
                    """

                    // Redémarrer Tomcat
                    sh """
                        ${TOMCAT_HOME}/bin/shutdown.sh || true
                        sleep 5
                        ${TOMCAT_HOME}/bin/startup.sh
                    """
                }
            }
        }

        stage('Verify Deployment') {
            steps {
                // Tester l'accès à l'application
                sh 'curl -I http://localhost:8888/Reservationavion/'
            }
        }
    }
}