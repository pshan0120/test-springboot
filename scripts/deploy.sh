#!/bin/bash

REPOSITORY=/home/ec2-user/app/step2
PROJECT_NAME=test-springboot

echo "> Build 파일 복사"

cp $REPOSITORY/zip/*.jar $REPOSITORY/

echo "> 현재 구동중인 애플리케이션 pid 확인"

# 현재 수행 중 스프링부트 PID를 찾고 실행중이면 종료
# 동일 이름의 프로그램이 있을 수 있기 때문에 jar로 ID를 찾음
#CURRENT_PID=$(pgrep -fl test-springboot | grep jar | awk '{print $1}')
# Amazon Linux2에서 변경되어야 할 부분
CURRENT_PID=$(pgrep -fl test-springboot | grep java | awk '{print $1}')

echo "현재 구동중인 어플리케이션 pid: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
    echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
    echo "> kill -15 $CURRENT_PID"
    kill -15 $CURRENT_PID
    sleep 5
fi

echo "> 새 어플리케이션 배포"

JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR Name: $JAR_NAME"

echo "> $JAR_NAME 에 실행권한 추가"

# jar파일은 실행권한이 없는 상태라서 nohup으로 실행되도록 권한 부여
chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"

# nohup 실행시 codeDeploy는 무한 대기하는 이슈를 해결하기 위해
# nohup.out 파일을 표준 입출력용으로 별도 사용
# 이렇게 안하면 nohup.out 파일 대신 codeDeploy 로그에 출력됨
# nohup이 끝나기 전까지 codeDeploy도 끝나지 않기 때문에 이렇게 해야 함
nohup java -jar \
    -Dspring.config.location=classpath:/application.properties,classpath:/application-real.properties,/home/ec2-user/app/application-oauth.properties,/home/ec2-user/app/application-real-db.properties \
    -Dspring.profiles.active=real \
    $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &
