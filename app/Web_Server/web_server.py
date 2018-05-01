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