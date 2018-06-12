import string
import random
import unittest
import json
import DB
import time
import calendar
from datetime import datetime

def id_generator(size=6, chars=string.ascii_uppercase + string.digits):
    return ''.join(random.choice(chars) for _ in range(size))

class MyTestCase(unittest.TestCase):

    def test_something(self):
        db_name = "test_db.db"
        DB.change_DB(db=db_name)

        # for i in range(10):
        #     test_id = id_generator(random.randrange(1, 10))
        #     test_pw = id_generator(random.randrange(1, 10))
        #     print "Test #", i+1,":",  test_id, test_pw
        #
        #     result = DB.user_register(test_id, test_pw)
        #     self.assertEqual(type(result), str)
        #     try:
        #         result = json.loads(result)
        #     except:
        #         self.fail("JSON load fail")
        #     self.assertEqual(result["result"], "Yes")
        #
        #     result = DB.user_validation(test_id)
        #     self.assertEqual(type(result), str)
        #     try:
        #         result = json.loads(result)
        #     except:
        #         self.fail("JSON load fail")
        #     self.assertEqual(result["result"], "No")
        #
        #     result = DB.user_login(test_id, test_pw)
        #     self.assertEqual(type(result), str)
        #     try:
        #         result = json.loads(result)
        #     except:
        #         self.fail("JSON load fail")
        #     self.assertEqual(result["result"], "Yes")
        #
        # for i in range(10):
        #     test_user = id_generator(random.randrange(1, 30))
        #     test_medicine = [id_generator(random.randrange(1, 30)) for _ in range(10)]
        #     test_medicine_taken = {}
        #     for item in test_medicine:
        #         test_medicine_taken[item] = [random.randrange(0, 152846712057)/100.0 for _ in range(10)]
        #         DB.medicine_add(test_user, item)
        #         for taken in test_medicine_taken[item]:
        #             DB.medicine_taken(test_user, item, taken)
        #
        #     result = DB.medicine_search(test_user)
        #     self.assertEqual(type(result), str)
        #     try:
        #         result = json.loads(result)
        #     except:
        #         self.fail("JSON load fail")
        #     for item in result["record"]:
        #         self.assertTrue(item["medicine"] in test_medicine)
        #         taken = DB.medicine_taking(test_user, item["medicine"], "19700101", "21001231")
        #         print taken
        #         self.assertEqual(type(taken), str)
        #         try:
        #             taken = json.loads(taken)
        #         except:
        #             self.fail("JSON load fail")
        #
        #         for idx in range(len(test_medicine_taken[item["medicine"]])):
        #             test_medicine_taken[item["medicine"]][idx] = int(test_medicine_taken[item["medicine"]][idx])
        #
        #         for t in taken["record"]:
        #             tmp_time = calendar.timegm(time.struct_time(
        #                 time.strptime(t["Date"], '%Y-%m-%d %H:%M:%S')))
        #             print tmp_time - 9*3600, test_medicine_taken[item["medicine"]]
        #             self.assertTrue(int(tmp_time) - 9*3600 in test_medicine_taken[item["medicine"]])

        for i in range(10):
            test_user = id_generator(random.randint(1, 30))
            test_memo_text = [id_generator(random.randint(1, 400)) for _ in range(10)]
            test_memo_medicine = [id_generator(random.randint(1, 30)) for _ in range(10)]
            test_memo_text_change = [id_generator(random.randint(1, 400)) for _ in range(10)]
            test_memo_medicine_change = [id_generator(random.randint(1, 30)) for _ in range(10)]
            for idx in range(len(test_memo_text)):
                DB.insert_memo(test_user, time.time(), test_memo_text[idx], "", test_memo_medicine[idx])

            inserted_memo = DB.search_memo(test_user, "*")
            self.assertEqual(type(inserted_memo), str)
            try:
                result = json.loads(inserted_memo)
            except:
                self.fail("JSON load fail")
            for memo in result["memo"]:
                self.assertTrue(memo["user"] == test_user)
                self.assertTrue(memo["text"] in test_memo_text)
                self.assertTrue(memo["medicine_name"] in test_memo_medicine)
                inserted_memo2 = DB.search_memo(test_user, memo["medicine_name"])
                self.assertEqual(type(inserted_memo2), str)
                try:
                    mini_result = json.loads(inserted_memo2)
                except:
                    self.fail("JSON load fail")

                for mini in mini_result["memo"]:
                    self.assertTrue(mini["user"] == test_user)
                    self.assertTrue(mini["text"] in test_memo_text)
                    self.assertTrue(mini["medicine_name"] in test_memo_medicine)

                inserted_memo3 = DB.search_memos(test_user, memo["id"], memo["medicine_name"])
                self.assertEqual(type(inserted_memo3), str)
                try:
                    mini_result2 = json.loads(inserted_memo3)
                except:
                    self.fail("JSON load fail")
                mini = mini_result2
                self.assertTrue(mini["user"] == test_user)
                self.assertTrue(mini["text"] in test_memo_text)
                self.assertTrue(mini["medicine_name"] in test_memo_medicine)

            for idx in range(len(test_memo_text_change)):
                result = DB.change_memo(test_user, time.time(), test_memo_text_change[idx], "", test_memo_medicine_change[idx], time.time())
                self.assertEqual(type(result), str)
                try:
                    result = json.loads(result)
                except:
                    self.fail("JSON load fail")
                self.assertEqual(result["result"], "Yes")

            inserted_memo = DB.search_memo(test_user, "*")
            self.assertEqual(type(inserted_memo), str)
            try:
                result = json.loads(inserted_memo)
            except:
                self.fail("JSON load fail")
            for memo in result["memo"]:
                self.assertTrue(memo["user"] == test_user)
                self.assertTrue(memo["text"] in test_memo_text)
                self.assertTrue(memo["medicine_name"] in test_memo_medicine)
                inserted_memo2 = DB.search_memo(test_user, memo["medicine_name"])
                self.assertEqual(type(inserted_memo2), str)
                try:
                    mini_result = json.loads(inserted_memo2)
                except:
                    self.fail("JSON load fail")

                for mini in mini_result["memo"]:
                    self.assertTrue(mini["user"] == test_user)
                    self.assertTrue(mini["text"] in test_memo_text)
                    self.assertTrue(mini["medicine_name"] in test_memo_medicine)

                inserted_memo3 = DB.search_memos(test_user, memo["id"], memo["medicine_name"])
                self.assertEqual(type(inserted_memo3), str)
                try:
                    mini_result2 = json.loads(inserted_memo3)
                except:
                    self.fail("JSON load fail")

                mini = mini_result2
                self.assertTrue(mini["user"] == test_user)
                self.assertTrue(mini["text"] in test_memo_text)
                self.assertTrue(mini["medicine_name"] in test_memo_medicine)

            for memo in result["memo"]:
                delete_result = DB.delete_memo(test_user, memo["id"])
                self.assertEqual(type(delete_result), str)
                try:
                    delete_result = json.loads(delete_result)
                except:
                    self.fail("JSON load fail")
                self.assertEqual(delete_result["result"], "Yes")


if __name__ == '__main__':
    unittest.main()
