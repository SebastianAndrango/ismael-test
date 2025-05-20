PROFILE=${1:-dev} 

if [[ "$PROFILE" != "dev" && "$PROFILE" != "prod" ]]; then
  echo "Incorrect enviroment: '$PROFILE'"
  exit 1
fi

if [[ "$PROFILE" == "dev" ]]; then
  echo "Compiling and running in development mode"
  mvn compile quarkus:dev -Dquarkus.profile=dev

else
  echo "Packaging artifact for PROD…"
  mvn package -DskipTests

  ARTIFACT="target/quarkus-app/quarkus-run.jar"

  if [ ! -f "$ARTIFACT" ]; then
    echo "JAR file not found at '$ARTIFACT'."
    exit 1
  fi

  echo "JAR built successfully."
  echo "Starting in production mode"
  java -Dquarkus.profile=prod -jar "$ARTIFACT"
fi
