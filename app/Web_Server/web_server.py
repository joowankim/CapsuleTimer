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
from flask import render_template, redirect
app = Flask(__name__)

@app.route('/')
def hello_world():
    return render_template('home.html')

@app.route('/memo/write', methods=['POST', 'GET'])
def memo_write():
    if request.method == "GET":
        return render_template("Memo_write.html")
    elif request.method == "POST":
        memo_user = request.form['memo_user']
        memo_text = request.form['memo_text']
        memo_image = request.form['memo_image']
        DB.insert_memo(memo_user, time.time(), memo_text, memo_image)
        return redirect("/memo")

@app.route('/memo')
def memo_list():
    result = json.loads(DB.search_memo("Test"))
    print result
    return render_template("Memo_list.html", memos=result)

@app.route('/search/medicine/<medicine_link>')
def specific_medicine(medicine_link):
    medicine_link = "./"+medicine_link+"?item_Seq="+request.args.get("item_Seq")
    result = json.loads(MedicineSearch.crawler(medicine_link))
    pprint.pprint(result)
    return render_template('Medicine_info.html', medicine=result)

@app.route('/search/medicine', methods=['POST'])
def search_medicine():
    medicine_name = request.form['medicine_name']
    result = json.loads(MedicineSearch.crawler(medicine_name))
    pprint.pprint(result)
    return render_template('Medicine_list.html', medicines=result)

if __name__ == '__main__':
    app.run()
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
            if req['Type'] == 'Write_Memo':
                client.send(DB.insert_memo("TEST", time.time(), req['Text'], req['Image']))
            if req['Type'] == 'Search_Memo':
                client.send(DB.search_memo(req['User']))
            if req['Type'] == "Register":
                client.send(DB.user_register(req['Id'], req['Password']))
            if req['Type'] == "Validation":
                client.send(DB.user_validation(req['Id']))
            if req['Type'] == 'Login':
                client.send(DB.user_login(req['Id'], req['Password']))
            client.close()
    except Exception, e:
        print e
        sock.close()

# print DB.insert_memo("Tester", time.time(), "Test", "Test")
# DB.search_memo("Tester")
# print DB.user_validation("Test")

# # Search list of medicine with medicine name
# print repr(MedicineSearch.crawler("타이레놀"))
#
# # Search specific information of medicine with url
# MedicineSearch.crawler("./bxsSearchDrugProduct.jsp?item_Seq=200302348")

