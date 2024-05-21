#Docker 镜像构建
#@author: hailong
FROM alpine:3.19.1 as builder

#添加说明等信息
LABEL name=bkp description="jdk21 base on alpine image"

#安装jdk
RUN apk add --no-cache --no-cache openjdk21

#安装maven
#RUN apk add --no-cache --no-cache maven
#COPY settings.xml /usr/share/java/maven-3/conf/settings.xml

# Copy local code to the container image.
ADD api-gateway-0.0.1-SNAPSHOT.jar app.jar


# Run the web service on container startup.
CMD ["java","-jar","/app.jar","--spring.profiles.active=prod"]