# -*- encoding: utf-8 -*-
import sys
reload(sys)
sys.setdefaultencoding('utf-8')

import unittest
import web_server
import json
import flask
from flask import Flask
from flask import url_for
from flask_testing import TestCase
# from flask.ext.testing import TestCase

class MyTestCase(TestCase):

    def create_app(self):

        app = Flask(web_server.__name__)
        app.config['TESTING'] = True
        return app

    def test_something(self):
        # Main page testing
        with web_server.app.test_client() as c:
            result = c.get('/')
            self.assertEqual(type(result), flask.wrappers.Response)
            self.assertEqual(result._status, "200 OK")

        # Login testing
        with web_server.app.test_client() as c:
            test_user = [
                ["test", "test"],
                ["arien", "TerveinvSt"],
                ["dewin", "fren"],
                ["voenv", "sadoew"]
            ]
            result = c.post("/login", data = dict(userID = test_user[0][0], userPW = test_user[0][1]), follow_redirects = True)
            self.assertEquals(result.status_code, 200)


            for user in test_user[1:]:
                result = c.post("/login", data = dict(userID = user[0], userPW = user[1]), follow_redirects = True)
                self.assertEquals(result._status_code, 200)

        # Register testing
        with web_server.app.test_client() as c:
            test_user = [
                ["abcd", "abcd", "abcd"],
                ["apple", "pineapple", "pineapple"],
                ["google", "android", "apple"],
                ["I_am", "too_tired", "I_need_vacation"]
            ]

            for user in test_user[:2]:
                # result = c.post("/register", data = dict(userID = user[0], userPW = user[1], userREPW = user[2]), follow_redirects = True)
                # self.assertEqual(result._status, "200 OK")
                result = c.post("/login", data=  dict(userID = user[0], userPW = user[1]), follow_redirects = True)
                self.assertEquals(result.status_code, 200)


            for user in test_user[2:]:
                result = c.post("/register", data=dict(userID=user[0], userPW=user[1], userREPW=user[2]),
                                follow_redirects=True)
                self.assertEquals(result.status_code, 200)
                result = c.post("/login", data=dict(userID=user[0], userPW=user[1]),
                                follow_redirects=True)
                self.assertEquals(result._status_code, 200)

            for user in test_user[:2]:
                result = c.post("/register", data = dict(userID = user[0], userPW = user[1], userREPW = user[2]), follow_redirects = True)
                self.assertEqual(result.status_code, 200)
                result = c.post("/login", data=  dict(userID = user[0], userPW = user[1]), follow_redirects = True)
                self.assertEqual(result.status_code, 200)

        with web_server.app.test_client() as c:
            report_user = ["", "test"]

            result = c.post("/login", data=dict(userID=report_user[0], userPW=report_user[1]), follow_redirects=True)
            self.assertEqual(result._status_code, 200)
            result = c.get("/report")
            self.assertEqual(result._status_code, 302)

            result = c.get("/report")
            self.assertEqual(result._status_code, 302)


        with web_server.app.test_client() as c:
            test_medicine = ["타이레놀", "비타민", "밴드", "파스", "부루펜", "후시딘", "인사돌", "이가탄", "겔포스"]

            for medicine in test_medicine:
                result = c.post("/search/medicine", data=dict(medicine_name=medicine), follow_redirects=True)
                self.assertEqual(result.status_code, 200)

        with web_server.app.test_client() as c:
            report_user = ["test", "test"]

            result = c.post("/login", data=dict(userID=report_user[0], userPW=report_user[1]), follow_redirects=True)
            self.assertEqual(result._status_code, 200)
            result = c.get("/report")
            self.assertEqual(result._status_code, 200)

            result = c.get("/report")
            self.assertEqual(result._status_code, 200)


if __name__ == '__main__':
    unittest.main()
