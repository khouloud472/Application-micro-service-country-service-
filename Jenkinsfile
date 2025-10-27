pipeline {
    agent any

    tools {
        maven 'Maven'
        jdk 'JDK17'
    }

    stages {
        stage('Checkout') {
            steps {
                git(
                    url: 'https://github.com/khouloud472/Application-micro-service-country-service-.git',
                    branch: 'main',
                    credentialsId: 'github-token'
                )
            }
        }

        stage('Build & Test') {
            steps {
                sh 'mvn clean package -Dmaven.test.failure.ignore=true'
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

        stage('Deploy WAR to Nexus') {
            steps {
                script {
                    def isSnapshot = sh(script: 'mvn help:evaluate -Dexpression=project.version -q -DforceStdout', returnStdout: true).trim().endsWith('-SNAPSHOT')

                    if (isSnapshot) {
                        sh '''
                            mvn deploy:deploy-file \
                            -Dfile=target/Reservationavion-0.0.1-SNAPSHOT.war \
                            -DgroupId=com.khouloud \
                            -DartifactId=Reservationavion \
                            -Dversion=0.0.1-SNAPSHOT \
                            -Dpackaging=war \
                            -DrepositoryId=nexus-snapshots \
                            -Durl=http://localhost:8081/repository/maven-snapshots/ \
                            -Dusername=admin \
                            -Dpassword=admin
                        '''
                    } else {
                        sh '''
                            mvn deploy:deploy-file \
                            -Dfile=target/Reservationavion-0.0.1.war \
                            -DgroupId=com.khouloud \
                            -DartifactId=Reservationavion \
                            -Dversion=0.0.1 \
                            -Dpackaging=war \
                            -DrepositoryId=nexus-releases \
                            -Durl=http://localhost:8081/repository/maven-releases/ \
                            -Dusername=admin \
                            -Dpassword=admin
                        '''
                    }
                }
            }
        }

        stage('Deploy WAR to Tomcat') {
            steps {
                script {
                    def TOMCAT_HOME = "/mnt/c/Users/khouloud/Downloads/apache-tomcat-11.0.13/apache-tomcat-11.0.13"
                    def WAR_FILE = "target/Reservationavion-0.0.1-SNAPSHOT.war"
                    def DEPLOY_WAR = "${TOMCAT_HOME}/webapps/Reservationavion.war"

                    sh """
                        echo "Déploiement du WAR dans Tomcat..."
                        if [ -d "${TOMCAT_HOME}/webapps/Reservationavion" ]; then
                            rm -rf "${TOMCAT_HOME}/webapps/Reservationavion"
                        fi
                        cp "${WAR_FILE}" "${DEPLOY_WAR}"
                    """

                    sh """
                        echo "Redémarrage de Tomcat..."
                        bash "${TOMCAT_HOME}/bin/shutdown.sh" || true
                        sleep 5
                        bash "${TOMCAT_HOME}/bin/startup.sh"
                    """
                }
            }
        }

        stage('Verify Deployment') {
            steps {
                script {
                    echo "Vérification du déploiement..."
                    sh 'sleep 10'
                    sh 'curl -I http://localhost:8888/Reservationavion/ || true'
                }
            }
        }
    } // <- Fin de stages
} // <- FIN pipeline
