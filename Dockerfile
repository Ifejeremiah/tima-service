FROM openjdk:8u252

LABEL author="Ife Jeremiah"

EXPOSE 8080

# Install and setup
COPY install.sh /root/tima-edutech/install.sh
COPY setup.sh /root/tima-edutech/setup.sh
COPY .env /root/tima-edutech/.env


RUN chmod +x /root/tima-edutech/setup.sh

RUN /root/tima-edutech/setup.sh

ADD target/tima-edutech-*.jar /opt/tima-edutech/tima-edutech-*.jar
WORKDIR /opt/tima-edutech

RUN chmod +x /root/tima-edutech/install.sh
CMD  /root/tima-edutech/install.sh