import sqlite3
import json

memo_insert_sql = "insert into Memo (User, Date, Text, Image) values (?, ?, ?, ?)"
memo_search_sql = "select * from Memo where User=?"

register = "insert into User (Id, Password) values (?, ?)"
validation = "select * from User where id=?"
login = "w"

conn = sqlite3.connect("CapsuleTimer.db")
cur = conn.cursor()

def insert_memo(user, date, text="", image=""):
    result = {}

    if text == "" and image == "":
        result['result'] = 'No'
    else:
        cur.execute(memo_insert_sql, (user, date, text, image))
        conn.commit()
        result['result'] = 'Yes'
    return json.dumps(result)

def search_memo(user):
    result = {}
    result["memo"] = []
    index = ["id", "user", "time", "text", "image"]

    cur.execute(memo_search_sql, (user,))
    data = cur.fetchall()
    for d in data:
        result["memo"].append(dict(zip(index, list(d))))
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
    print 2
    result = {}
    cur.execute(login, (id, password))
    print 1
    data = cur.fetchall()
    if len(data) > 0:
        result['result'] = 'No'
    else:
        result['result'] = 'Yes'
    return json.dumps(result)
