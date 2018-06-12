# -*- encoding: utf-8 -*-
import sys
import random
reload(sys)
sys.setdefaultencoding('utf-8')
import unittest
import json
import MedicineSearch


class MyTestCase(unittest.TestCase):
    def test_something(self):
        medicine = ["tylenol", "케토톱", "비타민", "부루펜", "키미테", "아스피린", "파스", "보나링", "펜잘", "게보린", "이가탄", "둘코락스", "활명수", "베나치오"]
        url = [
            "./bxsSearchDrugProduct.jsp?item_Seq=200302348",
            "./bxsSearchDrugProduct.jsp?item_Seq=200202893",
            "./bxsSearchDrugProduct.jsp?item_Seq=199303108",
            "./bxsSearchDrugProduct.jsp?item_Seq=199903739",
            "./bxsSearchDrugProduct.jsp?item_Seq=199602996",
            "./bxsSearchDrugProduct.jsp?item_Seq=199603003",
            "./bxsSearchDrugProduct.jsp?item_Seq=199603002",
            "./bxsSearchDrugProduct.jsp?item_Seq=199402278",
            "./bxsSearchDrugProduct.jsp?item_Seq=199303109",
            "./bxsSearchDrugProduct.jsp?item_Seq=200605346",
            "./bxsSearchDrugProduct.jsp?item_Seq=199600275",
            "./bxsSearchDrugProduct.jsp?item_Seq=199600277",
            "./bxsSearchDrugProduct.jsp?item_Seq=199300356",
            "./bxsSearchDrugProduct.jsp?item_Seq=201701674",
            "./bxsSearchDrugProduct.jsp?item_Seq=201700241",
            "./bxsSearchDrugProduct.jsp?item_Seq=201700004",
            "./bxsSearchDrugProduct.jsp?item_Seq=201602437",
            "./bxsSearchDrugProduct.jsp?item_Seq=201405294",
            "./bxsSearchDrugProduct.jsp?item_Seq=201206324",
            "./bxsSearchDrugProduct.jsp?item_Seq=201205962",
            "./bxsSearchDrugProduct.jsp?item_Seq=201005179",
            "./bxsSearchDrugProduct.jsp?item_Seq=201005178",
            "./bxsSearchDrugProduct.jsp?item_Seq=200906542"
        ]
        url += medicine
        random.shuffle(url)
        for case in url:
            print case
            result = MedicineSearch.crawler(case)
            self.assertEqual(type(result), str)
            try:
                result = json.loads(result)
            except:
                self.fail("Can't load string to json")
            if len(result) > 0:
                if "/" in case:
                    keys = sorted(result.keys())
                    std_keys = sorted(["name", "effect", "usage", "notice", "image"])
                    for idx in range(len(keys)):
                        self.assertEqual(keys[idx], std_keys[idx])
                        self.assertTrue(len(result[keys[idx]]) > 0)

                if "/" not in case:
                    keys = sorted(result["medicine"][0].keys())
                    std_keys = sorted(["Sequence", "name", "link", "ingredient", "ningredient", "company", "type"])
                    for idx in range(len(keys)):
                        self.assertEqual(keys[idx], std_keys[idx])
                        self.assertTrue(len(result["medicine"][0][keys[idx]]) > 0)


if __name__ == '__main__':
    unittest.main()
