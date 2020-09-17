FROM adoptopenjdk/openjdk11:x86_64-alpine-jdk-11.0.4_11 as packager
VOLUME /tmp

#quando for pelo eclipse @project.build.finalName@.jar
ARG JAR_FILE=target/cursomc-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]


# > docker image build -t image-pedidos-back .
# > docker run -it -p 8080:8080 pedidos-back



#criando a rede
# > docker network create network-mysql
#criando o container MySQL de uma imagem do dockerhub usando nossa rede
# > docker container run --name mysqldb --network network-mysql -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=bootdb -d mysql:8

# para rodar em bash
# > docker container exec -it mysqldb bash
#para logar
# > mysql -uroot -proot
#para logar
# mysql> show databases;
# mysql> use bootbd;
# mysql> show tables;

#criando a imagem do sistema - rodar na pasta deste Dockerfile
# > docker image build -t i-pedidos-back .
# criando o container apontando para a mesma rede
# > docker container run --network network-mysql --name c-pedidos-back -p 8080:8080 -d i-pedidos-back
# > docker container logs -f c-pedidos-back 

# Apagando
# > docker container stop c-pedidos-back
# > docker container rm c-pedidos-back
# > docker image rm i-pedidos-back
# > docker container stop mysqldb
# > docker container rm mysqldb