pipeline {
    agent any

    tools {
        maven 'Maven'
        jdk 'JDK17'
    }

    environment {
        IMAGE_NAME = "my-country-service"
        DOCKERHUB_USER = "khouloudchrif"
        DOCKERHUB_CREDENTIALS = "dockerhub-pwd"
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
/*
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

 stage('Test Docker') {
    steps {
        sh 'docker version'
        sh 'docker info'
    }
}
*/


stage('Build Docker Image') {
            steps {
                sh "docker build -t ${IMAGE_NAME}:v10 ."
            }
        }

stage('Push Docker Image to Hub') {
    steps {
        // Connexion à Docker Hub
        sh "docker login"
        
        // Tag et push de l'image
        sh "docker tag my-country-service:v10 khouloudchrif/my-country-service:v10"
        sh "docker push khouloudchrif/my-country-service:v10"
    }
}
stage('Deploy to Kubernetes') {
    steps {
        script {
            // Utilisation du kubeconfig stocké dans Jenkins Credentials
            withCredentials([file(credentialsId: 'kubeconfig-file', serverUrl: '')]) {
                // Appliquer les manifestes Kubernetes
                sh 'kubectl apply -f deployment.yaml'
                sh 'kubectl apply -f service.yaml'
            }
        }
    }
}


        stage('Verify Docker Deployment') {
            steps {
                echo "Vérification du service Docker..."
                sh 'sleep 10' // Attendre que le conteneur démarre
                // Vérification simple du service
                sh 'curl -I http://localhost:8086/countries || true'
            }
        }

    }
}
/*

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

                    sh """
                        echo "Téléchargement du WAR depuis Nexus..."
                        curl -f -L -o "${TOMCAT_HOME}/webapps/${WAR_NAME}" "${NEXUS_URL}"
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

        stage('Verify Tomcat Deployment') {
            steps {
                echo "Vérification du déploiement Tomcat..."
                sh 'sleep 10'
                sh 'curl -I http://localhost:8888/Reservationavion/ || true'
            }
        }

    } 
} 


*/

