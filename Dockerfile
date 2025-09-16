# 1. Imagem base Java 21
FROM eclipse-temurin:21-jdk-jammy

# 2. Cria diretório do app
WORKDIR /app

# 3. Copia o JAR gerado pelo Maven/Gradle
COPY build/libs/habitus-0.0.1-SNAPSHOT.jar app.jar


# 4. Expõe a porta que sua API vai rodar
EXPOSE 8080

# 5. Comando para rodar o app
ENTRYPOINT ["java", "-jar", "app.jar"]
