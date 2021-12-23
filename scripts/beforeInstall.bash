if [ -d /home/ubuntu/build ]; then
    sudo rm -rf /home/ubuntu/build
fi

sudo mkdir -vp /home/ubuntu/build

docker stop $DOCKER_CONTAINER_NAME
docker rm $DOCKER_CONTAINER_NAME

if [[ $(docker images -q $DOCKER_ID/$DOCKER_REPOSITORY:$VERSION 2> /dev/null) != '' ]]; then
docker rmi -f $(docker images --format '{{.Repository}}:{{.Tag}}' --filter=reference='$DOCKER_ID/$DOCKER_REPOSITORY:$VERSION')
fi
