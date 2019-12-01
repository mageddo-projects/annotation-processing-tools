set -e

SCRIPT_DIR=$(dirname $0)
cd $SCRIPT_DIR

echo "> compiling"
./mvnw clean install

echo "> generating native image"
native-image -J-Xmx5G --no-server -cp ./target/reflection-configuration-example-1.0.0-jar-with-dependencies.jar

echo "> running"
./reflection-configuration-example
