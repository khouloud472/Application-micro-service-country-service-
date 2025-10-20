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

        stage('Build') { 
            steps { 
                sh 'mvn clean compile' 
            } 
        }

        stage('Test') {
            steps { 
                sh 'mvn test' 
            }
            post { 
                always { 
                    junit 'target/surefire-reports/*.xml' 
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

        stage('Package') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }

        stage('Publish to Nexus') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'nexus-credentials', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASS')]) {
                    sh "mvn deploy -DskipTests -Dnexus.username=${NEXUS_USER} -Dnexus.password=${NEXUS_PASS}"
                }
            }
        }

        stage('Deploy to Tomcat') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'tomcat-credentials', usernameVariable: 'TOMCAT_USER', passwordVariable: 'TOMCAT_PASS')]) {
                    sh """
                        curl -u $TOMCAT_USER:$TOMCAT_PASS \
                        -T target/Reservationavion-0.0.1-SNAPSHOT.war \
                        'http://MON_TOMCAT:8080/manager/text/deploy?path=/monapp&update=true'
                    """
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'target/*.war', allowEmptyArchive: true
            publishHTML([
                allowMissing: false,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'target/site/jacoco',
                reportFiles: 'index.html',
                reportName: 'Code Coverage Report'
            ])
        }
    }
}
