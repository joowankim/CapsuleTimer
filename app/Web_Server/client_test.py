# import os
# from flask import Flask
# from flask import request, url_for
# from flask import render_template, redirect, make_response
# from nltk.tree import Tree
# import pprint
# os.environ['CLASSPATH'] = os.environ['CLASSPATH']+"/Users/Knight/Downloads/stanford-parser-full-2018-02-27:"
# import nltk
# import random
#
# print os.environ['CLASSPATH']
# from nltk.parse import stanford
# print nltk.__version__
# # print Tree(sentences).flatten()
# # GUI
#
# app = Flask(__name__, static_url_path="/image", static_folder="image")
#
# def traverse_tree(tree, n, res):
#     if n == 0:
#         res.append([' '.join(tree.leaves()), random.randint(0, 255), random.randint(0, 255), random.randint(0, 255)])
#         return
#     for subtree in tree:
#         if type(subtree) == nltk.tree.Tree:
#             traverse_tree(subtree, n-1, res)
#         else:
#             print subtree
#             res.append(
#                 [subtree, random.randint(0, 255), random.randint(0, 255), random.randint(0, 255)])
# def parsing(sentences, level, res):
#     parser = stanford.StanfordParser()
#     sentences = parser.raw_parse_sents((sentences, u''))
#     for line in sentences:
#         for sentence in line:
#             print dir(sentence)
#             traverse_tree(sentence, 2 + level, res)
#
# @app.route("/", methods=["GET", "POST"])
# def home():
#     if request.method == "GET":
#         return render_template("nltk_home.html")
#     elif request.method == "POST":
#         sentence = request.form['sentence']
#         level = request.form['level']
#         res = []
#         parsing(sentence, int(level), res)
#         return render_template("nltk_home.html", res=res)
#
# if __name__ == '__main__':
#     app.run()

# import MedicineSearch
# import json
#
# print json.loads(MedicineSearch.crawler("//bxsSearchDrugProduct.jsp?item_Seq=199402278"))["usage"]

# import random
#
# print [random.randrange(1, 10) for _ in range(10)]
# import time
# print time.time()
# import datetime
# import datetime
# import calendar
# import time
# # print datetime.datetime.now()
#
# print calendar.timegm(time.struct_time(time.strptime(datetime.datetime.fromtimestamp(), '%Y-%m-%d %H:%M:%S')))
# import weasyprint
# pdf = weasyprint.HTML('http://www.google.com').write_pdf()
# file('google.pdf', 'w').write(pdf)

third = [80.46, 82.52, 80.44, 90.77, 82.48, 90.65, 89.06, 84.06, 91.13, 90.77, 83.52, 82.77, 66.77]
second = [78.5, 94.1042, 88.5208, 90.4375, 81.9583, 76.7292, 90.375, 76.25, 85.9167, 89.35417, 81.9375, 78.75, 69.4792]
first = [93.1667, 87.3125, 88.0625, 90.5625, 83.9167, 88.75, 81.1667, 87.375, 84.0625, 96.58333, 93.0833, 90.0625, 76.6875]

res = []
for idx in range(len(first)):
    res.append(first[idx] + second[idx] + third[idx])

# for idx in range(1, len(res)):
#     if res[0] < res[idx]:
#         print res[idx],

print sorted(res)[::-1][0] - res[0]