FROM ubuntu:latest
ENV TZ=Europe/Rome
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
RUN apt-get update
RUN apt-get -y install git
RUN apt-get -y install default-jdk 
RUN apt-get -y install default-jre
RUN apt-get -y install maven 
COPY entrypoint.sh /entrypoint.sh
ENTRYPOINT /entrypoint.sh
EXPOSE 6969
