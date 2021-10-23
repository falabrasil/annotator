FROM alpine:3.13.6

RUN apk update && apk add openjdk8-jre-base

COPY fb_nlplib.jar /

CMD ["sh", "-c", "java -jar fb_nlplib.jar -t $(nproc) -s -i /dev/stdin"]
