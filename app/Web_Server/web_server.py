# -*- encoding:utf-8 -*-
import sys
reload(sys)
sys.setdefaultencoding('utf-8')

import socket
import json
import requests

def crawler(medicine_name):
    data = {'startCount':'0', 'requery':medicine_name, 'mode':'basic', 'sort':'RANK', 'collection':'kifda', 'item':medicine_name}
    result = requests.post("http://drug.mfds.go.kr/html/search.jsp", data)
    print result.text

# curl 'http://drug.mfds.go.kr/html/search.jsp' \
# -XPOST \
# -H 'Content-Type: application/x-www-form-urlencoded' \
# -H 'Origin: http://drug.mfds.go.kr' \
# -H 'Host: drug.mfds.go.kr' \
# -H 'Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8' \
# -H 'Connection: keep-alive' \
# -H 'Content-Length: 106' \
# -H 'Accept-Encoding: gzip, deflate' \
# -H 'Cookie: JSESSIONID=Ss7LRa1oBOYVrEEBxYcFxVQikp0doTiXv8gCNaMNdWGtojUqMFm8xNcKcN91vP3X.drugwas_servlet_engine11; elevisor_for_j2ee_uid=2aqmvura7nb39' \
# -H 'Upgrade-Insecure-Requests: 1' \
# -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/11.1 Safari/605.1.15' \
# -H 'Referer: http://drug.mfds.go.kr/' \
# -H 'Accept-Language: ko-kr' \
# --data 'startCount=0&requery=&mode=basic&sort=RANK&collection=kifda&rt2=&item=%ED%83%80%EC%9D%B4%EB%A0%88%EB%86%80'

# sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
# sock.bind(("192.168.0.122", 11114))
# sock.listen(5)
# try:
#     while True:
#         client, addr = sock.accept()
#         print client.recv(1024)
#         client.send("Hello")
#         client.close()
# except:
#     sock.close()

crawler("타이레놀")