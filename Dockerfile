# Utiliser une image Java 17 officielle et maintenue
FROM eclipse-temurin:17-jdk

# Créer un volume temporaire
VOLUME /tmp

# Copier le fichier WAR généré par Maven
COPY target/*.war app.war

# Définir le point d'entrée
ENTRYPOINT ["java", "-jar", "/app.war"]
