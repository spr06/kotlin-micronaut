FROM gcr.io/distroless/java11-debian11
COPY app/build/libs/app-*-all.jar app.jar
CMD ["-Xms512M", "-Xmx512M", "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=100", "app.jar"]