#/bin/sh
protoc --java_out=../src/main/java *.proto


/home/vst/soft/nanopb-0.3.9.1-linux-x86/nanopb-0.3.9.1-linux-x86/generator-bin/protoc --nanopb_out=../src/main/cpp *.proto

#/mnt/localdisk/my/repos/radio_proj/climate_datalogger_server/protocol/src/main/java/com/acumen/climatedataloger/protocol