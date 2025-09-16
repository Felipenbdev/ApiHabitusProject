# 1️⃣ Imagem base Java 21
FROM eclipse-temurin:21-jdk-jammy

# 2️⃣ Cria diretório do app
WORKDIR /app

# 3️⃣ Copia 
COPY . .

# 4️⃣ Dá permissão de execução ao Gradle Wrapper
RUN chmod +x gradlew

# 5️⃣ Builda o JAR dentro do container (ignora testes)
RUN ./gradlew build -x test

# 6️⃣ Copia o JAR para rodar
RUN cp build/libs/habitus-0.0.1-SNAPSHOT.jar app.jar

# 7️⃣ Expõe a porta
EXPOSE 8080

# 8️⃣ Comando para rodar a API
ENTRYPOINT ["java", "-jar", "app.jar"]
