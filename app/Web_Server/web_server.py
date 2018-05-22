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
from flask import request, url_for
from flask import render_template, redirect, make_response
import threading
import os
from werkzeug import secure_filename
import datetime

app = Flask(__name__, static_url_path="/image", static_folder="image")
UPLOAD_FOLDER = '/Users/Knight/AndroidStudioProjects/CapsuleTimer/app/Web_Server/image'
ALLOWED_EXTENSIONS = set(['txt', 'pdf', 'png', 'jpg', 'jpeg', 'gif'])
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER

def allowed_file(filename):
    return '.' in filename and filename.rsplit('.', 1)[1] in ALLOWED_EXTENSIONS

@app.route('/')
def hello_world():
    if "userID" in request.cookies:
        login = {"bool":"true"}
    else:
        login = {"bool":"false"}
    return render_template('home.html', login=login)

@app.route('/login', methods=['POST', 'GET'])
def login():
    if request.method == "GET":
        return render_template("Login.html", res={"result":""})
    elif request.method == "POST":
        userID = request.form['userID']
        userPW = request.form['userPW']
        res = json.loads(DB.user_login(userID, userPW))
        if res['result'] == "Yes":
            response = make_response(redirect("/"))
            response.set_cookie("userID", userID)
            return response
        else:
            return render_template("Login.html", res=res)

@app.route('/register', methods=['POST', 'GET'])
def register():
    if request.method == "GET":
        return render_template("Register.html", res={"result":""})
    elif request.method == "POST":
        userID = request.form['userID']
        userPW = request.form['userPW']
        userREPW = request.form['userREPW']
        res = json.loads(DB.user_validation(userID))
        if res['result'] == "No":
            return render_template("Register.html", res={"result":"Duplicate"})
        elif len(userID) < 4:
            return render_template("Register.html", res={"result":"Short"})
        elif userPW != userREPW:
            return render_template("Register.html", res={"result":"Different"})
        else:
            DB.user_register(userID, userPW)
            return redirect("/login")

@app.route('/logout', methods=["GET"])
def logout():
    response = make_response(redirect('/'))
    response.set_cookie("userID", expires=0)
    return response

@app.route('/memo/write', methods=['POST', 'GET'])
def memo_write():
    if "userID" in request.cookies:
        if request.method == "GET":
            return render_template("Memo_write.html")
        elif request.method == "POST":
            image = request.files['image']
            memo_user = request.cookies.get("userID")
            memo_text = request.form['memo_text']
            cur_time = float(time.time())

            if image and allowed_file(image.filename):
                image.save(os.path.join(app.config['UPLOAD_FOLDER'], memo_user+datetime.datetime.fromtimestamp(cur_time).strftime("%Y-%m-%d-%H-%M-%S")+".jpeg"))
            DB.web_insert_memo(memo_user, cur_time, memo_text, "/image/" + memo_user + datetime.datetime.fromtimestamp(cur_time).strftime("%Y-%m-%d-%H-%M-%S"))
            return redirect("/memo")
    else:
        return redirect("/login")

@app.route('/memo')
def memo_list():
    if "userID" in request.cookies:
        result = json.loads(DB.search_memo(request.cookies.get("userID")))
        print result
        return render_template("Memo_list.html", memos=result)
    else:
        return redirect("/login")

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

@app.route('/report', methods=['GET'])
def report():
    user = request.cookies.get("userID")
    medicine = "Tylenol"
    result = json.loads(DB.medicine_taking(user, medicine, datetime.datetime.fromtimestamp(0).strftime("%Y%m%d"), datetime.datetime.fromtimestamp(time.time()).strftime("%Y%m%d")))
    res = []
    print result
    for date in result['record']:
        date = date['Date'].split(' ')
        print ''.join(date[0].split('-'))+''.join(date[1].split(':'))
        res.append(float(''.join(date[0].split('-'))+''.join(date[1].split(':')[:2])))
    res = dict(zip(range(len(res)), res))
    return render_template("Report.html", res=res)

def get_read_score(request):
    user = Login.get_current_user(request)
	#로그인이 되어있지 않다면 login_please.html 띄우기
    if user == -1:
        return render(request, "login_please.html")
	#읽기시험 최근 30개를 역순으로 출력
    read_scores = json.loads(user.readding_level)[:30][::-1]
    data = []
    # for idx in range(len(read_scores)):
    #     data.append({"close": read_scores[idx][0].strip('%'), "date": idx + 1})
	#각 시험의 점수 출력
    for idx, value in enumerate(read_scores):
        data.append({"close": value[0].strip('%'), "date": idx+1})
    # data = data[::-1]
    return JsonResponse(data, safe=False)

def smartphone_connection():
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock.bind(("", 6000))
    sock.listen(5)
    try:
        while True:
            client, addr = sock.accept()
            req = client.recv(10240)
            print req
            req = json.loads(req)
            if req['Type'] == 'Search_Medicine':
                client.send(MedicineSearch.crawler(req["Name"]))
            if req['Type'] == 'Write_Memo':
                client.send("1\n")
                length = int(client.recv(1024))
                client.send("1\n")
                image = ""
                print length
                if length%1024 == 0:
                    length /= 1024
                else:
                    length = length/1024+1
                while length > 0:
                    length -= 1
                    image += client.recv(1024)
                    client.send("1\n")
                print image
                client.send(DB.insert_memo(req['Id'], time.time(), req['Text'], image))
            if req['Type'] == 'Search_Memo':
                client.send(DB.search_memo(req['User']))
            if req['Type'] == "Register":
                client.send(DB.user_register(req['Id'], req['Password']))
            if req['Type'] == "Validation":
                client.send(DB.user_validation(req['Id']))
            if req['Type'] == 'Login':
                client.send(DB.user_login(req['Id'], req['Password']))
            if req['Type'] == 'Edit_Memo':
                client.send(DB.search_memos(req['Id'], req['Position']))
            if req['Type'] == 'Edit_Content':
                client.send(DB.change_memo(req['Id'], req['Position'], req['Text'], req['Image']))
            if req['Type'] == 'Delete_Memo':
                client.send(DB.delete_memo(req['Id'], req['Position']))
            if req['Type'] == 'Medicine_Record':
                client.send(DB.medicine_taking(req['Id'], req['Medicine_Name'], req['From'], req['To']))
            client.close()
    except Exception, e:
        print e
        sock.close()

if __name__ == '__main__':
    app.run()
    # threading.Thread(target=smartphone_connection).start()
    # print 1


# print DB.insert_memo("Tester", time.time(), "Test", "Test")
# DB.search_memo("Tester")

# # Search list of medicine with medicine name
# print repr(MedicineSearch.crawler("타이레놀"))
#
# # Search specific information of medicine with url
# # MedicineSearch.crawler("./bxsSearchDrugProduct.jsp?item_Seq=199303109")

