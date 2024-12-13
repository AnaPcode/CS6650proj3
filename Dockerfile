
FROM bellsoft/liberica-openjdk-alpine-musl:11 AS server-build
COPY . /usr/src
WORKDIR /usr/src
RUN javac *.java
CMD ["java", "Server", "32000"]


FROM bellsoft/liberica-openjdk-alpine-musl:11 AS client-build
COPY . /usr/src
WORKDIR /usr/src
RUN javac *.java
CMD ["java", "Client", "keyValServer", "32000"]