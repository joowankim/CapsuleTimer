# -*- encoding:utf-8 -*-

import sys
reload(sys)
sys.setdefaultencoding('utf-8')

import time
import socket
import json
import pprint
import MedicineSearch
import DB
import sqlite3
from flask import Flask
from flask import request
from flask import render_template
app = Flask(__name__)

# @app.route('/')
# def hello_world():
#     return render_template('home.html')
#
# @app.route('/search/medicine', methods=['POST'])
# def search_medicine():
#     print 1
#     medicine_name = request.form['medicine_name']
#     result = MedicineSearch.crawler(medicine_name)
#     pprint.pprint(result)
#     # if type(result) == list:
#     return render_template('home.html')
#
if __name__ == '__main__':
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock.bind(("", 6000))
    sock.listen(5)
    try:
        while True:
            client, addr = sock.accept()
            req = client.recv(1024)
            print req
            req = json.loads(req)
            if req['Type'] == 'Search_Medicine':
                    client.send(MedicineSearch.crawler(req["Name"]))
            if 
            client.close()
    except Exception, e:
        print e
        sock.close()

# print DB.insert_memo("Tester", time.time(), "Test", "Test")

# # Search list of medicine with medicine name
# print repr(MedicineSearch.crawler("타이레놀"))
#
# # Search specific information of medicine with url
# # MedicineSearch.crawler("./bxsSearchDrugProduct.jsp?item_Seq=199303109")
