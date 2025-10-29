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

        stage('Build Docker Image') {
            steps {
                sh "docker build . -t ${IMAGE_NAME}:${BUILD_NUMBER}"
            }
        }

        stage('Push Docker Image to Hub') {
            steps {
                withCredentials([string(credentialsId: "${DOCKERHUB_CREDENTIALS}", variable: 'DOCKERHUB_PWD')]) {
                    sh "echo ${DOCKERHUB_PWD} | docker login -u ${DOCKERHUB_USER} --password-stdin"
                    sh "docker tag ${IMAGE_NAME}:${BUILD_NUMBER} ${DOCKERHUB_USER}/${IMAGE_NAME}:${BUILD_NUMBER}"
                    sh "docker push ${DOCKERHUB_USER}/${IMAGE_NAME}:${BUILD_NUMBER}"
                }
            }
        }

        stage('Deploy Micro-Service via Docker') {
            steps {
                // Supprime tous les conteneurs existants (optionnel)
                sh "docker rm -f \$(docker ps -aq) || true"

                // Lancer le conteneur avec le port 8086
                sh "docker run -d -p 8086:8080 --name ${IMAGE_NAME}-${BUILD_NUMBER} ${DOCKERHUB_USER}/${IMAGE_NAME}:${BUILD_NUMBER}"
            }
        }

        stage('Verify Deployment') {
            steps {
                echo "Vérification du service..."
                sh 'sleep 10' // attendre que le conteneur démarre
                sh 'curl -I http://localhost:8086/api/countries || true'
            }
        }


        stage('Deploy WAR to Nexus') {
            steps {
                script {
                    def isSnapshot = sh(
                        script: 'mvn help:evaluate -Dexpression=project.version -q -DforceStdout', 
                        returnStdout: true
                    ).trim().endsWith('-SNAPSHOT')

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

        stage('Deploy WAR from Nexus to Tomcat') {
            steps {
                script {
                    def TOMCAT_HOME = "/mnt/c/Users/khouloud/Downloads/apache-tomcat-11.0.13/apache-tomcat-11.0.13"
                    def WAR_NAME = "Reservationavion.war"
                    def NEXUS_URL = "http://admin:admin@localhost:8081/repository/maven-snapshots/com/khouloud/Reservationavion/0.0.1-SNAPSHOT/Reservationavion-0.0.1-20251027.044043-1.war"

                    // Télécharger le WAR depuis Nexus
                    sh """
                        echo " Téléchargement du WAR depuis Nexus..."
                        curl -f -L -o "${TOMCAT_HOME}/webapps/${WAR_NAME}" "${NEXUS_URL}"
                    """

                    // Redémarrage de Tomcatcom/khouloud/Reservationavion/0.0.1-SNAPSHOT/Reservationavion-0.0.1-20251027.044043-1.war
                    sh """
                        echo " Redémarrage de Tomcat..."
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
                    echo " Vérification du déploiement..."
                    sh 'sleep 10' // Attendre que Tomcat démarre
                    sh 'curl -I http://localhost:8888/Reservationavion/ || true'
                }
            }
        }
    } // Fin de stages
} // Fin du pipeline
