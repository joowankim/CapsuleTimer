# -*- encoding:utf-8 -*-
import sys
reload(sys)
sys.setdefaultencoding('utf-8')

import socket
import json
import pprint
import MedicineSearch
from flask import Flask
from flask import request
from flask import render_template
# app = Flask(__name__)
#
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
# # if __name__ == '__main__':
# #     app.run()
#
#
#
# sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
# sock.bind(("192.168.0.122", 11113))
# sock.listen(5)
# try:
#     while True:
#         client, addr = sock.accept()
#         req = client.recv(1024)
#         print req
#         req = json.loads(req)
#         client.send(MedicineSearch.crawler(req["Name"]))
#         client.close()
# except:
#     sock.close()

# Search list of medicine with medicine name
print repr(MedicineSearch.crawler("타이레놀"))

# Search specific information of medicine with url
# MedicineSearch.crawler("./bxsSearchDrugProduct.jsp?item_Seq=199303109")