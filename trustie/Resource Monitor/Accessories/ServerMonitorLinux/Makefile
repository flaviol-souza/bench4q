
CPPFLAGS = -shared 
TARGET = org_bench4Q_servermonitor_ServerDataManager
BINARY = libServerDataProvider
JAVA_HOME = /usr/lib/jvm/default-java
INCLUDES = -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux
LINKS = -L$(JAVA_HOME)/include -L$(JAVA_HOME)/include/linux

all: 
	g++ -c -fPIC $(INCLUDES) *.cpp 
	g++ $(LINKS) *.o $(CPPFLAGS) -o $(BINARY).so

clean:
	rm *.o *.so

