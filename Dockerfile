FROM openjdk:8u252

LABEL author="ifedun.jeremiah@gmail.com"

EXPOSE 8080

# Install and setup
COPY install.sh /root/tima-service/install.sh
COPY setup.sh /root/tima-service/setup.sh
COPY .env /root/tima-service/.env


RUN chmod +x /root/tima-service/setup.sh

RUN /root/tima-service/setup.sh

ADD target/tima-service-*.jar /opt/tima-service/tima-service-*.jar
WORKDIR /opt/tima-service

RUN chmod +x /root/tima-service/install.sh
CMD  /root/tima-service/install.sh