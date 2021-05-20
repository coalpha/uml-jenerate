run:
	mvn compile exec:java

build:
	mvn compile

clean:
	mvn clean

.PHONY: run build clean
