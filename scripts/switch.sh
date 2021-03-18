#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

function switch_proxy() {
    IDLE_PORT=$(find_idle_port)

    echo "> 전환할 Port: $IDLE_PORT"
    echo "> Port 전환"
    # 하나의 문장을 만들어 파이프라인(|)으로 넘겨주기 위해 echo 사용
    # nginx가 변경할 프록시 주소 생성
    # 쌍따옴표로 사용해야 함, 그렇지 않으면 $service_url을 인식하지 못하고 변수를 찾게 됨
    # sudo 이후는 앞에서 넘긴 문장을 service-url.inc에 덮어씌우는 의미
    echo "set \$service_url http://127.0.0.1:${IDLE_PORT};" | sudo tee /etc/nginx/conf.d/service-url.inc

    echo "> 엔진엑스 Reload"
    # nginx설정을 다시 불러옴
    # restart와는 다름. reload는 끊김 없이 다시 불러옴
    # 중요한 설정들은 restart로 해야 반영됨
    # 여기서는 외부 설정 파일 service-url.inc 만 불러오므로 reload로 가능
    sudo service nginx reload
}
