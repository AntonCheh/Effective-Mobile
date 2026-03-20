# Базовый образ с Maven и Java 17 для сборки
FROM maven:3.9-eclipse-temurin-17 AS builder

# Копируем настройки Maven
COPY settings.xml /root/.m2/settings.xml

WORKDIR /app

# Копируем pom.xml и скачиваем зависимости
COPY pom.xml .
RUN mvn dependency:go-offline

# Копируем исходники и компилируем
COPY src ./src
RUN mvn compile

# Второй этап - финальный образ с браузером
FROM maven:3.9-eclipse-temurin-17

# Устанавливаем Chrome и необходимые зависимости
RUN apt-get update && apt-get install -y \
    wget \
    gnupg2 \
    unzip \
    curl \
    && wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add - \
    && echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google.list \
    && apt-get update && apt-get install -y google-chrome-stable \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Копируем settings.xml в финальный образ
COPY settings.xml /root/.m2/settings.xml

# Включаем headless режим для Docker
ENV MAVEN_OPTS="-Dheadless=true"
ENV DOCKER_ENV=true

WORKDIR /tests

# Копируем собранные классы и зависимости
COPY --from=builder /app/target /tests/target
COPY --from=builder /root/.m2/repository /tests/repository
COPY --from=builder /app/pom.xml .
COPY --from=builder /app/src ./src

# Запускаем тесты
CMD ["mvn", "test"]