FROM openjdk:8u252

LABEL author="ifedun.jeremiah@gmail.com"

EXPOSE 8080

# Install and setup
COPY install.sh /root/tima-auth-service/install.sh
COPY setup.sh /root/tima-auth-service/setup.sh
COPY .env /root/tima-auth-service/.env


RUN chmod +x /root/tima-auth-service/setup.sh

RUN /root/tima-auth-service/setup.sh

ADD target/tima-auth-service-*.jar /opt/tima-auth-service/tima-auth-service-*.jar
WORKDIR /opt/tima-auth-service

RUN chmod +x /root/tima-auth-service/install.sh
CMD  /root/tima-auth-service/install.sh