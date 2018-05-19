import sqlite3
import json
import datetime
import time
import calendar
import base64

memo_insert_sql = "insert into Memo (User, Date, Text, Image) values (?, ?, ?, ?)"
memo_search_sql = "select * from Memo where User=?"
memo_position_search_sql = "select * from Memo where User=? and id=?"
memo_delete_sql = "delete from memo where user=? and id=?"
memo_change_sql = "update memo set text=?, image=? where id=?"

register = "insert into User (Id, Password) values (?, ?)"
validation = "select * from User where id=?"
login = "select * from User where id=? and password=?"

medicine_taking_sql = "select * from Medicine where User=? and Medicine_Name=? and Date >= ? and Date <= ?"

conn = sqlite3.connect("CapsuleTimer.db", check_same_thread=False)
cur = conn.cursor()

def web_insert_memo(user, date, text="", image=""):
    result = {}

    if text == "" and image == "":
        result['result'] = 'No'
    else:
        cur.execute(memo_insert_sql, (user, date, text, image))
        conn.commit()
        result['result'] = 'Yes'
    return json.dumps(result)

def insert_memo(user, date, text="", image=""):
    result = {}

    if text == "" and image == "":
        result['result'] = 'No'
    else:
        cur.execute(memo_insert_sql, (user, date, text, "/image/"+user+datetime.datetime.fromtimestamp(date).strftime("%Y-%m-%d-%H-%M-%S")))
        conn.commit()
        with open("/image/"+user+datetime.datetime.fromtimestamp(date).strftime("%Y-%m-%d-%H-%M-%S"), 'wb') as f:
            f.write(base64.decodestring(image))
        result['result'] = 'Yes'
    return json.dumps(result)

def search_memo(user):
    result = {}
    result["memo"] = []
    index = ["id", "user", "time", "text", "image"]

    cur.execute(memo_search_sql, (user,))
    data = cur.fetchall()
    for d in data:
        # print type(''.join(datetime.datetime.fromtimestamp(d[2]).strftime('%Y-%m-%d %H:%M:%S')))
        d = list(d)
        d[2] = ''.join(datetime.datetime.fromtimestamp(d[2]).strftime('%Y-%m-%d %H:%M:%S'))
        result["memo"].append(dict(zip(index, list(d))))
    return json.dumps(result)

def search_memos(user, position):
    result = {}
    index = ["id", "user", "time", "text", "image"]

    print memo_position_search_sql, user, position
    cur.execute(memo_position_search_sql, (user, position))
    data = cur.fetchall()
    for d in data:
        # print type(''.join(datetime.datetime.fromtimestamp(d[2]).strftime('%Y-%m-%d %H:%M:%S')))
        d = list(d)
        d[2] = ''.join(datetime.datetime.fromtimestamp(d[2]).strftime('%Y-%m-%d %H:%M:%S'))
        result = dict(zip(index, list(d)))
    return json.dumps(result)

def delete_memo(user, position):
    result = {}
    try:
        cur.execute(memo_delete_sql, (user, position))
        conn.commit()
        result['result'] = 'Yes'
    except:
        result['result'] = 'No'
    return json.dumps(result)

def change_memo(user, position, text, image):
    result = {}
    try:
        cur.execute(memo_change_sql, (text, image, position))
        conn.commit()
        result['result'] = 'Yes'
    except:
        result['result'] = 'No'
    return json.dumps(result)


def user_validation(id):
    result = {}
    cur.execute(validation, (id,))
    data = cur.fetchall()
    if len(data) > 0:
        result['result'] = 'No'
    else:
        result['result'] = 'Yes'
    return json.dumps(result)

def user_register(id, password):
    result = {}
    cur.execute(register, (id, password))
    conn.commit()
    result['result'] = 'Yes'
    return json.dumps(result)

def user_login(id, password):
    result = {}
    cur.execute(login, (id, password))
    data = cur.fetchall()
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
    print from_date, to_date
    cur.execute(medicine_taking_sql, (user, medicine_name, from_date, to_date))
    data = cur.fetchall()
    for d in data:
        d = list(d)
        d[2] = ''.join(datetime.datetime.fromtimestamp(d[2]).strftime('%Y-%m-%d %H:%M:%S'))
        result["record"].append(dict(zip(index, list(d))))
    return json.dumps(result)

# times = datetime.datetime.fromtimestamp(time.time()).strftime('%Y-%m-%d %H:%M:%S')
# print times
# changed = calendar.timegm(time.struct_time(time.strptime(times, '%Y-%m-%d %H:%M:%S'))) - 9 * 3600
# print datetime.datetime.fromtimestamp(changed).strftime('%Y-%m-%d %H:%M:%S')