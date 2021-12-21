docker pull $DOCKER_IMAGE
docker run -e "SPRING_PROFILES_ACTIVE=prod" --publish $LOCAL_PORT_NUMBER:$DOCKER_PORT_NUMBER -it --detach --name $DOCKER_CONTAINER_NAME $DOCKER_IMAGE /bin/bash
