
CPPFLAGS = -shared -lc
TARGET = org_bench4Q_servermonitor_ServerDataManager
BINARY = libServerDataProvider


all: 
	g++ -c -fPIC $(TARGET).cpp -o $(TARGET).o
	g++ $(TARGET).o $(CPPFLAGS) -o $(BINARY).so

clean:
	rm *.o

