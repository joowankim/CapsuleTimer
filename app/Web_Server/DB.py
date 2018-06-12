# -*- encoding: utf-8 -*-

import sys
reload(sys)
sys.setdefaultencoding("utf-8")
import sqlite3
import json
import datetime
import time
import calendar
import base64
import threading

memo_insert_sql = "insert into Memo (User, Date, Text, Image, medicine) values (?, ?, ?, ?, ?)"
memo_search_sql = "select * from Memo where User=? and Medicine=?"
memo_search_sql2 = "select * from Memo where User=?"
memo_position_search_sql = "select * from Memo where User=? and id=? and Medicine=?"
memo_delete_sql = "delete from memo where user=? and id=?"
memo_change_sql = "update memo set text=?, image=?, medicine=? where id=?"

register = "insert into User (Id, Password) values (?, ?)"
validation = "select * from User where id=?"
login = "select * from User where id=? and password=?"

medicine_add_sql = "insert into Medicine_name (User, medicine_name) values (?, ?)"
medicine_search_sql = "select * from Medicine_name where User = ?"
medicine_taking_sql = "select * from Medicine where User=? and Medicine_Name=? and Date >= ? and Date <= ?"
medicine_taken_sql = "insert into Medicine (User, Medicine_Name, Date) values (?, ?, ?)"

instant_report_add_sql = "insert into instant_report (hash_value, medicine_name, user_id) values (?, ?, ?)"
instant_report_search_sql = "select * from instant_report where hash_value = ?"
instant_report_delete_sql = "delete from instant_report where hash_value = ?"

conn = sqlite3.connect("CapsuleTimer.db", check_same_thread=False)
cur = conn.cursor()
lock = threading.Lock()

# Only for test
def change_DB(db):
    global conn
    global cur
    conn = sqlite3.connect(db, check_same_thread=False)
    cur = conn.cursor()

def instant_report_insert(hash_value, medicine_name, user_id):
    result = {}

    if hash_value == "" or medicine_name == "":
        result['result'] = 'No'
    else:
        lock.acquire()
        cur.execute(instant_report_add_sql, (hash_value, medicine_name, user_id))
        conn.commit()
        lock.release()
        result['result'] = 'Yes'
    return json.dumps(result)

def instant_report_search(hash_value):
    result = {}
    index = ["hash_value", "medicine_name", "user_id"]

    lock.acquire()
    cur.execute(instant_report_search_sql, (hash_value,))
    lock.release()
    data = cur.fetchall()[0]
    print data
    result["hash_value"] = data[1]
    result["medicine_name"] = data[2]
    result["user_id"] = data[3]
    print result
    return json.dumps(result)


def instant_report_delete(hash_value):
    result = {}
    try:
        lock.acquire()
        cur.execute(instant_report_delete_sql, (hash_value, ))
        conn.commit()
        lock.release()
        result['result'] = 'Yes'
    except Exception, e:
	print e
        result['result'] = 'No'
    return json.dumps(result)

def web_insert_memo(user, date, text="", image="", medicine_name=""):
    result = {}

    if text == "" and image == "":
        result['result'] = 'No'
    else:
        lock.acquire()
        cur.execute(memo_insert_sql, (user, date, text, image, medicine_name))
        conn.commit()
        lock.release()
        result['result'] = 'Yes'
    return json.dumps(result)

def insert_memo(user, date, text="", image="", medicine_name=""):
    result = {}

    if text == "" and image == "":
        result['result'] = 'No'
    else:
        lock.acquire()
        cur.execute(memo_insert_sql, (user, date, text, "/image/"+user+datetime.datetime.fromtimestamp(date).strftime("%Y-%m-%d-%H-%M-%S"), medicine_name))
        conn.commit()
        lock.release()
        with open("./image/"+user+datetime.datetime.fromtimestamp(date).strftime("%Y-%m-%d-%H-%M-%S")+".jpeg", 'wb') as f:
            f.write(base64.decodestring(image))
        result['result'] = 'Yes'
    return json.dumps(result)

def search_memo(user, medicine_name):
    result = {}
    result["memo"] = []
    index = ["id", "user", "time", "text", "image", "medicine_name"]

    if medicine_name == "*":
        lock.acquire()
        cur.execute(memo_search_sql2, (user, ))
        lock.release()
    else:
        lock.acquire()
        cur.execute(memo_search_sql, (user, medicine_name))
        lock.release()
    data = cur.fetchall()
    for d in data:
        # print type(''.join(datetime.datetime.fromtimestamp(d[2]).strftime('%Y-%m-%d %H:%M:%S')))
        d = list(d)
        d[2] = ''.join(datetime.datetime.fromtimestamp(d[2]).strftime('%Y-%m-%d %H:%M:%S'))
        result["memo"].append(dict(zip(index, list(d))))
    return json.dumps(result)

def search_memos(user, position, medicine_name):
    result = {}
    index = ["id", "user", "time", "text", "image", "medicine_name"]

    print memo_position_search_sql, user, position
    lock.acquire()
    cur.execute(memo_position_search_sql, (user, position, medicine_name))
    data = cur.fetchall()
    for d in data:
        # print type(''.join(datetime.datetime.fromtimestamp(d[2]).strftime('%Y-%m-%d %H:%M:%S')))
        d = list(d)
        d[2] = ''.join(datetime.datetime.fromtimestamp(d[2]).strftime('%Y-%m-%d %H:%M:%S'))
        result = dict(zip(index, list(d)))
    lock.release()
    return json.dumps(result)

def delete_memo(user, position):
    result = {}
    try:
        lock.acquire()
        cur.execute(memo_delete_sql, (user, position))
        conn.commit()
        lock.release()
        result['result'] = 'Yes'
    except:
        result['result'] = 'No'
    return json.dumps(result)

def change_memo(user, position, text, image, medicine_name, date):
    result = {}
    try:
        lock.acquire()
        cur.execute(memo_change_sql, (text, "/image/"+user+datetime.datetime.fromtimestamp(date).strftime("%Y-%m-%d-%H-%M-%S"), medicine_name, position))
	with open("./image/"+user+datetime.datetime.fromtimestamp(date).strftime("%Y-%m-%d-%H-%M-%S")+".jpeg", 'wb') as f:
            f.write(base64.decodestring(image))
        conn.commit()
        lock.release()
        result['result'] = 'Yes'
        print memo_change_sql, (text,medicine_name, position)
    except Exception, e:
        print e
        result['result'] = 'No'
    return json.dumps(result)


def user_validation(id):
    result = {}
    lock.acquire()
    cur.execute(validation, (id,))
    data = cur.fetchall()
    lock.release()
    if len(data) > 0:
        result['result'] = 'No'
    else:
        result['result'] = 'Yes'
    return json.dumps(result)

def user_register(id, password):
    result = {}
    lock.acquire()
    cur.execute(register, (id, password))
    conn.commit()
    lock.release()
    result['result'] = 'Yes'
    return json.dumps(result)

def user_login(id, password):
    result = {}
    lock.acquire()
    cur.execute(login, (id, password))
    data = cur.fetchall()
    lock.release()
    print data, len(data)
    if len(data) <= 0:
        result['result'] = 'No'
    else:
        result['result'] = 'Yes'
    return json.dumps(result)

def medicine_taking(user, medicine_name, from_date, to_date):
    index = ["User", "Medicine_Name", "Date"]
    result = {}
    result["record"] = []
    from_date = calendar.timegm(time.struct_time(time.strptime('%s-%s-%s' %(from_date[:4], from_date[4:6], from_date[6:]), '%Y-%m-%d'))) - 9*3600
    to_date = calendar.timegm(time.struct_time(time.strptime('%s-%s-%s' % (to_date[:4], to_date[4:6], to_date[6:]), '%Y-%m-%d'))) - 9 * 3600
    lock.acquire()
    cur.execute(medicine_taking_sql, (str(user), unicode(medicine_name), from_date, to_date))
    data = cur.fetchall()
    lock.release()
    for d in data:
        d = list(d)
        d[2] = ''.join(datetime.datetime.fromtimestamp(d[2]).strftime('%Y-%m-%d %H:%M:%S'))
        result["record"].append(dict(zip(index, list(d))))
    return json.dumps(result)

def medicine_taken(user, medicine_name, date):
    result = {}
    lock.acquire()
    cur.execute(medicine_taken_sql, (user, medicine_name, date))
    conn.commit()
    lock.release()
    result["result"] = "Yes"
    return json.dumps(result)

def medicine_add(user, medicine_name):
    result = {}
    lock.acquire()
    cur.execute(medicine_add_sql, (user, medicine_name))
    conn.commit()
    lock.release()
    result["result"] = "Yes"
    return json.dumps(result)

def medicine_search(user):
    index = ["medicine_id", "User", "medicine"]
    result = {}
    result["record"] = []
    lock.acquire()
    cur.execute(medicine_search_sql, (user, ))
    data = cur.fetchall()
    lock.release()
    for d in data:
        d = list(d)
        result["record"].append(dict(zip(index, list(d))))
    return json.dumps(result)
