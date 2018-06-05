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

print hex(1118480)
print 0x01000000 & 1118480
